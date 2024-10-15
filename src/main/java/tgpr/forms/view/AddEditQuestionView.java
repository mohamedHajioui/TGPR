package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import tgpr.forms.controller.AddEditQuestionController;
import tgpr.forms.model.Question;

import java.util.List;

public class AddEditQuestionView extends BasicWindow {
    private final TextBox txtTitle;
    private final TextBox txtDescription;
    private final AddEditQuestionController controller;
    private final Label titleValidationLabel;
    private final Label descriptionValidationLabel;

    public AddEditQuestionView(AddEditQuestionController controller) {
        this.controller = controller;
        setTitle("Add/Edit Question");
        setHints(List.of(Hint.CENTERED, Hint.EXPANDED)); // Centrer la fenêtre

        // Panneau principal
        Panel content = new Panel().setLayoutManager(new GridLayout(2).setHorizontalSpacing(2).setVerticalSpacing(1)); // Layout en grille avec espacement

        // Titre
        content.addComponent(new Label("Title:"));
        txtTitle = new TextBox();
        content.addComponent(txtTitle);
        titleValidationLabel = new Label(""); // Label pour validation du titre
        content.addComponent(titleValidationLabel);

        // Description
        content.addComponent(new Label("Description:")); // Label de description
        txtDescription = new TextBox(new TerminalSize(20, 5)); // Taille ajustée pour le TextBox de description
        content.addComponent(txtDescription);
        descriptionValidationLabel = new Label(""); // Label pour validation de la description
        content.addComponent(descriptionValidationLabel);

        // Boutons
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        content.addComponent(saveButton);
        content.addComponent(cancelButton);

        // Ajoutez le panneau à la fenêtre
        setComponent(content);


    }
}