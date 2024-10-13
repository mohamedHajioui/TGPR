package tgpr.forms.view;

// View/EditInstanceView.java
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;

public class EditInstanceView {
    private Screen screen;
    private Panel mainPanel;

    public EditInstanceView(Screen screen) {
        this.screen = screen;
    }

    public void display() {
        Window window = new BasicWindow("Edit Instance");
        mainPanel = new Panel();
        mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        // Placeholder pour les composants de la vue
        mainPanel.addComponent(new Label("Formulaire en cours de création..."));

        window.setComponent(mainPanel);
        screen.startScreen();
        screen.setWindow(window);
    }
}

