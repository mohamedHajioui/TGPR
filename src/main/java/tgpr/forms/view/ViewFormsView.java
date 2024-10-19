package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import tgpr.forms.controller.ViewFormsController;
import tgpr.forms.model.Form;
import tgpr.forms.model.Security;
import tgpr.forms.model.User;

import java.util.List;

public class ViewFormsView extends BasicWindow {
    private final Button createNewFormButton = new Button("Create a new form");
    private final Button firstButton = new Button("First");
    private final Button previousButton = new Button("Previous");
    private final Button nextButton = new Button("Next");
    private final Button lastButton = new Button("Last");
    private final ViewFormsController controller;
    private final TextBox filterTextBox = new TextBox();
    private Label pageLabel;
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
        Panel topPanel = buttonsFileAndParameters();
        Panel centerPanel = new Panel(new GridLayout(2));
        centerPanel.setPreferredSize(new TerminalSize(110, 2));
        //zone de texte pour le filtre
        Panel filterPanel = zoneFilter(controller);
        //bouton CreateNewForm
        Panel bottomPanel = createNewForm();
        //pagination
        Panel navigationPanel = pagination(controller);
        bottomPanel.addComponent(navigationPanel);
        // Initialisation du formsPanel avec une taille préférée
        formsPanel = new Panel(new GridLayout(3));
        formsPanel.setPreferredSize(new TerminalSize(100, 28));
        // Ajouter les panneaux à mainPanel
        addComponents(topPanel, filterPanel, bottomPanel);

        setComponent(mainPanel);
    }

    private void addComponents(Panel topPanel, Panel filterPanel, Panel bottomPanel) {
        mainPanel.addComponent(topPanel);       // Boutons "File" et "Parameters"
        mainPanel.addComponent(filterPanel);    // Zone de filtre
        mainPanel.addComponent(formsPanel);     // Les formulaires seront affichés ici
        mainPanel.addComponent(bottomPanel);    // Panneau de navigation avec "Create a new form"
    }

    private Panel createNewForm() {
        Panel bottomPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        bottomPanel.addComponent(createNewFormButton);
        bottomPanel.addComponent(new EmptySpace(new TerminalSize(40, 1)));
        return bottomPanel;
    }

    private Panel zoneFilter(ViewFormsController controller) {
        Panel filterPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        filterPanel.addComponent(new Label("Filter:"));
        filterTextBox.setPreferredSize(new TerminalSize(30, 1));
        filterTextBox.setTextChangeListener(((newText, changedByUser) -> controller.filterForms(newText)));
        filterPanel.addComponent(filterTextBox);
        return filterPanel;
    }

    private Panel pagination(ViewFormsController controller) {
        Panel navigationPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        navigationPanel.addComponent(firstButton);
        firstButton.addListener(button -> controller.goToFirstPage());
        navigationPanel.addComponent(previousButton);
        previousButton.addListener(button -> controller.goToPreviousPage());
        pageLabel = new Label("Page 1 of 1");
        navigationPanel.addComponent(pageLabel);
        navigationPanel.addComponent(nextButton);
        nextButton.addListener(button -> controller.goToNextPage());
        navigationPanel.addComponent(lastButton);
        lastButton.addListener(button -> controller.goToLastPage());
        return navigationPanel;
    }

    private Panel buttonsFileAndParameters() {
        Panel topPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Button fileButton = new Button("File");
        fileButton.addListener(button -> openFileMenu());
        Button parametersButton = new Button("Parameters");
        topPanel.addComponent(fileButton);
        topPanel.addComponent(parametersButton);
        return topPanel;
    }

    // Méthode pour afficher les formulaires

    public void displayForms(List<Form> forms, int currentPage, int formsPerPage) {
        formsPanel.removeAllComponents();  // Supprimer les anciens composants
        GridLayout gridLayout = new GridLayout(3);
        gridLayout.setVerticalSpacing(1);
        formsPanel.setLayoutManager(gridLayout);

        // Calculer les formulaires à afficher en fonction de la page actuelle
        int start = currentPage * formsPerPage;
        int end = Math.min(start + formsPerPage, forms.size());

        for (int i = start; i < end; i++) {
            Form form = forms.get(i);
            if (form != null) {
                Panel formPanel = setTitle(form);
                setDescription(form, formPanel);
                Label created = new Label("Created by " + currentUser.getName());
                created.center();
                formPanel.addComponent(created);
                buttonsOpenAndManage(formPanel);
                formsPanel.addComponent(formPanel);
            }
        }
        // Mettre à jour l'affichage du numéro de page
        int totalPages = (int) Math.ceil((double) forms.size() / formsPerPage);
        pageLabel.setText("Page " + (currentPage + 1) + " of " + totalPages);

        this.setComponent(mainPanel);
    }

    private static void buttonsOpenAndManage(Panel formPanel) {
        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("Open"));
        buttonPanel.addComponent(new Button("Manage"));
        buttonPanel.center();
        formPanel.addComponent(buttonPanel);
    }

    private static void setDescription(Form form, Panel formPanel) {
        Label description = new Label(form.getDescription() != null ? form.getDescription() : "No description");
        description.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        description.center();
        formPanel.addComponent(description);
    }

    private static Panel setTitle(Form form) {
        Panel formPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        formPanel.setPreferredSize(new TerminalSize(60, 10));
        Label labelTitle = new Label(form.getTitle());
        labelTitle.setForegroundColor(TextColor.ANSI.BLUE_BRIGHT);
        labelTitle.center();
        formPanel.addComponent(labelTitle);
        return formPanel;
    }


    private void openFileMenu() {
        Window fileMenuWindow = new BasicWindow("File Menu");
        Panel fileMenuPanel = new Panel();

        Button viewProfileButton = new Button("View Profile", () -> {
            controller.showProfile();
            fileMenuWindow.close();
        });

        Button logoutButton = new Button("Logout", () -> {
            controller.logout();
            fileMenuWindow.close();
        });

        Button exitButton = new Button("Exit", () -> {
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
