package tgpr.forms.controller;

import tgpr.forms.model.FormValidator;
import tgpr.forms.model.User;
import tgpr.forms.view.AddEditFormView;
import tgpr.framework.Controller;
import tgpr.forms.model.Form;
import tgpr.framework.ErrorList;


public class AddEditFormController extends Controller<AddEditFormView> {

    private final AddEditFormView view;
    private Form form;
    private User owner;

    public AddEditFormController(User owner, Form form) {
        this.owner = owner;
        this.form = form;
        view = new AddEditFormView(this, owner, form);
    }

    @Override
    public AddEditFormView getView() {
        return view;
    }

    public Form getForm() {
        return form;
    }

    public ErrorList validate(String title, String description) {
        var errors = new ErrorList();

        if (form == null) {
            errors.add(FormValidator.isValidTitle(title, owner), Form.Fields.Title);
            errors.add(FormValidator.isValidDescription(description), Form.Fields.Description);
        }
        return errors;
    }

    public void addForm(String title, String description, boolean isPublic) {
        var errors = validate(title, description);
        if (errors.isEmpty()) {
            form = new Form(title, description, owner, isPublic);
            form.save();
            view.close();
        }
    }

    public void updateForm(String title, String description, boolean isPublic) {
        if (form != null) {
            var errors = validate(title, description);
            if (errors.isEmpty()) {
                form.setTitle(title);
                form.setDescription(description);
                form.setIsPublic(isPublic);
                form.save();
                view.close();
            }
        }
    }
}