package tgpr.forms.controller;

import tgpr.forms.model.FormValidator;
import tgpr.forms.model.User;
import tgpr.forms.view.AddEditFormView;
import tgpr.framework.Controller;
import tgpr.forms.model.Form;
import tgpr.framework.ErrorList;


public class AddEditFormController extends Controller<AddEditFormView> {

    private AddEditFormView view;
    private Form form;
    private User owner;


    public AddEditFormController(User owner, Form form) {
        this.owner = owner;
        view = new AddEditFormView(this, owner, form);
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
            form = new Form(title, description, owner, isPublic);
            form.save();
            view.close();
        }
    }

    public ErrorList validate(String title, String description) {
        var errors = new ErrorList();

        if (form == null) {
            errors.add(FormValidator.isValidAvailableTitle(title), Form.Fields.Title);
            errors.add(FormValidator.isValidDescription(description), Form.Fields.Description);
        }
        return errors;
    }

    public Form update() {
        var controller = new AddEditFormController(owner, form);
        navigateTo(controller);
        return controller.form;
    }
}