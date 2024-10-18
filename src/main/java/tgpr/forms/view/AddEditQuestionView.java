package tgpr.forms.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import tgpr.forms.controller.AddEditQuestionController;
import tgpr.forms.model.OptionList;
import tgpr.forms.model.Question;


import java.util.*;

public class AddEditQuestionView extends DialogWindow {
    private final AddEditQuestionController controller;
    private final TextBox txtTitle;
    private final TextBox txtDescription;
    private final Label errTitle;
    private final Label errDescription;
    private final ComboBox<Question.Type> cbType;
    private final Label lblRequired;
    private final ComboBox<OptionList> cbOption;
    private final Question question;
    private final Button btnAddEdit;
    private final Button btnCreateOrUpdate;
    private final Button btnCancel;
    private Button btnDelete;
    private final Label errOptionList;

    public AddEditQuestionView(AddEditQuestionController controller, Question question) {
        super((question == null ? "Add " : "Edit ") + "Question");
        this.controller = controller;
        this.question = question;
        setHints(List.of(Hint.CENTERED,Hint.FIXED_SIZE));
        //permet de fermer avec Echap
        setCloseWindowWithEscape(true);
        setFixedSize(new TerminalSize(70, 16));
        Panel root = new Panel();
        setComponent(root);
        root.setLayoutManager(new GridLayout(2).setTopMarginSize(1).setBottomMarginSize(0).setLeftMarginSize(0).setRightMarginSize(0).setHorizontalSpacing(1));


        new Label("Title:").addTo(root);
        txtTitle = new TextBox(new TerminalSize(28, 1)).addTo(root);
        new EmptySpace(new TerminalSize(0, 1)).addTo(root);
        errTitle = new Label("").addTo(root).setForegroundColor(TextColor.ANSI.RED);

        new Label("Description:").addTo(root);
        txtDescription = new TextBox(new TerminalSize(45, 3)).addTo(root);
        new EmptySpace(new TerminalSize(0, 1)).addTo(root);
        errDescription = new Label("").addTo(root).setForegroundColor(TextColor.ANSI.RED);
        new Label("Type :").addTo(root);
        cbType = new ComboBox<Question.Type>().addTo(root);
        questionTypes();
        cbType.addListener((selectedIndex, previousSelection, changedByUserInteraction) -> updateOptionListState());
        new EmptySpace(new TerminalSize(0,1)).addTo(root);
        new EmptySpace(new TerminalSize(0,1)).addTo(root);

        new Label("Required :").addTo(root);
        lblRequired = new Label("").addTo(root).addStyle(SGR.BOLD);

        new EmptySpace(new TerminalSize(0,1)).addTo(root);
        new EmptySpace(new TerminalSize(0,1)).addTo(root);

        new Label("Option List :").addTo(root);
        Panel optionPanel = new Panel(new LinearLayout(Direction.HORIZONTAL)).addTo(root);
        cbOption = new ComboBox<OptionList>().addTo(optionPanel);
        btnAddEdit = new Button("Add").addTo(optionPanel);
        new EmptySpace(new TerminalSize(1,0)).addTo(root);
        errOptionList = new Label("").addTo(root).setForegroundColor(TextColor.ANSI.RED);

        root.addComponent(new EmptySpace(new TerminalSize(1, 2)).setLayoutData(GridLayout.createHorizontallyFilledLayoutData(2)));

        var buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        if (question == null) {
            // Add a new question, show Create and Cancel buttons
            btnCreateOrUpdate = new Button("Create", this::handleCreate).addTo(buttonPanel);
        } else {
            // Edit a question, Update, Delete, and Cancel buttons
            btnCreateOrUpdate = new Button("Update", this::handleUpdate).addTo(buttonPanel);
            btnDelete = new Button("Delete", this::handleDelete).addTo(buttonPanel);
        }
        // Cancel button (common to both cases)
        btnCancel = new Button("Cancel", this::handleCancel).addTo(buttonPanel);
        root.addComponent(buttonPanel);
        buttonPanel.setLayoutData(GridLayout.createHorizontallyEndAlignedLayoutData(2));

        txtTitle.setTextChangeListener((text, changedByUserInteraction) -> validateTitle());
        txtDescription.setTextChangeListener((text, changedByUserInteraction) -> validateDescription());
        cbType.addListener((selectedIndex, previousSelection, changedByUserInteraction) -> updateOptionListState());
        cbOption.addListener((selectedIndex, previousSelection, changedByUserInteraction) -> validateOptionList());

        List<OptionList> optionLists = controller.getOptionLists();
        for (OptionList optionList : optionLists) {
            cbOption.addItem(optionList);
        }
        updateOptionListState();
    }


    private void questionTypes() {
        List<Question.Type> types = new ArrayList<>(Arrays.asList(Question.Type.values()));
        types.sort(Comparator.comparing(Enum::name));
        for (Question.Type type : types) {
            cbType.addItem(type);
        }
    }

    private void handleCreate() {
        if (validateFields()) {
            controller.createQuestion(
                    txtTitle.getText(),
                    txtDescription.getText(),
                    cbType.getSelectedItem(),
                    cbOption.getSelectedItem()
            );
            close();
        }
    }

    private void handleUpdate() {
        if (validateFields()) {
            controller.updateQuestion(
                    question,
                    txtTitle.getText(),
                    txtDescription.getText(),
                    cbType.getSelectedItem(),
                    cbOption.getSelectedItem()
            );
        }
    }


    private void handleDelete() {
        controller.deleteQuestion(question);
    }

    private void handleCancel() {
        // Fermer la fenêtre sans rien faire
        close();
    }
    private void validateTitle() {
        if (txtTitle.getText().isEmpty()) {
            errTitle.setText("Title is required");

        } else if (txtTitle.getText().length() < 3) {
            errTitle.setText("min 3 caracters");
        }
        else {
            errTitle.setText("");
        }
    }

    private void validateDescription() {
        if (question != null && txtDescription.getText().length() < 3) {
            errDescription.setText("3 char");
        } else {
            errDescription.setText("");
        }
    }

    private void validateOptionList() {
        if (cbType.getSelectedItem() != null && cbType.getSelectedItem().requiresOptionList() && cbOption.getSelectedItem() == null) {
            errOptionList.setText("Required for this type");
        } else {
            errOptionList.setText("");
        }
    }
    private void updateOptionListState() {
        if (cbType.getSelectedItem() != null && cbType.getSelectedItem().requiresOptionList()) {
            cbOption.setEnabled(true);
            cbOption.setSelectedItem(null);
        } else {
            cbOption.setEnabled(false);
            cbOption.setSelectedItem(null);
        }
    }

    private boolean validateFields() {
        validateTitle();
        validateDescription();
        validateOptionList();
        return errTitle.getText().isEmpty() && errDescription.getText().isEmpty()  && errOptionList.getText().isEmpty();
    }
}