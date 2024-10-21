package tgpr.forms.view;


import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;


import com.sun.net.httpserver.Request;
import tgpr.forms.controller.EditInstanceController;

import java.util.List;


public class EditInstanceView extends DialogWindow {
    private EditInstanceController controller;
    private Panel mainPanel;

    public EditInstanceView(EditInstanceController controller) {
        super("Open a form");
        this.controller = controller;

        RequestConfirmation();
    }

    private void RequestConfirmation() {

        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(50, 5)); // Définir une taille préférée pour le panel

        Label textLabel = new Label("You Have already answered this form.\n You can view your submission or submit again.\n What would you like to do?");
        mainPanel.addComponent(textLabel); // Ajouter le label au panel principal


        mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)), LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));


        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("view submission", this::buttonViewSubmission));
        buttonPanel.addComponent(new Button("submit again", this::SubmitAgain));
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

    private void buttonViewSubmission() {
    }

    private void SubmitAgain() {
    }

    private void buttonCancel() {
    }

    private void AnswerForm(){

    }


    public Panel getMainPanel() {
        return mainPanel;
    }
}
