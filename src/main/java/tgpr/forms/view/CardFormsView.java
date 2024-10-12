package tgpr.forms.view;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.TextColor;
import tgpr.forms.controller.ViewFormsController;
import tgpr.forms.model.Form;
import tgpr.forms.model.User;

public class CardFormsView extends Panel {

    public CardFormsView(Form form, User currentUser, ViewFormsController controller) {
        super(new GridLayout(1));

        // Titre avec couleur
        Label titleLabel = new Label(form.getTitle());
        titleLabel.setForegroundColor(TextColor.ANSI.BLUE);
        this.addComponent(titleLabel);

        // Description
        String description = form.getDescription().isEmpty() ? "No description" : form.getDescription();
        this.addComponent(new Label(description));

        // Créateur
        String creator = form.getOwner().getFullName();
        this.addComponent(new Label("Created by " + creator));

        // Statut de l'instance
        var instance = form.getMostRecentInstance(currentUser);
        String status;
        if (instance == null) {
            status = "Not Started";
        } else if (instance.isCompleted()) {
            status = "Submitted on " + instance.getCompleted();
        } else {
            status = "In Progress";
        }
        this.addComponent(new Label("Status: " + status));

        // Boutons Open et Manage
        Panel buttonPanel = new Panel(new GridLayout(2));

        // Bouton Open visible seulement si le formulaire a des questions
        if (!form.getQuestions().isEmpty()) {
            buttonPanel.addComponent(new Button("Open", () -> controller.onOpenForm(form)));
        }

        // Bouton Manage visible seulement si l'utilisateur a les droits d'édition
        if (form.hasEditAccess(currentUser)) {
            buttonPanel.addComponent(new Button("Manage", () -> controller.onManageForm(form)));
        }

        this.addComponent(buttonPanel);

        // Ajouter une bordure autour de la carte avec Borders.singleLine()
        Component borderedPanel = Borders.singleLine().addTo(this);
        this.addComponent(borderedPanel);
    }
}