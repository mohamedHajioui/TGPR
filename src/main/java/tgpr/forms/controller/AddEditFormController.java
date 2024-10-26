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

    public ErrorList validate(String title, String description, boolean isPublic) {
        var errors = new ErrorList();

        var titleError = FormValidator.isValidAvailableTitle(title, owner, form != null ? form : new Form());
        if (titleError != null) {
            errors.add(titleError, Form.Fields.Title);
        }
        var descriptionError = FormValidator.isValidDescription(description);
        if (descriptionError != null) {
            errors.add(descriptionError, Form.Fields.Description);
        }
        return errors;
    }

    public void addForm(String title, String description, boolean isPublic) {
        var errors = validate(title, description, isPublic);
        if (errors.isEmpty()) {
            form = new Form(title, description, owner, isPublic);
            form.save();
            view.close();
        }
    }

    public void updateForm(String title, String description, boolean isPublic) {
        if (form != null) {
            var errors = validate(title, description, isPublic);
            if (errors.isEmpty()) {
                if(!form.getIsPublic() && isPublic) {
                    if (askConfirmation("Are you sure you want to make this form public?\n" +
                            "This will delete all existing shares.", "Confirmation")) {

                        List<UserFormAccess> userAccesses = form.getUserFormAccesses();
                        List<DistListFormAccess> distListAccesses = form.getDistListFormAccesses();

                        userAccesses.removeIf(access -> access.getAccessType() == AccessType.User);
                        distListAccesses.removeIf(access -> access.getAccessType() == AccessType.User);

                        for (UserFormAccess access : userAccesses) {
                            access.delete();
                        }
                        for (DistListFormAccess access : distListAccesses) {
                            access.delete();
                        }

                        form.setIsPublic(isPublic);
                    } else {
                        return;
                    }
                }
                form.setTitle(title);
                form.setDescription(description);
                form.setIsPublic(isPublic);
                form.save();
                view.close();
            }
        }
    }
}