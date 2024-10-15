package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.gui2.CheckBox;
import tgpr.forms.controller.AddEditFormController;
import tgpr.forms.model.Form;

import java.util.List;
import java.util.regex.Pattern;

public class AddEditFormView extends DialogWindow {
    private final AddEditFormController controller;
    private final Form form;
    private final TextBox txtTitle;
    private final TextBox txtDescription;
    private final Label errTitle;
    private final CheckBox isPublic;
    private final Button btnAddUpdate;

    public AddEditFormView(AddEditFormController controller, Form form) {
        super((form == null ? "Add" : "Edit ") + " a form");

        this.form = form;
        this.controller = controller;

        setHints(List.of(Hint.CENTERED, Hint.FIXED_SIZE));
        setCloseWindowWithEscape(true);
        setFixedSize(new TerminalSize(70, 11));

        Panel root = new Panel();
        root.setLayoutManager(new GridLayout(2).setTopMarginSize(1));

        new Label("Title:").addTo(root);
        txtTitle = new TextBox(new TerminalSize(20, 1)).addTo(root)
                .setValidationPattern(Pattern.compile("[a-z A-Z][a-z A-Z\\d]{0,25}"))
                .setTextChangeListener((txt, byUser) -> validate())
                .setReadOnly(form != null);
        new EmptySpace().addTo(root);
        errTitle = new Label("title required").addTo(root).setForegroundColor(TextColor.ANSI.RED);

        new Label("Description:").addTo(root);
        txtDescription = new TextBox(new TerminalSize(50, 4)).addTo(root)
                .setValidationPattern(Pattern.compile("[a-z A-Z][a-z A-Z\\d]{0,200}"));

        new EmptySpace().addTo(root);
        new EmptySpace().addTo(root);

        new Label("Public:").addTo(root);
        isPublic = new CheckBox().addTo(root);

        new EmptySpace().addTo(root);
        new EmptySpace().addTo(root);

        Panel buttons = new Panel().addTo(root).setLayoutManager(new LinearLayout(Direction.HORIZONTAL));

        btnAddUpdate = new Button(form == null ? "Create" : "Update", this::add).addTo(buttons).setEnabled(false);
        txtTitle.setTextChangeListener((txtTitle, byUser) -> validate());
        new Button("Cancel", this::close).addTo(buttons);

        setComponent(root);

        if (form != null) {
            txtTitle.setText(form.getTitle());
            txtDescription.setText(form.getDescription());
        }
    }

    private void add() {
        controller.save(
                txtTitle.getText(),
                txtDescription.getText()
        );
    }

    private void validate() {
        var errors = controller.validate(
                txtTitle.getText(),
                txtDescription.getText()
        );

        errTitle.setText(errors.getFirstErrorMessage(Form.Fields.Title));

        btnAddUpdate.setEnabled(errors.isEmpty());
    }


}package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
        import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.gui2.CheckBox;
import tgpr.forms.controller.AddEditFormController;
import tgpr.forms.model.Form;

import java.util.List;
import java.util.regex.Pattern;

public class AddEditFormView extends DialogWindow {
    private final AddEditFormController controller;
    private final Form form;
    private final TextBox txtTitle;
    private final TextBox txtDescription;
    private final Label errTitle;
    private final CheckBox isPublic;
    private final Button btnAddUpdate;

    public AddEditFormView(AddEditFormController controller, Form form) {
        super((form == null ? "Add" : "Edit ") + " a form");

        this.form = form;
        this.controller = controller;

        setHints(List.of(Hint.CENTERED, Hint.FIXED_SIZE));
        setCloseWindowWithEscape(true);
        setFixedSize(new TerminalSize(70, 11));

        Panel root = new Panel();
        root.setLayoutManager(new GridLayout(2).setTopMarginSize(1));

        new Label("Title:").addTo(root);
        txtTitle = new TextBox(new TerminalSize(20, 1)).addTo(root)
                .setValidationPattern(Pattern.compile("[a-z A-Z][a-z A-Z\\d]{0,25}"))
                .setTextChangeListener((txt, byUser) -> validate())
                .setReadOnly(form != null);
        new EmptySpace().addTo(root);
        errTitle = new Label("title required").addTo(root).setForegroundColor(TextColor.ANSI.RED);

        new Label("Description:").addTo(root);
        txtDescription = new TextBox(new TerminalSize(50, 4)).addTo(root)
                .setValidationPattern(Pattern.compile("[a-z A-Z][a-z A-Z\\d]{0,200}"));

        new EmptySpace().addTo(root);
        new EmptySpace().addTo(root);

        new Label("Public:").addTo(root);
        isPublic = new CheckBox().addTo(root);

        new EmptySpace().addTo(root);
        new EmptySpace().addTo(root);

        Panel buttons = new Panel().addTo(root).setLayoutManager(new LinearLayout(Direction.HORIZONTAL));

        btnAddUpdate = new Button(form == null ? "Create" : "Update", this::add).addTo(buttons).setEnabled(false);
        txtTitle.setTextChangeListener((txtTitle, byUser) -> validate());
        new Button("Cancel", this::close).addTo(buttons);

        setComponent(root);

        if (form != null) {
            txtTitle.setText(form.getTitle());
            txtDescription.setText(form.getDescription());
        }
    }

    private void add() {
        controller.save(
                txtTitle.getText(),
                txtDescription.getText()
        );
    }

    private void validate() {
        var errors = controller.validate(
                txtTitle.getText(),
                txtDescription.getText()
        );

        errTitle.setText(errors.getFirstErrorMessage(Form.Fields.Title));

        btnAddUpdate.setEnabled(errors.isEmpty());
    }


}