package tgpr.forms.controller;

import tgpr.forms.model.Form;
import tgpr.forms.model.User;
import tgpr.forms.view.ViewFormsView;
import tgpr.framework.Controller;

import java.util.List;

public class ViewFormsController extends Controller<ViewFormsView> {
    private final User currentUser;
    private final ViewFormsView view;

    public ViewFormsController(User user) {
        this.currentUser = user;
        this.view = new ViewFormsView(this);
        showUserForms();
    }

    @Override
    public ViewFormsView getView() {
        return view;
    }

    public List<Form> getForms() {
        return currentUser.getForms();
    }

    public List<Form> getUserForms() {
        return currentUser.getForms();
    }

    public void showUserForms(){
        List<Form> forms = getUserForms();
        view.displayForms(forms);
    }




}