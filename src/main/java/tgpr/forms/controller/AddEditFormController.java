package tgpr.forms.controller;

import com.googlecode.lanterna.gui2.CheckBox;
import tgpr.forms.model.FormValidator;
import tgpr.forms.view.AddEditFormView;
import tgpr.framework.Controller;
import tgpr.forms.model.Form;
import tgpr.framework.ErrorList;

import static tgpr.framework.Tools.hash;
import static tgpr.framework.Tools.toDate;

public class AddEditFormController extends Controller<AddEditFormView> {

    private final AddEditFormView view;
    private Form form;
    private final boolean isNew;

    public AddEditFormController() {
        this(null);
    }

    public AddEditFormController(Form form) {
        this.form = form;
        isNew = form == null;
        view = new AddEditFormView(this, form);
    }

    @Override
    public AddEditFormView getView() {
        return view;
    }

    public Form getForm() {
        return form;
    }

    public void save(String title, String description, boolean isPublic) {
        var errors = validate(title, description);
        if (errors.isEmpty()) {
            form = new Form();
            form.save();
            view.close();
        }
    }

    public ErrorList validate(String title, String description) {
        var errors = new ErrorList();

        if (isNew) {
            errors.add(FormValidator.isValidAvailableTitle(title), Form.Fields.Title);
            errors.add(FormValidator.isValidDescription(description), Form.Fields.Description);
        }
        return errors;
    }

    public Form update() {
        var controller = new AddEditFormController(form);
        navigateTo(controller);
        return controller.form;
    }
}