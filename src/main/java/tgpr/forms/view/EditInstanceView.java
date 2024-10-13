package tgpr.forms.view;

// View/EditInstanceView.java
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.gui2.Button;
import tgpr.forms.controller.EditInstanceController;


public class EditInstanceView {
    private Screen screen;
    private Panel mainPanel;

    public EditInstanceView(Screen screen) {
        this.screen = screen;
    }


    public void display(EditInstanceController controller) {
        Window window = new BasicWindow("Edit Instance");
        mainPanel = new Panel();
        mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        // Affiche la première question (placeholder pour le moment)
        mainPanel.addComponent(new Label("Question 1"));

        // Ajouter les boutons
        Button nextButton = new Button("Next", controller::nextQuestion);
        Button previousButton = new Button("Previous", controller::previousQuestion);
        Button submitButton = new Button("Submit", controller::submitInstance);

        mainPanel.addComponent(nextButton);
        mainPanel.addComponent(previousButton);
        mainPanel.addComponent(submitButton);

        window.setComponent(mainPanel);
        screen.startScreen();
        screen.setWindow(window);
    }


    public void showNextQuestion() {
        // Logique pour afficher la question suivante en utilisant l'index
    }

    public void showPreviousQuestion() {
        // Logique pour afficher la question précédente
    }



}

