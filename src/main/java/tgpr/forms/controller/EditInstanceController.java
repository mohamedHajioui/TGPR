package tgpr.forms.controller;

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

}
