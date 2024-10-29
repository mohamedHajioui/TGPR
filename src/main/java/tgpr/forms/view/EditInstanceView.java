package tgpr.forms.view;


import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import tgpr.forms.model.*;


import tgpr.forms.controller.EditInstanceController;

import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static java.awt.SystemColor.window;


public class EditInstanceView extends DialogWindow {
    private EditInstanceController controller;
    private Panel mainPanel;

    public EditInstanceView(EditInstanceController controller) {
        super("Open a form");
        this.controller = controller;
        setCloseWindowWithEscape(true);
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



    private int currentQuestionIndex = 0; // Pour suivre l'indice de la question actuelle
    private Panel questionPanel; // Panel pour afficher la question actuelle
    private Button nextButton;
    private Button previousButton;
    private Panel buttonPanel; // Panel pour les boutons
    private List<Object[]> answerList = new ArrayList<>();
    private int idForm = 1;
    private int userId = 1;
    private int instanceID;
    private LocalDateTime started ;
    private LocalDateTime complited = null;
    Instance latestInstanceByForm;


    private void SubmitAgain() {
        started = LocalDateTime.now();
        List<Instance> instance = Instance.getAll();
        instanceID = getMaxInstanceIdInstanceTable(instance);
        instanceID = instanceID + 1;
        AnswerForm();
    }

    private void buttonCancel() {
    }

    public Instance getLatestInstance(List<Instance> instances) {
        return instances.stream()
                .filter(instance -> instance.getFormId() == this.idForm) // Assuming 'this.id' is the desired formId
                .max(Comparator.comparing(Instance::getStarted)) // Get the latest instance based on 'started' time
                .orElse(null); // Return null if no instance is found
    }

    public Instance getFirstInstance(List<Instance> instances) {
        return instances.stream()
                .filter(instance -> instance.getFormId() == this.idForm) // Filtrer par idForm
                .findFirst() // Obtenir la première instance trouvée
                .orElse(null); // Retourner null si aucune instance n'est trouvée
    }



    private void AnswerForm() {


        setViewTitle("Answer the form"); // Titre spécifique pour AnswerForm
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(55, 20));

        Form form = new Form();

        form.setId(idForm);

        List<Question> questions = form.getQuestions();

        List<Instance> value = form.getInstances();

        latestInstanceByForm = getLatestInstance(value);

        Form formData = Form.getByKey(idForm);

        // Créer et ajouter les labels principaux (Title, Description, and Date)
        Label titleLabel = new Label("Title: " + (formData.getTitle() != null ? formData.getTitle() : "null"));
        Label descriptionLabel = new Label("Description: " + (formData.getDescription() != null ? formData.getDescription() : "null"));
        Label dateLabel = new Label("Submitted on: " + (latestInstanceByForm.getStarted() != null ? latestInstanceByForm.getStarted() : "null"));

        mainPanel.addComponent(titleLabel);
        mainPanel.addComponent(descriptionLabel);
        mainPanel.addComponent(dateLabel);


        questionPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        questionPanel.setPreferredSize(new TerminalSize(55, 25)); // Taille du panneau

        Panel container = new Panel(new LinearLayout(Direction.HORIZONTAL));
        container.addComponent(new EmptySpace(new TerminalSize(20, 1))); // Espace vide avant
        container.addComponent(questionPanel);
        container.addComponent(new EmptySpace(new TerminalSize(3, 1))); // Espace vide aprè

        buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL)); // Initialisation du buttonPanel

        createButtons(questions);

        mainPanel.addComponent(questionPanel);
        mainPanel.addComponent(buttonPanel);


        if(latestInstanceByForm.getCompleted()==null){
            displayQuestion(questions, false);
        }else{
            displayQuestion(questions, true);
        }



        setComponent(mainPanel);
    }

    private void renderQuestionInput(Question question) {
        String type = question.getType().toString().toLowerCase();
        OptionList options = new OptionList();

        // Fetch the answer for the current question, if it exists
        Object[] currentAnswer = null;
        if (currentQuestionIndex < answerList.size()) {
            currentAnswer = answerList.get(currentQuestionIndex);
        }

        // Based on the question type, add the appropriate input component
        switch (type) {
            case "short":
            case "email":
            case "date":
                TextBox textBox = addTextBoxInput(45);
                // Only populate TextBox if a valid answer exists
                if (currentAnswer != null && currentAnswer[2] instanceof String ) {
                    textBox.setText((String) currentAnswer[2]);
                } else {
                    textBox.setText(""); // Set empty if no answer exists
                }
                break;
            case "long":
                TextBox longTextBox = addTextBoxInput(55);
                if (currentAnswer != null && currentAnswer[2] instanceof String) {
                    longTextBox.setText((String) currentAnswer[2]);
                } else {
                    longTextBox.setText(""); // Set empty if no answer exists
                }
                break;
            case "combo":
                ComboBox<String> comboInput = addComboBoxInput(options, question.getOptionListId());
                if (currentAnswer != null && currentAnswer[2] instanceof String) {
                    comboInput.setSelectedItem((String) currentAnswer[2]);
                } else {
                    comboInput.setSelectedItem("Please select one"); // Set placeholder if no answer exists
                }
                break;
            case "check":
                CheckBoxList<String> checkBoxList = addCheckBoxListInput(options, question.getOptionListId());
                if (currentAnswer != null && currentAnswer[2] instanceof List) {
                    List<?> responses = (List<?>) currentAnswer[2];
                    for (Object item : responses) {
                        checkBoxList.setChecked(item.toString(), true);
                    }
                } else {
                    // If no answer exists, ensure all checkboxes are unchecked
                    checkBoxList.clearItems();
                    for (OptionValue optionValue : options.getOptionValues()) {
                        checkBoxList.addItem(optionValue.getLabel());
                    }
                }
                break;
            case "radio":
                RadioBoxList<String> radioList = addRadioBoxListInput(options, question.getOptionListId());
                if (currentAnswer != null && currentAnswer[2] instanceof String) {
                    radioList.setCheckedItem((String) currentAnswer[2]);
                } else {
                    radioList.clearSelection(); // Clear selection if no answer exists
                }
                break;
            default:
                System.out.println("Unknown question type: " + type);
        }

        // Refresh the question panel display
        questionPanel.invalidate();
    }

    public void listDistinctAnswersByInstanceId(List<Answer> answers, int instanceId) {
        // Réinitialiser la liste answerList
        answerList.clear();

        // Filtrer les réponses par instanceId
        List<Answer> filteredAnswers = answers.stream()
                .filter(answer -> answer.getInstanceId() == instanceId) // Filtre par instanceId
                .collect(Collectors.toList()); // Collecte les réponses filtrées

        // Vérifier si des réponses sont trouvées
        if (filteredAnswers.isEmpty()) {
            System.out.println("Aucune réponse trouvée pour l'instance ID " + instanceId + ".");
            return; // Sortir de la méthode si aucune réponse
        }

        // Trier les réponses filtrées par questionId
        List<Answer> sortedAnswers = filteredAnswers.stream()
                .sorted(Comparator.comparingInt(Answer::getQuestionId)) // Tri par questionId
                .collect(Collectors.toList());

        // Mettre les réponses triées dans answerList
        for (Answer answer : sortedAnswers) {
            Object[] newEntry = new Object[3];
            newEntry[0] = answer.getInstanceId(); // Instance ID
            newEntry[1] = answer.getQuestionId(); // Question ID
            newEntry[2] = answer.getValue();       // Response
            answerList.add(newEntry); // Ajouter à la liste
        }

        // Afficher la liste triée des réponses
        System.out.println("Réponses distinctes pour l'instance ID " + instanceId + ":");
        for (Object[] entry : answerList) {
            System.out.println("Instance ID: " + entry[0] + ", Question ID: " + entry[1] + ", Value: " + entry[2]);
        }
    }

    public int countDistinctAnswersByInstanceId(List<Answer> answers, int instanceId) {
        // Filtrer les réponses par instanceId
        List<Answer> filteredAnswers = answers.stream()
                .filter(answer -> answer.getInstanceId() == instanceId) // Filtre par instanceId
                .collect(Collectors.toList()); // Collecte les réponses filtrées

        // Compter le nombre de réponses distinctes par questionId
        long distinctCount = filteredAnswers.stream()
                .map(Answer::getQuestionId) // Récupérer le questionId
                .distinct() // Filtrer pour obtenir des questionId distincts
                .count(); // Compter le nombre de questionId distincts

        return (int) distinctCount; // Retourner le nombre en tant qu'int
    }


    private void displayQuestion(List<Question> questions, Boolean complitedBool) {
        // Clear all components from questionPanel

        if(complitedBool==false){
            List <Answer> answerInInstance = Answer.getAll();
            int distinctCount = countDistinctAnswersByInstanceId(answerInInstance, latestInstanceByForm.getId());
            listDistinctAnswersByInstanceId(answerInInstance, latestInstanceByForm.getId());
            currentQuestionIndex = distinctCount;
        }

        questionPanel.removeAllComponents();
        addSpacingLabel(questionPanel, 1);

        // Validate questions list
        if (questions == null || questions.isEmpty()) {
            System.out.println("No questions available.");
            return;
        }

        // Check that currentQuestionIndex is valid
        if (currentQuestionIndex < 0 || currentQuestionIndex >= questions.size()) {
            System.out.println("Invalid question index: " + currentQuestionIndex);
            return;
        }

        // Get the current question
        Question question = questions.get(currentQuestionIndex);

        if (question == null) {
            System.out.println("Question object is null.");
            return;
        }

        // Display question number and total
        Label questionNumberLabel = new Label("Question " + (currentQuestionIndex + 1) + " of " + questions.size());
        questionPanel.addComponent(questionNumberLabel);
        addSpacingLabel(questionPanel, 1);

        // Render the question title with an optional required marker
        addQuestionTitleWithRequiredMarker(question);

        // Render the appropriate input fields based on question type
        addSpacingLabel(questionPanel, 1);
        renderQuestionInput(question);

        // Display "Input Required" label if the question is required
        if (question.getRequired()) {
            Label requiredLabelText = new Label("Input Required (*)").setForegroundColor(TextColor.ANSI.RED);
            questionPanel.addComponent(requiredLabelText);
        }

        // Error message label
        errorMessageLabel = new Label("").setForegroundColor(TextColor.ANSI.RED);
        questionPanel.addComponent(errorMessageLabel);

        // Refresh and update button visibility for navigation
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



    private TextBox addTextBoxInput(int width) {
        TextBox textBox = new TextBox(new TerminalSize(width, 1));
        questionPanel.addComponent(textBox);
        return textBox; // Return the created TextBox for further use
    }

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

    private CheckBoxList<String> addCheckBoxListInput(OptionList options, int optionListId) {
        options.setId(optionListId);
        CheckBoxList<String> checkBoxList = new CheckBoxList<>(new TerminalSize(55, 10));
        for (OptionValue optionValue : options.getOptionValues()) {
            checkBoxList.addItem(optionValue.getLabel());
        }
        questionPanel.addComponent(checkBoxList);
        return checkBoxList; // Return the created CheckBoxList for further use
    }

    private RadioBoxList<String> addRadioBoxListInput(OptionList options, int optionListId) {
        options.setId(optionListId);
        RadioBoxList<String> radioList = new RadioBoxList<>(new TerminalSize(55, 10));
        for (OptionValue optionValue : options.getOptionValues()) {
            radioList.addItem(optionValue.getLabel());
        }
        questionPanel.addComponent(radioList);
        return radioList; // Return the created RadioBoxList for further use
    }


    // Méthode pour créer les boutons et les ajouter au panel buttonPanel
    private Label errorMessageLabel; // Add a field for the error message label

    private boolean isInputValid(Question currentQuestion) {
        Component currentComponent = questionPanel.getChildren().stream()
                .filter(component -> component instanceof TextBox || component instanceof ComboBox ||
                        component instanceof CheckBoxList || component instanceof RadioBoxList)
                .findFirst().orElse(null);

        if (currentComponent instanceof TextBox) {
            String text = ((TextBox) currentComponent).getText().trim();
            if (text.isEmpty()) {
                showError("This field cannot be empty.");
                return false;
            }
            // Additional checks based on question type
            if ("email".equalsIgnoreCase(currentQuestion.getType().toString()) && !isValidEmail(text)) {
                showError("Invalid email format.");
                return false;
            }  else if ("date".equalsIgnoreCase(currentQuestion.getType().toString()) && !isValidDate(text)) {
                showError("Invalid date format.");
                return false;
            }
            return true;
        } else if (currentComponent instanceof ComboBox) {
            if (((ComboBox<?>) currentComponent).getSelectedItem() == null ||
                    "Please select one".equals(((ComboBox<?>) currentComponent).getSelectedItem())) {
                showError("Please select an option.");
                return false;
            }
            return true;
        } else if (currentComponent instanceof CheckBoxList) {
            if (((CheckBoxList<?>) currentComponent).getCheckedItems().isEmpty()) {
                showError("Please select at least one option.");
                return false;
            }
            return true;
        } else if (currentComponent instanceof RadioBoxList) {
            if (((RadioBoxList<?>) currentComponent).getCheckedItem() == null) {
                showError("Please select an option.");
                return false;
            }
            return true;
        }

        return true; // Default case for unsupported components
    }


    // Méthode pour afficher un message d'erreur
    private void showError(String message) {
        errorMessageLabel.setText("Error: " + message); // Update the error message label
    }

    // Email validation method
    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$"; // Simple regex for email validation
        return email.matches(emailRegex);
    }


    private boolean isValidDate(String date) {
        String[] dateFormats = {
                "yyyy-MM-dd",
                "dd-MM-yyyy",
                "MM/dd/yyyy",
                "dd/MM/yyyy"
        };

        for (String format : dateFormats) {
            try {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
                LocalDate.parse(date, dateFormatter);
                return true; // Valid date found
            } catch (DateTimeParseException e) {
                // Continue checking the next format
            }
        }
        return false; // All formats failed
    }


    // Méthode pour afficher la valeur actuelle de l'input
    private void displayCurrentInputValue(List<Question> questions) {


        Question question = questions.get(currentQuestionIndex);
        Component currentComponent = questionPanel.getChildren().stream()
                .filter(component -> component instanceof TextBox || component instanceof ComboBox ||
                        component instanceof CheckBoxList || component instanceof RadioBoxList)
                .findFirst().orElse(null);

        // Check if an entry with the same question ID already exists
        Object[] existingEntry = answerList.stream()
                .filter(entry -> (int) entry[1] == question.getId())
                .findFirst()
                .orElse(null);

        if (currentComponent instanceof TextBox) {
            String inputValue = ((TextBox) currentComponent).getText();

            if (existingEntry != null) {
                existingEntry[2] = inputValue; // Update response
            } else {
                Object[] newEntry = new Object[3];
                newEntry[0] = instanceID;        // Instance ID
                newEntry[1] = question.getId(); // Question ID
                newEntry[2] = inputValue;       // Response
                answerList.add(newEntry);       // Add new entry
            }

            System.out.println("Input Value: " + inputValue);

        } else if (currentComponent instanceof ComboBox) {
            Object selectedOption = ((ComboBox<?>) currentComponent).getSelectedItem();

            if (existingEntry != null) {
                existingEntry[2] = selectedOption; // Update response
            } else {
                Object[] newEntry = new Object[3];
                newEntry[0] = instanceID;
                newEntry[1] = question.getId();
                newEntry[2] = selectedOption;
                answerList.add(newEntry);
            }

            System.out.println("Selected Option: " + selectedOption);

        } else if (currentComponent instanceof CheckBoxList) {
            List<?> selectedItems = ((CheckBoxList<?>) currentComponent).getCheckedItems();

            // Convert selected items to a single string for storage
            String selectedItemsString = selectedItems.stream()
                    .map(Object::toString) // Convert each selected item to a String
                    .collect(Collectors.joining(", ")); // Join them with commas

            if (existingEntry != null) {
                // If an entry already exists, update the response with the combined string
                existingEntry[2] = selectedItemsString; // Update response with concatenated string
            } else {
                // Create a new entry for the answerList
                Object[] newEntry = new Object[3];
                newEntry[0] = instanceID; // Assuming this is the ID of the instance
                newEntry[1] = question.getId(); // Get the question ID
                newEntry[2] = selectedItemsString; // Store the concatenated string of selected items
                answerList.add(newEntry); // Add the new entry to the list
            }

            // Print the selected options
            System.out.println("Selected Options: " + selectedItemsString);

        } else if (currentComponent instanceof RadioBoxList) {
            Object selectedOption = ((RadioBoxList<?>) currentComponent).getCheckedItem();

            if (existingEntry != null) {
                existingEntry[2] = selectedOption; // Update response
            } else {
                Object[] newEntry = new Object[3];
                newEntry[0] = instanceID;
                newEntry[1] = question.getId();
                newEntry[2] = selectedOption;
                answerList.add(newEntry);
            }

            System.out.println("Selected Option: " + selectedOption);
        }
    }


    public int getMaxInstanceIdInstanceTable(List<Instance> instances) {
        return instances.stream()
                .mapToInt(Instance::getId) // Assuming getId() returns the ID of an instance
                .max()
                .orElseThrow(() -> new NoSuchElementException("No instances found"));
    }


    // Méthode pour mettre à jour la visibilité des boutons "Next" et "Previous"
    private void createButtons(List<Question> questions) {
        // Initialize the error message label
        // Add the error message label to the panel

        Button cancelButton = new Button("Cancel", () -> {
            System.out.println("Cancel clicked");
        });

        previousButton = new Button("Previous", () -> {
            if (currentQuestionIndex > 0) {
                // Before moving to the previous question, display the answer for the current question index
                Object[] currentAnswer = answerList.get(currentQuestionIndex - 1); // Get the answer for the previous question
                System.out.println("Previous Question ID: " + currentAnswer[1]); // Display the question ID
                if (currentAnswer[2] instanceof List) {
                    // Check if the response is a List and print the responses
                    List<?> responses = (List<?>) currentAnswer[2];
                    String responseString = responses.stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(", "));
                    System.out.println("Previous Responses: " + responseString);
                } else {
                    System.out.println("Previous Response: " + currentAnswer[2]);
                }

                currentQuestionIndex--; // Go to the previous question
                displayQuestion(questions,true); // Update the display for the previous question
            }
        });



        nextButton = new Button("Next", () -> {
            errorMessageLabel.setText(""); // Clear previous error message

            // Pass the current question to isInputValid
            Question currentQuestion = questions.get(currentQuestionIndex);

            if (currentQuestion.getRequired()) {
                if (isInputValid(currentQuestion)) {
                    displayCurrentInputValue(questions); // Display current input value
                    if (currentQuestionIndex < questions.size() - 1) {
                        currentQuestionIndex++; // Aller à la question suivante
                        displayQuestion(questions,true); // Actualiser l'affichage
                        printAnswerList();
                    }
                }
            } else {
                displayCurrentInputValue(questions); // Display current input value
                if (currentQuestionIndex < questions.size() - 1) {
                    currentQuestionIndex++; // Aller à la question suivante
                    displayQuestion(questions, true); // Actualiser l'affichage
                }
            }


        });

        Button closeButton = new Button("Close", () -> {
            displayCurrentInputValue(questions);
            displayAnswerList(); // Display the answer list
            close();    // Close the application
        });

// Assuming mainPanel is the main container panel for your UI


        // Add buttons to the buttonPanel
        buttonPanel.addComponent(closeButton);
        buttonPanel.addComponent(cancelButton);



    }

    // Assuming you have a method to run the main UI loop



    private void printAnswerList() {
        System.out.println("----- Submitted Answers -----");

        for (Object[] entry : answerList) {
            // Assuming entry[0] is instanceId, entry[1] is questionId, and entry[2] is response
            int instanceId = (int) entry[0]; // Get the instance ID
            int questionId = (int) entry[1]; // Get the question ID
            Object response = entry[2];      // Get the response

            System.out.println("Instance ID: " + instanceId);
            System.out.println("Question ID: " + questionId);

            // Check if the response is a List (for CheckBoxList)
            if (response instanceof List) {
                List<?> responses = (List<?>) response;
                String responseString = responses.stream()
                        .map(Object::toString) // Convert each item to a String
                        .collect(Collectors.joining(", "));
                System.out.println("Responses: " + responseString);
            } else {
                // Print response directly (for TextBox, ComboBox, or RadioBoxList)
                System.out.println("Response: " + response);
            }

            System.out.println("------------------------------");
        }
    }



    private void updateButtonPanel(int totalQuestions, List<Question> questions) {
        // Supprimer tous les boutons actuels du buttonPanel sauf "Close" et "Cancel"
        buttonPanel.removeAllComponents();

        // Ajouter conditionnellement les boutons "Previous" et "Save"
        Button closeButton = new Button("Close", ()->{ displayCurrentInputValue(questions);
            displayAnswerList(); // Display the answer list
            close(); });

        Button cancelButton = new Button("Cancel", () -> {
            ConfirmationCancel();
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
        } else {
            // Ajouter "SUBMIT" si c'est la dernière question
            Button saveButton = new Button("Submit", () -> {
                // Clear any previous error messages
                errorMessageLabel.setText("");

                // Retrieve the current question and component

                displayCurrentInputValue(questions); // Save current input to answerList

                // Now handle the submission process
                ConfirmationOfSubmission();
            });

            buttonPanel.addComponent(saveButton);
        }

        // Actualiser l'affichage du panneau des boutons
        buttonPanel.invalidate(); // Rafraîchir l'affichage des boutons
    }


    private void ConfirmationOfSubmission() {
        setViewTitle("Submit form");
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(55, 5)); // Définir une taille préférée pour le panel

        Label textLabel = new Label("Are you sure you want to submit this form ?");
        mainPanel.addComponent(textLabel); // Ajouter le label au panel principal


        mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)), LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));


        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("Yes", this::ButtonConfirmYes));
        buttonPanel.addComponent(new Button("No", this::ButtonConfirmNo));

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
    private void ButtonConfirmYes()
    {
        complited = LocalDateTime.now();
        displayAnswerList();
        IndicateFormSubmitted();
    }
    private void ButtonConfirmNo() {
     close();
    }

    private void displayAnswerList() {
        System.out.println("----- Submitted Answers -----");


        saveInstance(idForm, userId, started, complited); // Assuming this will set a new instance ID for the latest submission

        for (Object[] entry : answerList) {
            int questionId = (int) entry[1]; // Get the question ID (first element)
            Object response = entry[2]; // Get the response (second element)

            System.out.println("Question ID: " + questionId);

            // Check if the response is a List (as returned by CheckBoxList)
            if (response instanceof List) {
                List<?> responses = (List<?>) response;
                String responseString = responses.stream()
                        .map(Object::toString) // Convert each item to a String
                        .collect(Collectors.joining(", ")); // Join responses with a comma
                System.out.println("Responses: " + responseString);

                // Save each response for CheckBoxList under the same instance ID
                for (Object res : responses) {
                    saveAnswer(instanceID, questionId, res.toString());
                }
            } else {
                // Print response directly (for TextBox, ComboBox, or RadioBoxList)
                System.out.println("Response: " + response);
                saveAnswer(instanceID, questionId, response.toString()); // Save response
            }

            System.out.println("Instance ID: " + instanceID);
            System.out.println("------------------------------");
        }
    }


    private void saveAnswer(int instanceId, int questionId, String value) {
        Answer answer = new Answer(); // Create a new Answer object
        answer.setInstanceId(instanceId); // Assuming you have a method to set instance ID
        answer.setQuestionId(questionId); // Assuming you have a method to set question ID
        answer.setValue(value); // Assuming you have a method to set value

        // Call the save method
        answer.save();
    }

    private void saveInstance(int formId, int userId, LocalDateTime started, LocalDateTime completed) {
        Instance instance = new Instance(); // Create a new Instance object
        instance.setFormId(formId); // Set form ID
        instance.setUserId(userId); // Set user ID
        instance.setStarted(started); // Set started time
        instance.setCompleted(completed); // Set completed time

        // Call the save method
        instance.save();
    }



    private void IndicateFormSubmitted() {

        setViewTitle("Information");
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(55, 5)); // Définir une taille préférée pour le panel

        Label textLabel = new Label("The form has been successfully submitted");
        mainPanel.addComponent(textLabel); // Ajouter le label au panel principal


        mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)), LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));


        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("OK", this::close));


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
    private void ButtonOK() {

    }
    private void ConfirmationCancel() {
        setViewTitle("Delete Instance");
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(45, 3)); // Définir une taille préférée pour le panel

        Label textLabel = new Label("Are you sure you want to delete this instance and its answers ?");
        mainPanel.addComponent(textLabel); // Ajouter le label au panel principal


        mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)), LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));


        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("Yes", this::ButtonCancelYes));
        buttonPanel.addComponent(new Button("No", this::ButtonCancelNo));

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
    private void ButtonCancelYes() {
        close();
    }
    private void ButtonCancelNo() {
        close();
    }




    ///////////////////////////////////////////////////////

    private void buttonViewSubmission() {
        ViewSubmission();
    }


    private void ViewSubmission() {

        setViewTitle("View Answers"); // Titre spécifique pour ViewAnswers
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(55, 20));

        Form form = new Form();

        form.setId(idForm);

        List<Question> questions = form.getQuestions();

        List<Instance> value = form.getInstances();

        latestInstanceByForm = getLatestInstance(value);

        Instance FirstInstance = getFirstInstance(value);

        User user = User.getByKey(userId);

        Form formData = Form.getByKey(idForm);

        // Créer et ajouter les labels principaux (Title, Description, and Date)
        Label titleLabel = new Label("Title: " + (formData.getTitle() != null ? formData.getTitle() : "null"));
        Label descriptionLabel = new Label("Description: " + (formData.getDescription() != null ? formData.getDescription() : "null"));
        Label startedOn = new Label("Started On: " + (FirstInstance.getStarted() != null ? FirstInstance.getStarted() : "null"));
        Label dateLabel = new Label("Submitted on: " +  (latestInstanceByForm.getCompleted() != null ? latestInstanceByForm.getCompleted() : "null"));
        Label submitterLabel = new Label("Submitted by: " + user.getFullName());

        mainPanel.addComponent(titleLabel);
        mainPanel.addComponent(descriptionLabel);
        mainPanel.addComponent(startedOn);
        mainPanel.addComponent(dateLabel);
        mainPanel.addComponent(submitterLabel);

        // Créer un panneau pour afficher la reponse actuelle
        questionPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        questionPanel.setPreferredSize(new TerminalSize(55, 25)); // Taille du panneau

        Panel container = new Panel(new LinearLayout(Direction.HORIZONTAL));
        container.addComponent(new EmptySpace(new TerminalSize(20, 1))); // Espace vide avant
        container.addComponent(questionPanel);
        container.addComponent(new EmptySpace(new TerminalSize(3, 1))); // Espace vide après



        // Créer un panel pour organiser les boutons horizontalement
        buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL)); // Initialisation du buttonPanel

        // Ajouter les boutons "Previous", "Next", "Close", et "Cancel"


        mainPanel.addComponent(questionPanel);
        mainPanel.addComponent(buttonPanel);

        displayQuestionViewSubmition(questions);

        setComponent(mainPanel);




        private void displayQuestionViewSubmition(List<Question> questions) {
            // Clear all components from questionPanel

            questionPanel.removeAllComponents();
            addSpacingLabel(questionPanel, 1);

            // Validate questions list
            if (questions == null || questions.isEmpty()) {
                System.out.println("No questions available.");
                return;
            }

            // Check that currentQuestionIndex is valid
            if (currentQuestionIndex < 0 || currentQuestionIndex >= questions.size()) {
                System.out.println("Invalid question index: " + currentQuestionIndex);
                return;
            }
            // Get the current question
            Question question = questions.get(currentQuestionIndex);

            if (question == null) {
                System.out.println("Question object is null.");
                return;
            }
            // Display question number and total
            Label questionNumberLabel = new Label("Question " + (currentQuestionIndex + 1) + " of " + questions.size());
            questionPanel.addComponent(questionNumberLabel);
            addSpacingLabel(questionPanel, 1);

            // Render the appropriate input fields based on question type
            addSpacingLabel(questionPanel, 1);

            addQuestionTitleWithRequiredMarker(question);

            createOrUpdateButtonsViewSubmission(questions);

            List<Answer> Answer = getAnswersForQuestion(question,latestInstanceByForm.getId());

            Label answer = new Label(Answer.getFirst().getValue());
            questionPanel.addComponent(answer);

            // Utility method to get answers for a given instance
            public List<Answer> getAnswersForInstance(int instanceId) {
                // Create an instance object to call the existing getAnswers() method
                Instance instance = new Instance();
                instance.setId(instanceId); // Set the id if not already set

                // Retrieve the answers list using the getAnswers() method
                return instance.getAnswers();
            }
            / Use this utility method elsewhere to get answers for a specific instance
            public List<Answer> getAnswersForQuestion(Question question, int instanceId) {
                // Retrieve all answers for the specified instance
                List<Answer> answers = getAnswersForInstance(instanceId);

                // Filter answers by question ID
                List<Answer> filteredAnswers = answers.stream()
                        .filter(answer -> answer.getQuestionId() == question.getId())
                        .collect(Collectors.toList());

                // Return the filtered list of answers
                return filteredAnswers;
            }

            private void createOrUpdateButtonsViewSubmission(List<Question> questions) {
                // Réinitialiser le panneau de boutons
                buttonPanel.removeAllComponents();

                // Vérifier si nous ne sommes pas à la première question
                if (currentQuestionIndex > 0) {
                    Button previousButton = new Button("Previous", () -> {
                        currentQuestionIndex--; // Décrémenter l'index de la question
                        displayQuestionViewSubmition(questions); // Afficher la question précédente
                        createOrUpdateButtonsViewSubmission(questions); // Mettre à jour les boutons
                    });
                    buttonPanel.addComponent(previousButton); // Ajouter le bouton "Previous"
                }
                // Vérifier si nous ne sommes pas à la dernière question
                if (currentQuestionIndex < questions.size() - 1) {
                    Button nextButton = new Button("Next", () -> {
                        currentQuestionIndex++; // Incrémenter l'index de la question
                        displayQuestionViewSubmition(questions); // Afficher la question suivante
                        createOrUpdateButtonsViewSubmission(questions); // Mettre à jour les boutons
                    });
                    buttonPanel.addComponent(nextButton); // Ajouter le bouton "Next"
                }
                // Bouton "Cancel" (affiché dans tous les cas)
                Button cancelButton = new Button("Cancel", () -> {
                    // Logique pour annuler ou fermer le formulaire
                    System.out.println("Action canceled.");
                    // Fermez ou retournez à l'écran précédent ici
                    // par exemple : setComponent(previousScreen);
                });
                buttonPanel.addComponent(cancelButton); // Ajouter le bouton "Cancel"
            }







        }

















    }



    public Panel getMainPanel() {
        return mainPanel;
    }}