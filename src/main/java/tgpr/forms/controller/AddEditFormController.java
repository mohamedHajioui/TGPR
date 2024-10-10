package tgpr.forms.controller;

import tgpr.forms.view.AddEditFormView;
import tgpr.framework.Controller;
import tgpr.forms.model.Form;

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
}
