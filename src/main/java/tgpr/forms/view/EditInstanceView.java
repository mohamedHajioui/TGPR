package tgpr.forms.view;

// View/EditInstanceView.java
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.gui2.Button;
import tgpr.forms.controller.EditInstanceController;

import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;


public class EditInstanceView {
    private Screen screen;
    private Panel mainPanel;

    public EditInstanceView(Screen screen) {
        this.screen = screen;
    }

    public void display(EditInstanceController controller) {
        // Crée la fenêtre
        Window window = new BasicWindow("Edit Instance");
        mainPanel = new Panel();
        mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        // Placeholder pour afficher la question actuelle
        mainPanel.addComponent(new Label("Formulaire en cours de création..."));

        // Ajouter les boutons de navigation et de soumission
        Button nextButton = new Button("Next", controller::nextQuestion);
        Button previousButton = new Button("Previous", controller::previousQuestion);
        Button submitButton = new Button("Submit", controller::submitInstance);

        mainPanel.addComponent(nextButton);
        mainPanel.addComponent(previousButton);
        mainPanel.addComponent(submitButton);

        window.setComponent(mainPanel);

        // Crée un text GUI pour gérer l'affichage de la fenêtre
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace());
        gui.addWindowAndWait(window);
    }



    public void showNextQuestion() {
        // Logique pour afficher la question suivante en utilisant l'index
    }

    public void showPreviousQuestion() {
        // Logique pour afficher la question précédente
    }



}

