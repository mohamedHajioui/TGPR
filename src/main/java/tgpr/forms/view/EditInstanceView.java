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
    }

    private void RequestConfirmation() {
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(55, 5));

        Label textLabel = new Label("You Have already answered this form.\nYou can view your submission or submit again.\nWhat would you like to do?");
        mainPanel.addComponent(textLabel);

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
        container.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        container.addComponent(mainPanel);
        container.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        setComponent(container);
    }

    private void buttonViewSubmission() {
    }

    private void SubmitAgain() {
        AnswerForm();
    }

    private void buttonCancel() {
    }
    private int currentQuestionIndex = 0;
    private Panel questionPanel;
    private Button nextButton;
    private Button previousButton;
    private Panel buttonPanel;

    private void AnswerForm() {
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(55, 30));

        Label titleLabel = new Label("Title: Abc" + Form.getTitle());
        Label descriptionLabel = new Label("Description: Test form" + Form.getDescription());
        Label dateLabel = new Label("Started on: " + LocalDateTime.now());

        mainPanel.addComponent(titleLabel);
        mainPanel.addComponent(descriptionLabel);
        mainPanel.addComponent(dateLabel);

        questionPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        questionPanel.setPreferredSize(new TerminalSize(55, 25));

        Panel container = new Panel(new LinearLayout(Direction.HORIZONTAL));
        container.addComponent(new EmptySpace(new TerminalSize(20, 1)));
        container.addComponent(questionPanel);
        container.addComponent(new EmptySpace(new TerminalSize(3, 1)));

        Form form = new Form();
        form.setId(1);
        List<Question> questions = form.getQuestions();

        buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
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
                    .setForegroundColor(TextColor.ANSI.RED);

            titlePanel.addComponent(requiredLabel);
        }

        questionPanel.addComponent(questionNumberLabel);
        questionPanel.addComponent(emptyLabel1);
        questionPanel.addComponent(titlePanel);
        questionPanel.addComponent(new Label(""));

        if (question != null) {
            String type = String.valueOf(question.getType());

            OptionList options = new OptionList();
            switch (type.toLowerCase()) {
                case "short":
                    TextBox shortInput = new TextBox(new TerminalSize(45, 1));
                    questionPanel.addComponent(shortInput);
                    break;
                case "email":
                    TextBox email = new TextBox(new TerminalSize(45, 1));
                    questionPanel.addComponent(email);
                    break;
                case "date":
                    TextBox date = new TextBox(new TerminalSize(45, 1));
                    questionPanel.addComponent(date);
                    break;
                case "long":
                    TextBox longInput = new TextBox(new TerminalSize(55, 1));
                    questionPanel.addComponent(longInput);
                    break;
                case "combo":
                    options.setId(question.getOptionListId());
                    List<OptionValue> valueOptions = options.getOptionValues();

                    ComboBox<String> comboInput = new ComboBox<>();
                    for (OptionValue optionValue : valueOptions) {
                        comboInput.addItem(optionValue.getLabel());
                    }

                    questionPanel.addComponent(comboInput);
                    break;
                case "check":
                    options.setId(question.getOptionListId());
                    List<OptionValue> checkboxOptions = options.getOptionValues();

                    CheckBoxList<String> checkboxList = new CheckBoxList<>(new TerminalSize(55, 10));
                    for (OptionValue optionValue : checkboxOptions) {
                        checkboxList.addItem(optionValue.getLabel());
                    }

                    questionPanel.addComponent(checkboxList);
                    break;
                case "radio":
                    options.setId(question.getOptionListId());
                    List<OptionValue> radioOptions = options.getOptionValues();

                    RadioBoxList<String> radioList = new RadioBoxList<>(new TerminalSize(55, 10));
                    for (OptionValue optionValue : radioOptions) {
                        radioList.addItem(optionValue.getLabel());
                    }

                    questionPanel.addComponent(radioList);
                    break;
            }
        }

        if (question.getRequired()) {
            questionPanel.addComponent(requiredLabelText);
        }

        questionPanel.invalidate();
        updateButtonPanel(questions.size());
    }

}
