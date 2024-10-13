package tgpr.forms.controller;

import java.time.LocalDateTime;

public class EditInstanceController {
    private FormInstance instance;
    private EditInstanceView view;

    public EditInstanceController(FormInstance instance, EditInstanceView view) {
        this.instance = instance;
        this.view = view;
    }

    public void start() {
        view.display(); // Affiche la vue
    }

    public void nextQuestion() {
        // Logique pour passer à la question suivante
        view.showNextQuestion();
    }

    public void previousQuestion() {
        // Logique pour revenir à la question précédente
        view.showPreviousQuestion();
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
        // Validation des réponses en utilisant les méthodes du modèle
        return true;
    }


}
