package tgpr.forms.view;

// View/EditInstanceView.java
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;

public class EditInstanceView {
    private Screen screen;

    public EditInstanceView(Screen screen) {
        this.screen = screen;
    }

    public void display() {
        // Initialisation de la fenêtre
        Window window = new BasicWindow("Edit Instance");
        Panel mainPanel = new Panel();
        mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        // Placeholder pour les composants (questions, boutons, etc.)
        mainPanel.addComponent(new Label("Formulaire en cours de création..."));

        // Ajouter les boutons de navigation
        mainPanel.addComponent(new Button("Next"));
        mainPanel.addComponent(new Button("Previous"));
        mainPanel.addComponent(new Button("Submit"));

        window.setComponent(mainPanel);
        screen.startScreen();
        screen.setWindow(window);
    }
}

