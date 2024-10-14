package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import tgpr.forms.controller.*;
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

    public ViewFormsView(ViewFormsController controller) {
        this.controller = controller;
        String email = Security.getLoggedUser().getEmail();

        setTitle("MyForms" + email);
        setHints(List.of(Hint.CENTERED, Hint.MODAL));
        setCloseWindowWithEscape(true);

        // Ajouter les boutons "File" et "Parameters" en haut à gauche
        Panel topPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Button fileButton = new Button("File");
        Button parametersButton = new Button("Parameters");
        topPanel.addComponent(fileButton);
        topPanel.addComponent(parametersButton);

        // Panneau central avec les boutons Open et Manage
        Panel centerPanel = new Panel(new GridLayout(1));
        centerPanel.addComponent(openButton);
        centerPanel.addComponent(manageButton);
        centerPanel.setPreferredSize(new TerminalSize(90, 30));

        // Panneau de navigation en bas
        Panel bottomPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        bottomPanel.addComponent(createNewFormButton);
        bottomPanel.addComponent(new EmptySpace(new TerminalSize(50, 1)));

        Panel navigationPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        navigationPanel.addComponent(firstButton);
        navigationPanel.addComponent(previousButton);

        // Texte de pagination entre Previous et Next
        Label pageLabel = new Label("Page 1 of 2 ");
        navigationPanel.addComponent(pageLabel);

        navigationPanel.addComponent(nextButton);
        navigationPanel.addComponent(lastButton);

        bottomPanel.addComponent(navigationPanel);

        // Panneau principal
        Panel mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        // Ajouter le panneau supérieur (topPanel) en premier
        mainPanel.addComponent(topPanel);  // Boutons "File" et "Parameters"

        // Ajouter les autres panneaux
        mainPanel.addComponent(centerPanel);  // Panneau central
        mainPanel.addComponent(bottomPanel);  // Panneau de navigation

        setComponent(mainPanel);
    }
}