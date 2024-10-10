package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.forms.controller.AddEditFormController;
import tgpr.forms.model.Form;

import java.util.List;

public class AddEditFormView extends DialogWindow {
    private final AddEditFormController controller;
    private final Form form;

    public AddEditFormView(AddEditFormController controller, Form form) {
        super((form == null ? "Add" : "Edit ") + "Form");

        this.form = form;
        this.controller = controller;

        setHints(List.of(Hint.CENTERED, Hint.FIXED_SIZE));
        setCloseWindowWithEscape(true);
        setFixedSize(new TerminalSize(70, 15));
    }
}
