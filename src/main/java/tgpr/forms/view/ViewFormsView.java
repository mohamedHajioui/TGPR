package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import tgpr.forms.controller.ViewFormsController;
import tgpr.forms.model.*;

import java.util.Comparator;
import java.util.List;

public class ViewFormsView extends BasicWindow {
    private final Button openButton = new Button("Open");
    private final Button manageButton = new Button("Manage");
    private final Button createNewFormButton = new Button("Create a new form");
    private final Button firstButton = new Button("First");
    private final Button previousButton = new Button("Previous");
    private final Button nextButton = new Button("Next");
    private final Button lastButton = new Button("Last");
    private final ViewFormsController controller;
    private final TextBox filterTextBox = new TextBox();
    //variables pour la pagination
    private final int formsPerPage = 9;
    private int currentPage = 0;
    private Label pageLabel;

    // Déclaration du mainPanel et du formsPanel
    private Panel mainPanel;
    private Panel formsPanel;

    private final User currentUser;

    public ViewFormsView(ViewFormsController controller, User currentUser) {
        this.controller = controller;
        this.currentUser = currentUser;
        String email = Security.getLoggedUser().getEmail();

        // Initialisation de mainPanel
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        setTitle("MyForms (" + email + ")");
        setHints(List.of(Hint.CENTERED, Hint.MODAL));
        setCloseWindowWithEscape(true);

        // Ajouter les boutons "File" et "Parameters" en haut à gauche
        Panel topPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Button fileButton = new Button("File");
        fileButton.addListener(button -> openFileMenu());
        Button parametersButton = new Button("Parameters");
        topPanel.addComponent(fileButton);
        topPanel.addComponent(parametersButton);

        Panel centerPanel = new Panel(new GridLayout(2));
        centerPanel.setPreferredSize(new TerminalSize(110, 2));  // Limiter la hauteur du panneau central

        //zone de texte pour le filtre
        Panel filterPanel = filterBox(controller);


        Panel buttonCreateNewForm = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonCreateNewForm.addComponent(createNewFormButton);
        buttonCreateNewForm.addComponent(new EmptySpace(new TerminalSize(40, 1)));

        // Panneau de navigation en bas
        Panel navigationPanel = buttonsNavigation(controller);
        buttonCreateNewForm.addComponent(navigationPanel);

        // Initialisation du formsPanel avec une taille préférée
        formsPanel = new Panel(new GridLayout(3));
        formsPanel.setPreferredSize(new TerminalSize(100, 28));  // Définir une taille plus petite pour les formulaires

        // Ajouter les panneaux à mainPanel
        mainPanel.addComponent(topPanel);       // Boutons "File" et "Parameters"
        mainPanel.addComponent(filterPanel);    // Zone de filtre
        mainPanel.addComponent(centerPanel);    // Panneau central (Open/Manage)
        mainPanel.addComponent(formsPanel);     // Les formulaires seront affichés ici
        mainPanel.addComponent(buttonCreateNewForm);    // Panneau de navigation avec "Create a new form"

        setComponent(mainPanel);
    }

    private Panel filterBox(ViewFormsController controller) {
        Panel filterPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        filterPanel.addComponent(new Label("Filter:"));
        filterTextBox.setPreferredSize(new TerminalSize(30, 1));
        filterTextBox.setTextChangeListener(((newText, changedByUser) -> controller.filterForms(newText)));
        filterPanel.addComponent(filterTextBox);
        return filterPanel;
    }

    private Panel buttonsNavigation(ViewFormsController controller) {
        Panel navigationPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        navigationPanel.addComponent(firstButton);
        firstButton.addListener(button -> controller.goToFirstPage());
        navigationPanel.addComponent(previousButton);
        previousButton.addListener(button -> controller.goToPreviousPage());

        // Texte de pagination entre Previous et Next
        pageLabel = new Label("Page 1 of 1");
        navigationPanel.addComponent(pageLabel);

        navigationPanel.addComponent(nextButton);
        nextButton.addListener(button -> controller.goToNextPage());
        navigationPanel.addComponent(lastButton);
        lastButton.addListener(button -> controller.goToLastPage());
        return navigationPanel;
    }

    // Méthode pour afficher les formulaires

    public void displayForms(List<Form> forms, int currentPage, int formsPerPage) {
        forms.sort(Comparator.comparing(Form::getTitle, String.CASE_INSENSITIVE_ORDER));
        formsPanel.removeAllComponents();  // Supprimer les anciens composants

        GridLayout gridLayout = new GridLayout(3);
        gridLayout.setVerticalSpacing(0);
        formsPanel.setLayoutManager(gridLayout);

        // Calculer les formulaires à afficher en fonction de la page actuelle
        int start = currentPage * formsPerPage;
        int end = Math.min(start + formsPerPage, forms.size());

        for (int i = start; i < end; i++) {
            Form form = forms.get(i);
            if (form != null) {
                // Créer un panneau pour chaque formulaire avec son titre et sa description
                Panel formPanel = new Panel(new LinearLayout(Direction.VERTICAL));
                formPanel.setPreferredSize(new TerminalSize(70, 10));

                Label labelTitle = new Label(form.getTitle());
                labelTitle.setForegroundColor(TextColor.ANSI.BLUE_BRIGHT);
                labelTitle.center();
                formPanel.addComponent(labelTitle);

                Label description = new Label(form.getDescription() != null ? form.getDescription() : "No description");
                description.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
                description.center();
                formPanel.addComponent(description);

                formPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

                Label created = new Label("Created by " + currentUser.getName());
                created.center();
                formPanel.addComponent(created);

                var instance = form.getMostRecentInstance(currentUser);
                String startDate = (instance != null) ? instance.getStarted().toString() : "Not started";
                Label startLabel = new Label(startDate);
                startLabel.center();
                formPanel.addComponent(startLabel);

                String submissionDate = (instance != null && instance.getCompleted() != null) ?
                        instance.getCompleted().toString() : "In Progress";
                Label submissionLabel = new Label(submissionDate);
                if (startDate == null){
                    submissionLabel = new Label("");
                }
                submissionLabel.center();
                formPanel.addComponent(submissionLabel);

                //formPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

                // Ajouter un panneau pour les boutons "Open" et "Manage" côte à côte
                Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
                buttonPanel.setSize(new TerminalSize(40, 1));

                if (!form.getQuestions().isEmpty()) {
                    Button openButton = new Button("Open");
                    buttonPanel.addComponent(openButton);
                }

                if (hasEditorAccess(form, currentUser)){
                    Button manageButton = new Button("Manage");
                    buttonPanel.addComponent(manageButton);
                }
                buttonPanel.center();
                formPanel.addComponent(buttonPanel);
                formsPanel.addComponent(formPanel);

                formsPanel.addComponent(formPanel.withBorder(Borders.singleLine()));
            }

        }
        // Mettre à jour l'affichage du numéro de page
        int totalPages = (int) Math.ceil((double) forms.size() / formsPerPage);
        pageLabel.setText("Page " + (currentPage + 1) + " of " + totalPages);

        // Mettre à jour le composant principal
        this.setComponent(mainPanel);
    }


    public boolean hasEditorAccess(Form form, User user) {
        // Vérifier les accès de l'utilisateur individuel
        UserFormAccess userAccess = UserFormAccess.getByKey(form.getId(), user.getId());
        if (userAccess != null && userAccess.getAccessType() == AccessType.Editor) {
            return true;
        }
        // Vérifier les accès via les listes de distribution
        List<DistListFormAccess> distListAccesses = form.getDistListFormAccesses();
        for (DistListFormAccess access : distListAccesses) {
            if (access.getAccessType() == AccessType.Editor) {
                return true;
            }
        }

        // Si l'utilisateur est admin ou propriétaire, il a toujours accès en tant qu'éditeur
        return user.isAdmin() || form.getOwnerId() == user.getId();
    }

    private void openFileMenu() {
        Window fileMenuWindow = new BasicWindow("File Menu");
        Panel fileMenuPanel = new Panel();

        Button viewProfileButton = new Button("View Profile", () -> {
            // Appeler le contrôleur pour afficher le profil
            controller.showProfile();
            fileMenuWindow.close();  // Fermer la fenêtre après l'action
        });

        Button logoutButton = new Button("Logout", () -> {
            // Appeler le contrôleur pour gérer la déconnexion
            controller.logout();
            fileMenuWindow.close();  // Fermer la fenêtre après l'action
        });

        Button exitButton = new Button("Exit", () -> {
            // Appeler le contrôleur pour fermer l'application
            controller.exitApplication();
        });

        fileMenuPanel.addComponent(viewProfileButton);
        fileMenuPanel.addComponent(logoutButton);
        fileMenuPanel.addComponent(exitButton);

        fileMenuWindow.setComponent(fileMenuPanel);
        this.getTextGUI().addWindowAndWait(fileMenuWindow);  // Afficher la fenêtre contextuelle
    }

    // Méthode pour afficher une boîte de dialogue avec les informations du profil
    public void showProfileDialog(String fullName, String email) {
        MessageDialog.showMessageDialog(this.getTextGUI(), "Profile", "Full Name: " + fullName + "\nEmail: " + email);
    }
}