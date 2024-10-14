package tgpr.forms.controller;

// Controller/EditInstanceController.java
import tgpr.forms.model.Instance;
import tgpr.forms.model.Question;
import tgpr.forms.model.User;
import tgpr.forms.view.EditInstanceView;
import java.time.LocalDateTime;
import java.util.List;

public class EditInstanceController {
    private Instance instance;
    private EditInstanceView view;
    private User user; // Utilisation potentielle de la classe User si elle est pertinente

    public EditInstanceController(Instance instance, EditInstanceView view) {
        this.instance = instance;
        this.view = view;
    }

    public void start() {
        view.display(this);
    }

    public void nextQuestion() {
        view.showNextQuestion();
    }

    public void previousQuestion() {
        view.showPreviousQuestion();
    }

    public void submitInstance() {
        if (isInstanceSubmitted()) {
            view.showError("Cette instance est déjà soumise.");
        } else {
            if (validateAnswers()) {
                setInstanceCompleted(LocalDateTime.now());
                view.showMessage("Formulaire soumis avec succès !");
            } else {
                view.showError("Veuillez remplir toutes les questions obligatoires.");
            }
        }
    }

    private boolean validateAnswers() {
        List<Question> questions = instance.getForm().getQuestions(); // Récupérer les questions du formulaire
        for (Question question : questions) {
            if (question.isRequired() && !question.isAnswered()) { // Vérifie si chaque question requise est répondue
                return false; // Retourne faux si une question requise n'a pas été répondue
            }
        }
        return true; // Toutes les validations passent
    }


    private boolean isInstanceSubmitted() {
        return instance.getCompleted() != null; // Suppose que l'Instance a un attribut 'completed'
    }

    private void setInstanceCompleted(LocalDateTime completed) {
        instance.setCompleted(completed); // Suppose que l'Instance a cette méthode
    }
}
