package tgpr.forms.controller;

// Controller/EditInstanceController.java
import java.time.LocalDateTime;

public class EditInstanceController {
    private FormInstance instance;
    private EditInstanceView view;

    public EditInstanceController(FormInstance instance, EditInstanceView view) {
        this.instance = instance;
        this.view = view;
    }

    public void start() {
        view.display();
    }

    public void submitInstance() {
        if (instance.isSubmitted()) {
            view.showError("Cette instance est déjà soumise.");
        } else {
            if (validateAnswers()) {
                instance.setCompleted(LocalDateTime.now());
                view.showMessage("Formulaire soumis avec succès !");
            } else {
                view.showError("Veuillez remplir toutes les questions obligatoires.");
            }
        }
    }

    private boolean validateAnswers() {
        // Implémente la logique de validation
        return true;
    }
}

