package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.gui2.CheckBox;
import tgpr.forms.controller.AddEditFormController;
import tgpr.forms.model.Form;
import tgpr.forms.model.User;

import java.util.List;
import java.util.regex.Pattern;

public class AddEditFormView extends DialogWindow {
    private final AddEditFormController controller;
    private final User owner;
    private TextBox txtTitle;
    private final TextBox txtDescription;
    private final Form form;
    private final CheckBox chkIsPublic;
    private final Label errTitle;
    private final Label errDescription;
    private final Panel root;
    private final Panel formPanel;
    private final Panel buttonsPanel;
    private final Button btnAddUpdate;
    private final Button btnCancel;

    public AddEditFormView(AddEditFormController controller, User owner, Form form) {
        super((form == null ? "Add" : "Edit") + " a form");

        this.form = form;
        this.owner = owner;
        this.controller = controller;

        setHints(List.of(Hint.CENTERED, Hint.FIXED_SIZE));
        setCloseWindowWithEscape(true);
        setFixedSize(new TerminalSize(70, 11));

        root = new Panel().setLayoutManager(new BorderLayout());

        formPanel = new Panel().addTo(root).setLayoutData(BorderLayout.Location.TOP)
                .setLayoutManager(new GridLayout(2).setTopMarginSize(1).setLeftMarginSize(1));

        new Label("Title:").addTo(formPanel);
        txtTitle = new TextBox(new TerminalSize(20, 1)).addTo(formPanel)
                .setValidationPattern(Pattern.compile("[a-z A-Z][a-z A-Z\\d.;:/,-_]{0,25}"))
                .setTextChangeListener((txt, byUser) -> validate());
        new EmptySpace().addTo(formPanel);
        errTitle = new Label("title required").addTo(formPanel).setForegroundColor(TextColor.ANSI.RED);

        new Label("Description:").addTo(formPanel);
        txtDescription = new TextBox(new TerminalSize(55, 4)).addTo(formPanel)
                .setValidationPattern(Pattern.compile("[a-z A-Z][a-z A-Z\\d.;:/,-_]{0,200}"))
                .setTextChangeListener((txt, byUser) -> validate());
        new EmptySpace().addTo(formPanel);
        errDescription = new Label("").addTo(formPanel).setForegroundColor(TextColor.ANSI.RED);

        new Label("Public:").addTo(formPanel);
        chkIsPublic = new CheckBox().addTo(formPanel);

        buttonsPanel = new Panel().addTo(root).setLayoutData(BorderLayout.Location.BOTTOM)
                .setLayoutManager(new GridLayout(3));
        new EmptySpace().addTo(buttonsPanel)
                .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.BEGINNING, true, false));
        btnAddUpdate = new Button(form == null ? "Create" : "Update", this::createOrUpdate).addTo(buttonsPanel).setEnabled(false)
                .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.END, GridLayout.Alignment.BEGINNING));
        txtTitle.setTextChangeListener((txtTitle, byUser) -> validate());

        btnCancel = new Button("Cancel", this::close).addTo(buttonsPanel)
                .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.END, GridLayout.Alignment.BEGINNING));

        setComponent(root);

        if (form != null) {
            txtTitle.setText(form.getTitle());
            txtDescription.setText(form.getDescription());
            chkIsPublic.setChecked(form.getIsPublic());
        }
    }

    private void createOrUpdate() {
        if (form == null) {
            addForm();
        } else {
            updateForm();
        }
    }

    private void addForm() {
        controller.addForm(
                txtTitle.getText(),
                txtDescription.getText(),
                chkIsPublic.isChecked()
        );
    }

    private void updateForm() {
        controller.updateForm(
                txtTitle.getText(),
                txtDescription.getText(),
                chkIsPublic.isChecked()
        );
    }

    private void validate() {
        var errors = controller.validate(
                txtTitle.getText(),
                txtDescription.getText()
        );

        errTitle.setText(errors.getFirstErrorMessage(Form.Fields.Title));
        errDescription.setText(errors.getFirstErrorMessage(Form.Fields.Description));

        btnAddUpdate.setEnabled(errors.isEmpty());
    }

}