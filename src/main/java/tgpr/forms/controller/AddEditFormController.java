package tgpr.forms.controller;

import tgpr.forms.model.*;
import tgpr.forms.view.AddEditFormView;
import tgpr.framework.Controller;
import tgpr.framework.ErrorList;
import tgpr.framework.Params;

import java.util.List;


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
                if (askConfirmation("Are you sure you want to make this form public? " +
                        "This will delete all existing shares.", "Confirmation")) {
                    form.getUserFormAccesses();
                    form.setTitle(title);
                    form.setDescription(description);
                    form.setIsPublic(isPublic);
                    form.save();
                    view.close();
                }
            }
        }
    }
}