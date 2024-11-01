package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.forms.controller.EditInstanceController;
import tgpr.forms.controller.ViewInstancesController;

import java.util.List;

public class ViewInstancesView extends DialogWindow {
    private ViewInstancesController controller;
    private Panel mainPanel;

    public ViewInstancesView(ViewInstancesController controller) {
        super("Titre par defaut");
        this.controller = controller;
        ListInstancesSubmitted();


    }
    private void setViewTitle(String title) {
        setTitle(title); // Méthode pour changer le titre dynamiquement
    }
    private void ListInstancesSubmitted(){

        setViewTitle("List of Submitted Instances"); // Titre spécifique pour RequestConfirmation
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(60, 20)); // Définir une taille préférée pour le panel



        // Créer et ajouter les labels principaux (Title, Description, and Date)
        Label titleLabel = new Label("Title: " );
        Label descriptionLabel = new Label("Description: ");

        mainPanel.addComponent(titleLabel);
        mainPanel.addComponent(descriptionLabel);


        mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)), LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));


        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("Delete Selected", this::ButtonDeleteSelected));
        buttonPanel.addComponent(new Button("Delete All", this::ButtonDeleteAll));
        buttonPanel.addComponent(new Button("Close", this::close));
        setHints(List.of(Hint.CENTERED));

        buttonPanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        mainPanel.addComponent(buttonPanel);

        Panel container = new Panel(new LinearLayout(Direction.HORIZONTAL));
        container.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        container.addComponent(new EmptySpace(new TerminalSize(0, 1))); // Espace vide avant
        container.addComponent(mainPanel);
        container.addComponent(new EmptySpace(new TerminalSize(0, 1))); // Espace vide après

        // Définir le panneau de conteneur comme composant principal de la fenêtre
        setComponent(container);

    }
    private void ButtonDeleteSelected() {

    }
    private void ButtonDeleteAll() {

    }





}
