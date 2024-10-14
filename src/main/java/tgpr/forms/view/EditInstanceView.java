package tgpr.forms.view;

// View/EditInstanceView.java
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import tgpr.forms.model.Question;

import com.googlecode.lanterna.gui2.Button;
import tgpr.forms.controller.EditInstanceController;

import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.List;


import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;

public class EditInstanceView {
    private Screen screen;
    private Panel mainPanel;
    private int currentQuestionIndex = 0; // Index de la question actuelle
    private List<Question> questions; // Liste des questions à afficher

    public EditInstanceView(Screen screen, List<Question> questions) {
        this.screen = screen;
        this.questions = questions; // Initialise la liste des questions
    }

    public void display(EditInstanceController controller) {
        // Crée la fenêtre
        Window window = new BasicWindow("Edit Instance");
        mainPanel = new Panel();
        mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        // Affiche la question actuelle
        showCurrentQuestion();

        // Ajouter les boutons de navigation et de soumission
        Button nextButton = new Button("Next", () -> {
            controller.nextQuestion(); // Appel à la méthode du contrôleur
            showNextQuestion(); // Afficher la prochaine question
        });
        Button previousButton = new Button("Previous", () -> {
            controller.previousQuestion(); // Appel à la méthode du contrôleur
            showPreviousQuestion(); // Afficher la question précédente
        });
        Button submitButton = new Button("Submit", controller::submitInstance);

        mainPanel.addComponent(previousButton);
        mainPanel.addComponent(nextButton);
        mainPanel.addComponent(submitButton);

        window.setComponent(mainPanel);
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);
        gui.addWindowAndWait(window);
    }

    private void showCurrentQuestion() {
         mainPanel.removeAllComponents(); // Efface les composants existants

        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            mainPanel.addComponent(new Label(currentQuestion.getTitle())); // Affiche le titre de la question
            // Ajouter la logique pour afficher la réponse ici, selon le type de question.
        }

        // Réinitialise le GUI en recréant la fenêtre
        Window window = new BasicWindow("Edit Instance");
        window.setComponent(mainPanel);
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);
        gui.addWindowAndWait(window); // Met à jour l'affichage
    }

    public void showNextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++; // Avance à la question suivante
            showCurrentQuestion(); // Met à jour l'affichage
        }
    }

    public void showPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--; // Retourne à la question précédente
            showCurrentQuestion(); // Met à jour l'affichage
        }
    }

    public void showError(String message) {
        MessageDialog.showMessageDialog(
                new MultiWindowTextGUI(screen),
                "Erreur",
                message
        );
    }

    public void showMessage(String message) {
        MessageDialog.showMessageDialog(
                new MultiWindowTextGUI(screen),
                "Information",
                message
        );
    }
}