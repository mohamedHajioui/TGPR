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


import java.util.List;

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

    public AddEditQuestionView(AddEditQuestionController controller, Question question) {
        super((question == null ? "Add" : "Edit") + "Question");
        this.controller = controller;
        this.question = question;
        setHints(List.of(Hint.CENTERED,Hint.FIXED_SIZE));
        //permet de fermer avec Echap
        setCloseWindowWithEscape(true);
        setFixedSize(new TerminalSize(70, 15));
        Panel root = new Panel();
        root.setLayoutManager(new GridLayout(2).setTopMarginSize(1));
        new Label("Title :").addTo(root);
        txtTitle = new TextBox(new TerminalSize(10, 10));
        new EmptySpace().addTo(root);
        errTitle = new Label("").addTo(root).setForegroundColor(TextColor.ANSI.RED);
        new Label("Description :").addTo(root);
        txtDescription = new TextBox(new TerminalSize(15, 12));
        new EmptySpace().addTo(root);
        errDescription = new Label("").addTo(root).setForegroundColor(TextColor.ANSI.RED);
        new Label("Type :").addTo(root);
        cbType = new ComboBox<Question.Type>().addTo(root);
        new Label("Required :").addTo(root);
        lblRequired = new Label("").addTo(root).addStyle(SGR.BOLD);
        var optionlist = new Panel().addTo(root).setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        new Label("Option :").addTo(optionlist);
        cbOption = new ComboBox<OptionList>().addTo(optionlist);
        btnAddEdit = new Button("Add").addTo(optionlist).setEnabled(false);
        var buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL)).addTo(root)
                .setLayoutData(GridLayout.createHorizontallyFilledLayoutData(2));
        if (question.getTitle() == null) {
            // Adding a new question, show Create and Cancel buttons
            btnCreateOrUpdate = new Button("Create", this::handleCreate).addTo(buttonPanel);
        } else {
            // Editing an existing question, show Update, Delete, and Cancel buttons
            btnCreateOrUpdate = new Button("Update", this::handleUpdate).addTo(buttonPanel);
            btnDelete = new Button("Delete", this::handleDelete).addTo(buttonPanel);
        }

        // Cancel button (common to both cases)
        btnCancel = new Button("Cancel", this::handleCancel).addTo(buttonPanel);

    }
    private void handleCreate() {
        // Logique pour créer une question
        controller.createQuestion(
                txtTitle.getText(),
                txtDescription.getText(),
                cbType.getSelectedItem(),
                cbOption.getSelectedItem()
        );
    }
    private void handleUpdate() {
        // Logique pour mettre à jour une question existante
        controller.updateQuestion(
                question,
                txtTitle.getText(),
                txtDescription.getText(),
                cbType.getSelectedItem(),
                cbOption.getSelectedItem()
        );
    }

    private void handleDelete() {
        controller.deleteQuestion(question);
    }

    private void handleCancel() {
        // Fermer la fenêtre sans rien faire
        close();
    }










}