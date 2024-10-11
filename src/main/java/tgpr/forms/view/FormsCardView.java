package tgpr.forms.view;

import com.googlecode.lanterna.gui2.*;
import tgpr.forms.controller.FormCardController;

public class FormsCardView {
    private final Panel panel;
    private final FormCardController controller;

    public FormsCardView(FormCardController controller) {
        this.controller = controller;
        this.panel = new Panel();

        // Création des composants Lanterna pour afficher les détails du formulaire
        Label titleLabel = new Label("Title: " + controller.getTitle());
        Label descriptionLabel = new Label("Description: " + controller.getDescription());
        Label creatorLabel = new Label("Creator: " + controller.getCreator());
       /* Label startDateLabel = new Label("Started: " + (controller.getStartDate() != null ? controller.getStartDate().toString() : "Not Started"));
        Label submissionDateLabel = new Label("Submitted: " + (controller.getSubmissionDate() != null ? controller.getSubmissionDate().toString() : "In Progress"));
*/
        // Boutons "Open" et "Manage"
        Button openButton = new Button("Open", controller::onOpen);
        Button manageButton = new Button("Manage", controller::onManage);

        if (!controller.hasQuestions()) {
            openButton.setEnabled(false);
        }

        if (!controller.canManage()) {
            manageButton.setVisible(false);
        }

        // Ajout des composants au panel
        panel.addComponent(titleLabel);
        panel.addComponent(descriptionLabel);
        panel.addComponent(creatorLabel);
        /*
        panel.addComponent(startDateLabel);
        panel.addComponent(submissionDateLabel);
        */
        panel.addComponent(openButton);
        panel.addComponent(manageButton);
    }

    public Panel getPanel() {
        return panel;
    }
}
