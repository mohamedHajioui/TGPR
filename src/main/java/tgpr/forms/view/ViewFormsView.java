package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import tgpr.forms.controller.ViewFormsController;
import tgpr.forms.model.Form;
import tgpr.forms.model.Security;
import tgpr.forms.model.User;

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

    // Déclaration du mainPanel et du formsPanel
    private Panel mainPanel;
    private Panel formsPanel;

    public ViewFormsView(ViewFormsController controller) {
        this.controller = controller;
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

        // Panneau central avec les boutons Open et Manage
        Panel centerPanel = new Panel(new GridLayout(2));
        centerPanel.addComponent(openButton);
        centerPanel.addComponent(manageButton);
        centerPanel.setPreferredSize(new TerminalSize(110, 15));  // Limiter la hauteur du panneau central

        // Panneau de navigation en bas
        Panel bottomPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        bottomPanel.addComponent(createNewFormButton);
        bottomPanel.addComponent(new EmptySpace(new TerminalSize(40, 1)));

        Panel navigationPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        navigationPanel.addComponent(firstButton);
        navigationPanel.addComponent(previousButton);

        // Texte de pagination entre Previous et Next
        Label pageLabel = new Label("Page 1 of 2");
        navigationPanel.addComponent(pageLabel);

        navigationPanel.addComponent(nextButton);
        navigationPanel.addComponent(lastButton);

        bottomPanel.addComponent(navigationPanel);

        // Initialisation du formsPanel avec une taille préférée
        formsPanel = new Panel(new GridLayout(1));
        formsPanel.setPreferredSize(new TerminalSize(50, 10));  // Définir une taille plus petite pour les formulaires

        // Ajouter les panneaux à mainPanel
        mainPanel.addComponent(topPanel);       // Boutons "File" et "Parameters"
        mainPanel.addComponent(centerPanel);    // Panneau central (Open/Manage)
        mainPanel.addComponent(formsPanel);     // Les formulaires seront affichés ici
        mainPanel.addComponent(bottomPanel);    // Panneau de navigation avec "Create a new form"

        setComponent(mainPanel);
    }

    // Méthode pour afficher les formulaires
    public void displayForms(List<Form> forms) {
        formsPanel.removeAllComponents();  // Supprimer les anciens composants s'il y en a

        // Ajouter chaque formulaire sous forme de label dans formsPanel
        for (Form form : forms) {
            formsPanel.addComponent(new Label(form.getTitle()));  // Affiche le titre de chaque formulaire
            formsPanel.addComponent(new Label(form.getDescription()));
            //formsPanel.addComponent(new Label(form.getDist);
        }

        // Mettre à jour le composant principal
        this.setComponent(mainPanel);
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
