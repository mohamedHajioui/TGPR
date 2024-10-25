
package tgpr.forms.view;


import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.forms.model.*;


import tgpr.forms.controller.EditInstanceController;

import java.time.LocalDateTime;
import java.util.List;


public class EditInstanceView extends DialogWindow {
    private EditInstanceController controller;
    private Panel mainPanel;

    public EditInstanceView(EditInstanceController controller) {
        super("Answer a form");
        this.controller = controller;

        RequestConfirmation();
        //AnswerForm();
    }

    private void setViewTitle(String title) {
        setTitle(title); // Méthode pour changer le titre dynamiquement
    }


    private void RequestConfirmation() {

        setViewTitle("Open a form"); // Titre spécifique pour RequestConfirmation

        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(55, 5)); // Définir une taille préférée pour le panel

        Label textLabel = new Label("You Have already answered this form.\nYou can view your submission or submit again.\nWhat would you like to do?");
        mainPanel.addComponent(textLabel); // Ajouter le label au panel principal


        mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)), LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));


        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("view submission", this::buttonViewSubmission));
        buttonPanel.addComponent(new Button("submit again", this::SubmitAgain));
        buttonPanel.addComponent(new Button("cancel", this::close));
        setHints(List.of(Hint.CENTERED));

        buttonPanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        mainPanel.addComponent(buttonPanel);

        Panel container = new Panel(new LinearLayout(Direction.HORIZONTAL));
        container.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        container.addComponent(new EmptySpace(new TerminalSize(0, 1))); // Espace vide avant
        container.addComponent(mainPanel);
        container.addComponent(new EmptySpace(new TerminalSize(0, 1))); // Espace vide après

        // Définir le panneau de conteneur comme composant principal de la fenêtre
        setComponent(container);

    }

    private void buttonViewSubmission() {
    }

    private void SubmitAgain() {
        AnswerForm();
    }

    private void buttonCancel() {
    }

    private int currentQuestionIndex = 0; // Pour suivre l'indice de la question actuelle
    private Panel questionPanel; // Panel pour afficher la question actuelle
    private Button nextButton;
    private Button previousButton;
    private Panel buttonPanel; // Panel pour les boutons

    private void AnswerForm() {

        setViewTitle("Answer the form"); // Titre spécifique pour RequestConfirmation

        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(55, 30));

        Form form = new Form();

        form.setId(1);

        List<Question> questions = form.getQuestions();

        List<Instance> value = form.getInstances();

        Form formData = Form.getByKey(1);

        // Créer et ajouter les labels principaux (Title, Description, and Date)
        Label titleLabel = new Label("Title: " + formData.getTitle());
        Label descriptionLabel = new Label("Description: " + formData.getDescription());
        Label dateLabel = new Label("Started on: " + value.getFirst().getStarted());

        mainPanel.addComponent(titleLabel);
        mainPanel.addComponent(descriptionLabel);
        mainPanel.addComponent(dateLabel);

        // Créer un panneau pour afficher la question actuelle
        questionPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        questionPanel.setPreferredSize(new TerminalSize(55, 25)); // Taille du panneau

        Panel container = new Panel(new LinearLayout(Direction.HORIZONTAL));
        container.addComponent(new EmptySpace(new TerminalSize(20, 1))); // Espace vide avant
        container.addComponent(questionPanel);
        container.addComponent(new EmptySpace(new TerminalSize(3, 1))); // Espace vide aprè



        // Créer un panel pour organiser les boutons horizontalement
        buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL)); // Initialisation du buttonPanel

        // Ajouter les boutons "Previous", "Next", "Close", et "Cancel"
        createButtons(questions);


        mainPanel.addComponent(questionPanel);
        mainPanel.addComponent(buttonPanel);


        displayQuestion(questions);


        setComponent(mainPanel);
    }


    private void displayQuestion(List<Question> questions) {

        questionPanel.removeAllComponents();

        Label emptyLabel = new Label("");
        Label emptyLabel1 = new Label("");



        Label requiredLabelText = new Label("Input Required (*)")
                .setForegroundColor(TextColor.ANSI.RED);


        Question question = questions.get(currentQuestionIndex);

        questionPanel.addComponent(emptyLabel);

        Label questionNumberLabel = new Label("Question " + (currentQuestionIndex + 1) + " of " + questions.size());

        Panel titlePanel = new Panel(new LinearLayout(Direction.HORIZONTAL));

        Label questionTitle = new Label(question.getTitle())
                .setForegroundColor(TextColor.ANSI.BLACK);

        titlePanel.addComponent(questionTitle);

        if (question.getRequired()) {
            Label requiredLabel = new Label(" (*)")
                    .setForegroundColor(TextColor.ANSI.RED); // Astérisque en rouge

            // Ajouter l'astérisque rouge au panel horizontal
            titlePanel.addComponent(requiredLabel);
        }

        // Ajouter le panel horizontal avec le titre et l'astérisque au questionPanel
        questionPanel.addComponent(questionNumberLabel);
        questionPanel.addComponent(emptyLabel1);
        questionPanel.addComponent(titlePanel); // Ajouter le panel du titre

        questionPanel.addComponent(new Label(""));

        if (question != null) {  // Check if question is not null
            String type = String.valueOf(question.getType());  // This should work if getType() returns a String

            OptionList options = new OptionList();
            switch (type.toLowerCase()) {
                case "short":
                    TextBox shortInput = new TextBox(new TerminalSize(45, 1)); // Input pour réponse courte
                    questionPanel.addComponent(shortInput);
                    break;
                case "email":
                    TextBox email = new TextBox(new TerminalSize(45, 1)); // Input pour réponse courte
                    questionPanel.addComponent(email);
                    break;
                case "date":
                    TextBox date = new TextBox(new TerminalSize(45, 1)); // Input pour réponse courte
                    questionPanel.addComponent(date);
                    break;
                case "long":
                    TextBox longInput = new TextBox(new TerminalSize(55, 1)); // Input pour réponse courte
                    questionPanel.addComponent(longInput);
                    break;
                case "combo":

                    // Fetch the list of OptionValues using the getOptionValues method

                    options.setId(question.getOptionListId());
                    List<OptionValue> valueOptions = options.getOptionValues();

                    // Create the ComboBox and populate it with option values from the database
                    ComboBox<String> comboInput = new ComboBox<>();
                    for (OptionValue optionValue : valueOptions) {
                        comboInput.addItem(optionValue.getLabel());  // Assuming OptionValue has a getValue() method
                    }

                    // Add the ComboBox to the panel
                    questionPanel.addComponent(comboInput);
                    break;

                case "check":
                    // Fetch the list of OptionValues using the getOptionValues method
                    options.setId(question.getOptionListId());
                    List<OptionValue> checkboxOptions = options.getOptionValues();

                    // Create the CheckBoxList and populate it with option values from the database
                    CheckBoxList<String> checkboxList = new CheckBoxList<>(new TerminalSize(55, 10));
                    for (OptionValue optionValue : checkboxOptions) {
                        checkboxList.addItem(optionValue.getLabel());  // Assuming OptionValue has a getLabel() method
                    }

                    // Add the CheckBoxList to the panel
                    questionPanel.addComponent(checkboxList);
                    break;

                case "radio":
                    // Fetch the list of OptionValues using the getOptionValues method
                    options.setId(question.getOptionListId());
                    List<OptionValue> radioOptions = options.getOptionValues();

                    // Create the RadioBoxList and populate it with option values from the database
                    RadioBoxList<String> radioList = new RadioBoxList<>(new TerminalSize(55, 10));
                    for (OptionValue optionValue : radioOptions) {
                        radioList.addItem(optionValue.getLabel());  // Assuming OptionValue has a getLabel() method
                    }

                    // Add the RadioBoxList to the panel
                    questionPanel.addComponent(radioList);
                    break;

            }
        } else {
            System.out.println("Question object is null.");
        }



        if(question.getRequired()) {
            questionPanel.addComponent(requiredLabelText);
        }

        // Mettre à jour l'affichage du panneau des questions
        questionPanel.invalidate(); // Actualiser l'affichage des questions

        // Mettre à jour la visibilité des boutons
        updateButtonPanel(questions.size());
    }

    // Méthode pour créer les boutons et les ajouter au panel buttonPanel
    private void createButtons(List<Question> questions) {
        Button cancelButton = new Button("Cancel", () -> {
            System.out.println("Cancel clicked");
        });

        previousButton = new Button("Previous", () -> {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--; // Aller à la question précédente
                displayQuestion(questions); // Actualiser l'affichage
            }
        });

        nextButton = new Button("Next", () -> {
            if (currentQuestionIndex < questions.size() - 1) {
                currentQuestionIndex++; // Aller à la question suivante
                displayQuestion(questions); // Actualiser l'affichage
            }
        });

        Button closeButton = new Button("Close", () -> {
            System.out.println("Close clicked");
        });

        // Ajouter les boutons au panel buttonPanel
        buttonPanel.addComponent(closeButton);
        buttonPanel.addComponent(cancelButton);
    }

    // Méthode pour mettre à jour la visibilité des boutons "Next" et "Previous"
    private void updateButtonPanel(int totalQuestions) {
        // Supprimer tous les boutons actuels du buttonPanel sauf "Close" et "Cancel"
        buttonPanel.removeAllComponents();

        // Ajouter conditionnellement les boutons "Previous" et "Next"
        Button closeButton = new Button("Close", () -> {
            System.out.println("Close clicked");
        });
        Button cancelButton = new Button("Cancel", () -> {
            System.out.println("Cancel clicked");
        });

        buttonPanel.addComponent(closeButton); // Ajouter bouton "Close"
        buttonPanel.addComponent(cancelButton); // Ajouter bouton "Cancel"

        if (currentQuestionIndex > 0) {
            // Ajouter "Previous" si ce n'est pas la première question
            buttonPanel.addComponent(previousButton);
        }

        if (currentQuestionIndex < totalQuestions - 1) {
            // Ajouter "Next" si ce n'est pas la dernière question
            buttonPanel.addComponent(nextButton);
        }

        // Actualiser l'affichage du panneau des boutons
        buttonPanel.invalidate(); // Rafraîchir l'affichage des boutons
    }

    public Panel getMainPanel() {
        return mainPanel;
    }
}