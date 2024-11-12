package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import tgpr.forms.controller.ViewInstancesController;
import tgpr.forms.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;


import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;




public class ViewInstancesView extends DialogWindow {
    private ViewInstancesController controller;
    private Panel mainPanel;
    private Table<String> instancesTable;
    private int currentForm ;

    public ViewInstancesView(ViewInstancesController controller, int currentForm) {

        super("Titre par defaut");
        this.controller = controller;
        this.currentForm = currentForm;


        ListInstancesSubmitted();
        addDeleteKeyListener(); // Add Delete key listener
        addEnterKeyListener();
    }




    private void setViewTitle(String title) {
        setTitle(title); // Method to change the title dynamically
    }

    private void ListInstancesSubmitted() {
        Form dataForm = Form.getByKey(currentForm);
        setViewTitle("List of Submitted Instances");
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(60, 20));

        // Create and add main labels (Title, Description, and Date)
        Label titleLabel = new Label("Title: " + dataForm.getTitle());
        Label descriptionLabel = new Label("Description: " + dataForm.getDescription());
        mainPanel.addComponent(titleLabel);
        mainPanel.addComponent(descriptionLabel);
        mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)), LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));

        // Create table with columns "ID", "User Name", "Date"
        instancesTable = new Table<>("ID", "User Name", "Date");
        instancesTable.setPreferredSize(new TerminalSize(50, 10));

        Form form = new Form();
        form.setId(currentForm);
        List<Instance> completedInstances = form.getCompletedInstances();
        instancesTable.getTableModel().clear();

        for (Instance instance : completedInstances) {
            String id = String.valueOf(instance.getId());
            User user = User.getByKey(instance.getUserId());
            String userName = user.getName();
            String completedDate = instance.getCompleted().toString();

            instancesTable.getTableModel().addRow(id, userName, completedDate);
        }
        mainPanel.addComponent(instancesTable);

        mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 6)), LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("Delete Selected", this::confirmDeleteSelected));
        buttonPanel.addComponent(new Button("Delete All", this::ButtonDeleteAll));
        buttonPanel.addComponent(new Button("Close", this::close));


        int selectedRow = instancesTable.getSelectedRow();
        User user;
        if (selectedRow >= 0 && selectedRow < instancesTable.getTableModel().getRowCount()) {
            // Safely access the selected row
            String id = instancesTable.getTableModel().getCell(0, selectedRow);

            Instance instance = Instance.getByKey(Integer.parseInt(id));
            user = User.getByKey(instance.getUserId());
        } else {
            user = null;
        }



        buttonPanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        mainPanel.addComponent(buttonPanel);

        Panel container = new Panel(new LinearLayout(Direction.HORIZONTAL));
        container.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        container.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        container.addComponent(mainPanel);
        container.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        setComponent(container);
        setHints(List.of(Hint.CENTERED));
    }


    private void addDeleteKeyListener() {
        this.addWindowListener(new WindowListenerAdapter() {
            @Override
            public void onUnhandledInput(Window basePane, KeyStroke keyStroke, AtomicBoolean hasBeenHandled) {
                if (keyStroke.getKeyType() == KeyType.Delete) {
                    confirmDeleteSelected();  // Trigger delete action with confirmation
                    hasBeenHandled.set(true);
                }
            }
        });
    }
    private void addEnterKeyListener() {
        this.addWindowListener(new WindowListenerAdapter() {
            @Override
            public void onUnhandledInput(Window basePane, KeyStroke keyStroke, AtomicBoolean hasBeenHandled) {
                if (keyStroke.getKeyType() == KeyType.Enter) {
                    ViewSubmission();  // Trigger delete action with confirmation
                    hasBeenHandled.set(true);
                }
            }
        });
    }

    private void confirmDeleteSelected() {
        if (instancesTable.getSelectedRow() >= 0) {
            MessageDialogButton result = MessageDialog.showMessageDialog(
                    getTextGUI(),
                    "Confirm Delete",
                    "Are you sure you want to delete this instance?",
                    MessageDialogButton.Yes, MessageDialogButton.No
            );

            if (result == MessageDialogButton.Yes) {
                deleteSelectedInstance();
            }
        }
    }

    private void deleteSelectedInstance() {
        int selectedRow = instancesTable.getSelectedRow();
        // Vérifie de nouveau que la table contient des lignes et que l'index est valide
        if (selectedRow >= 0 && selectedRow < instancesTable.getTableModel().getRowCount()) {
            String id = instancesTable.getTableModel().getCell(0, selectedRow);
            System.out.println("Delete instance with ID: " + id);

            // Suppression de l'instance dans le modèle de données
            Instance instanceDelete = new Instance();
            instanceDelete.setId(Integer.parseInt(id));
            instanceDelete.delete();

            // Supprime la ligne de la table
            instancesTable.getTableModel().removeRow(selectedRow);
        }
    }
    private void ButtonDeleteAll() {
        // Show confirmation dialog
        MessageDialogButton result = MessageDialog.showMessageDialog(
                getTextGUI(),
                "Delete All instances",
                "Are you sure you want to delete all submitted instances?\nNote : This will not delete instances that are currently being edited (not submitted).",
                MessageDialogButton.Yes, MessageDialogButton.No
        );

        // Proceed with deletion if the user confirms
        if (result == MessageDialogButton.Yes) {
            Form form = new Form();
            form.setId(currentForm);
            form.deleteAllSubmittedInstances();  // Delete all instances
            instancesTable.getTableModel().clear();  // Clear the table
        }
        // If "No" is selected, the dialog simply closes and no action is taken
    }

    private void BtnDeleteAll() {
        // Show confirmation dialog
        MessageDialogButton result = MessageDialog.showMessageDialog(
                getTextGUI(),
                "Delete All instances",
                "Are you sure you want to delete all instances?\nNote : This will also delete instances that are currently being edited (not submitted).",
                MessageDialogButton.Yes, MessageDialogButton.No
        );

        // Proceed with deletion if the user confirms
        if (result == MessageDialogButton.Yes) {
            Form form = new Form();
            form.setId(currentForm);
            form.deleteAllInstances();  // Delete all instances
            instancesTable.getTableModel().clear();  // Clear the table
        }
        // If "No" is selected, the dialog simply closes and no action is taken
    }
    private int currentQuestionIndex = 0; // Pour suivre l'indice de la question actuelle
    private Panel questionPanel; // Panel pour afficher la question actuelle
    private Button nextButton;
    private Button previousButton;
    private Panel buttonPanel; // Panel pour les boutons
    private List<Object[]> answerList = new ArrayList<>();
    private String role;
    private int instanceID;
    private LocalDateTime started ;
    private LocalDateTime complited = null;
    Instance latestInstanceByForm;
    private int currentUser = 2;

    public Instance getLatestInstance(List<Instance> instances) {
        return instances.stream()
                .filter(instance -> instance.getFormId() == this.currentForm) // Assuming 'this.id' is the desired formId
                .max(Comparator.comparing(Instance::getStarted)) // Get the latest instance based on 'started' time
                .orElse(null); // Return null if no instance is found
    }
    public Instance getFirstInstance(List<Instance> instances) {
        return instances.stream()
                .filter(instance -> instance.getFormId() == this.currentForm) // Filtrer par idForm
                .findFirst() // Obtenir la première instance trouvée
                .orElse(null); // Retourner null si aucune instance n'est trouvée
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

    private void ViewSubmission() {
        setViewTitle("View Answers"); // Titre spécifique pour AnswerForm
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(55, 20));

        Form form = new Form();
        form.setId(currentForm);

        List<Question> questions = form.getQuestions();
        List<Instance> value = form.getInstances();

        latestInstanceByForm = getLatestInstance(value);
        Instance FirstInstance = getFirstInstance(value);

        User user = User.getByKey(currentUser);
        Form formData = Form.getByKey(currentForm);

        // Créer et ajouter les labels principaux (Title, Description, Date)
        Label titleLabel = new Label("Title: " + (formData.getTitle() != null ? formData.getTitle() : "null"));
        Label descriptionLabel = new Label("Description: " + (formData.getDescription() != null ? formData.getDescription() : "null"));
        Label startedOn = new Label("Started On: " + (FirstInstance.getStarted() != null ? FirstInstance.getStarted() : "null"));
        Label dateLabel = new Label("Submitted on: " + (latestInstanceByForm.getCompleted() != null ? latestInstanceByForm.getCompleted() : "null"));
        Label submitterLabel = new Label("Submitted by: " + user.getFullName());

        mainPanel.addComponent(titleLabel);
        mainPanel.addComponent(descriptionLabel);
        mainPanel.addComponent(startedOn);
        mainPanel.addComponent(dateLabel);
        mainPanel.addComponent(submitterLabel);

        // Créer un panneau pour afficher la réponse actuelle
        questionPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        questionPanel.setPreferredSize(new TerminalSize(55, 25));

        Panel container = new Panel(new LinearLayout(Direction.HORIZONTAL));
        container.addComponent(new EmptySpace(new TerminalSize(20, 1))); // Espace vide avant
        container.addComponent(questionPanel);
        container.addComponent(new EmptySpace(new TerminalSize(3, 1))); // Espace vide après

        // Créer un panel pour organiser les boutons horizontalement
        buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));

        mainPanel.addComponent(questionPanel);
        mainPanel.addComponent(buttonPanel);

        displayQuestionViewSubmition(questions);

        setComponent(mainPanel);
    }

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

        // Récupère et affiche la réponse pour la question actuelle
        List<Answer> answers = getAnswersForQuestion(question);

        if (answers != null && !answers.isEmpty()) {
            Label answerLabel = new Label(answers.get(0).getValue());
            questionPanel.addComponent(answerLabel);
        } else {
            Label noAnswerLabel = new Label("No answer provided for this question.");
            questionPanel.addComponent(noAnswerLabel);
        }
    }

    // Utility method to get answers for a given instance
    public int getFirstCompletedInstanceIdByUserAndForm(int userId, int formId) {
        // Crée une instance de Form pour appeler la méthode getCompletedInstances
        Form form = new Form();
        form.setId(formId);
        List<Instance> completedInstances = form.getCompletedInstances();

        // Utilisez un stream pour filtrer par userId et formId, et obtenir le premier id des instances complétées, ou -1 si aucun n'est trouvé
        return completedInstances.stream()
                .filter(instance -> instance.getUserId() == userId && instance.getFormId() == formId)
                .map(Instance::getId)
                .findFirst()
                .orElse(-1);
    }

    // Use this utility method elsewhere to get answers for a specific instance
    public List<Answer> getAnswersForQuestion(Question question) {
        // Retrieve all answers for the specified instance
        Integer idInstance = getFirstCompletedInstanceIdByUserAndForm(currentUser, currentForm );

        Instance instance = new Instance();
        instance.setId(idInstance);
        List<Answer> answers = instance.getAnswers(question);

        // Filter answers by question ID
        List<Answer> filteredAnswers = answers != null ?
                answers.stream()
                        .filter(answer -> answer.getQuestionId() == question.getId())
                        .collect(Collectors.toList())
                : new ArrayList<>();
        System.out.println(filteredAnswers);

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
                createOrUpdateButtonsViewSubmission(questions);
                // Met
                // tre à jour les boutons
            });
            buttonPanel.addComponent(nextButton); // Ajouter le bouton "Next"
        }

        // Bouton "Cancel" (affiché dans tous les cas)
        Button cancelButton = new Button("Cancel", this::close );
        ;
        buttonPanel.addComponent(cancelButton); // Ajouter le bouton "Cancel"
    }









}