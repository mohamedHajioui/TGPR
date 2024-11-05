package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import jdk.jfr.Description;
import tgpr.forms.controller.ViewInstancesController;
import tgpr.forms.model.OptionList;
import tgpr.framework.ViewManager;

import java.awt.event.ActionEvent;
import java.util.List;


public class ViewInstanceView extends DialogWindow {
    private ViewInstancesController controller;
    private Panel mainPanel;

    public ViewInstanceView(ViewInstancesController controller) {
        super("List of Submitted Instances");
        final ObjectTable<OptionList> table;

        this.controller = controller;


        ListSubmittedInstances();
    }


    private void setViewTitle(String title) {
        setTitle(title); // Méthode pour changer le titre dynamiquement
    }
    private void ListSubmittedInstances() {

        setViewTitle("List of Submitted Instances"); // Titre spécifique pour RequestConfirmation
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(55, 20)); // Définir une taille préférée pour le panel

         // Créer et ajouter les labels principaux (Title, Description)
        Label titleLabel = new Label("Title : " );
        Label descriptionLabel = new Label("Description : " );

        mainPanel.addComponent(titleLabel);
        mainPanel.addComponent(descriptionLabel);




        mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)), LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("Delete Selected", this::buttonDeleteSelected));
        buttonPanel.addComponent(new Button("Delete All", this::buttonDeleteAll));
        buttonPanel.addComponent(new Button("cancel", this::close));
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
    private void buttonDeleteSelected() {
       ConfirmationDeleteSelected();
    }
    private void buttonDeleteAll() {
       ConfirmationDeleteallInstances();
    }

    private void ConfirmationDeleteSelected() {
        setViewTitle("Delete Instance");
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(45, 3)); // Définir une taille préférée pour le panel

        Label textLabel = new Label("Are you sure you want to delete this instance ?  ");
        mainPanel.addComponent(textLabel); // Ajouter le label au panel principal


        mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)), LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));


        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("Yes", this::ButtonYes));
        buttonPanel.addComponent(new Button("No", this::ButtonNo));

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
    private void ButtonYes() {
    }
    private void ButtonNo() {
        close();
    }
    private void ConfirmationDeleteallInstances() {
        setViewTitle("Delete Instance");
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(85, 4)); // Définir une taille préférée pour le panel

        Label textLabel = new Label("Are you sure you want to delete all the submitted instance ?.\nNote : This will not delete instances that are currently being edited (not submitted)");
        mainPanel.addComponent(textLabel); // Ajouter le label au panel principal


        mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)), LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));


        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("Yes", this::ButtonAllYes));
        buttonPanel.addComponent(new Button("No", this::ButtonAllNo));

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
    private void ButtonAllYes() {
    }
    private void ButtonAllNo() {
        close();
    }

    private void ConfirmationDeleteallInstancesSubmitted() {
        setViewTitle("Delete Instance");
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(85, 4)); // Définir une taille préférée pour le panel

        Label textLabel = new Label("Are you sure you want to delete all the submitted instance ?.\nNote : This will delete also instances that are currently being edited (not submitted) .");
        mainPanel.addComponent(textLabel); // Ajouter le label au panel principal


        mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)), LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));


        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("Yes", this::ButtonAllSubYes));
        buttonPanel.addComponent(new Button("No", this::ButtonAllSubNo));

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
    private void ButtonAllSubYes() {
    }
    private void ButtonAllSubNo() {
        close();
    }




    public Panel getMainPanel() {
        return mainPanel;
    }
}
