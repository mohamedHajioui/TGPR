
package tgpr.forms.view;


import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.forms.model.*;


import tgpr.forms.controller.EditInstanceController;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


public class EditInstanceView extends DialogWindow {
    private EditInstanceController controller;
    private Panel mainPanel;

    public EditInstanceView(EditInstanceController controller) {
        super("Open a form");
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
        ViewSubmission();
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
    private List<Object[]> answerList = new ArrayList<>();



    private void AnswerForm() {

        setViewTitle("Answer the form"); // Titre spécifique pour AnswerForm
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(55, 20));

        Form form = new Form();

        form.setId(1);

        List<Question> questions = form.getQuestions();

        List<Instance> value = form.getInstances();

        Form formData = Form.getByKey(1);
        System.out.println(formData);

        // Créer et ajouter les labels principaux (Title, Description, and Date)
        Label titleLabel = new Label("Title: " + (formData.getTitle() != null ? formData.getTitle() : "null"));
        Label descriptionLabel = new Label("Description: " + (formData.getDescription() != null ? formData.getDescription() : "null"));
        Label dateLabel = new Label("Submitted on: " + (value.getFirst().getStarted() != null ? value.getFirst().getStarted() : "null"));
        Label submitterLabel = new Label("Submitted by: " +  (value.getFirst().getCompleted() != null ? value.getFirst().getCompleted() : "null"));

        mainPanel.addComponent(titleLabel);
        mainPanel.addComponent(descriptionLabel);
        mainPanel.addComponent(dateLabel);
        mainPanel.addComponent(submitterLabel);


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
        addSpacingLabel(questionPanel, 1);

        Question question = questions.get(currentQuestionIndex);

        if (question == null) {
            System.out.println("Question object is null.");
            return;
        }

        // Add question number and title with optional required marker
        Label questionNumberLabel = new Label("Question " + (currentQuestionIndex + 1) + " of " + questions.size());
        questionPanel.addComponent(questionNumberLabel);
        addSpacingLabel(questionPanel, 1);

        addQuestionTitleWithRequiredMarker(question);

        addSpacingLabel(questionPanel, 1);
        renderQuestionInput(question);

        if (question.getRequired()) {
            Label requiredLabelText = new Label("Input Required (*)").setForegroundColor(TextColor.ANSI.RED);
            questionPanel.addComponent(requiredLabelText);
        }

        errorMessageLabel = new Label("").setForegroundColor(TextColor.ANSI.RED);
        questionPanel.addComponent(errorMessageLabel);

        // Refresh question panel display and update button panel visibility
        questionPanel.invalidate();
        updateButtonPanel(questions.size(), questions);
    }
    private void addSpacingLabel(Panel panel, int count) {
        for (int i = 0; i < count; i++) {
            panel.addComponent(new Label(""));
        }
    }
    private void addQuestionTitleWithRequiredMarker(Question question) {
        Panel titlePanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Label questionTitle = new Label(question.getTitle()).setForegroundColor(TextColor.ANSI.BLACK);
        titlePanel.addComponent(questionTitle);
        if (question.getRequired()) {
            Label requiredLabel = new Label(" (*)").setForegroundColor(TextColor.ANSI.RED);
            titlePanel.addComponent(requiredLabel);
        }
        questionPanel.addComponent(titlePanel);

    }
    private void renderQuestionInput(Question question) {
        String type = question.getType().toString().toLowerCase();
        OptionList options = new OptionList();

        // Fetch the answer for the current question, if it exists
        Object[] currentAnswer = null;
        if (currentQuestionIndex < answerList.size()) {
            currentAnswer = answerList.get(currentQuestionIndex);
        }

        // Clear previous input components to avoid carryover
        questionPanel.removeAllComponents(); // Clear the previous inputs

        switch (type) {
            case "short":
            case "email":
            case "date":
                TextBox textBox = addTextBoxInput(45);
                // If there's an answer, populate the TextBox
                if (currentAnswer != null && currentAnswer[2] instanceof String) {
                    textBox.setText((String) currentAnswer[2]);
                }
                break;
            case "long":
                TextBox longTextBox = addTextBoxInput(55);
                // If there's an answer, populate the TextBox
                if (currentAnswer != null && currentAnswer[2] instanceof String) {
                    longTextBox.setText((String) currentAnswer[2]);
                }
                break;
            case "combo":
                ComboBox<String> comboInput = addComboBoxInput(options, question.getOptionListId());
                // If there's an answer, set the selected item
                if (currentAnswer != null && currentAnswer[2] instanceof String) {
                    comboInput.setSelectedItem((String) currentAnswer[2]);
                }
                break;
            case "check":
                CheckBoxList<String> checkBoxList = addCheckBoxListInput(options, question.getOptionListId());
                // If there's an answer and it's a List, check the corresponding boxes
                if (currentAnswer != null && currentAnswer[2] instanceof List) {
                    List<?> responses = (List<?>) currentAnswer[2];
                    for (Object item : responses) {
                        checkBoxList.setChecked(item.toString(), true); // Check each item based on previous answers
                    }
                }
                break;
            case "radio":
                RadioBoxList<String> radioList = addRadioBoxListInput(options, question.getOptionListId());
                // If there's an answer, set the checked item
                if (currentAnswer != null && currentAnswer[2] instanceof String) {
                    radioList.setCheckedItem((String) currentAnswer[2]);
                }
                break;
            default:
                System.out.println("Unknown question type: " + type);
        }

        // Refresh the question panel display
        questionPanel.invalidate();
    }
        private TextBox addTextBoxInput(int width) {
            TextBox textBox = new TextBox(new TerminalSize(width, 1));
            questionPanel.addComponent(textBox);
            return textBox; // Return the created TextBox for further use
        }
    private ComboBox<String> addComboBoxInput(OptionList options, int optionListId) {
        options.setId(optionListId);
        ComboBox<String> comboInput = new ComboBox<>();

        private ComboBox<String> addComboBoxInput(OptionList options, int optionListId) {
            options.setId(optionListId);
            ComboBox<String> comboInput = new ComboBox<>();

            // Add a placeholder item that is not a valid option
            comboInput.addItem("Please select one"); // Placeholder item
            comboInput.setSelectedItem("Please select one"); // Set it as selected initially

            // Add actual option values from the OptionList
            for (OptionValue optionValue : options.getOptionValues()) {
                comboInput.addItem(optionValue.getLabel());
            }

            // Add ComboBox to the question panel
            questionPanel.addComponent(comboInput);
            return comboInput; // Return the created ComboBox for further use
        }


    }



}













    public Panel getMainPanel() {
        return mainPanel;
    }}