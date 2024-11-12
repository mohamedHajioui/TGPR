package tgpr.forms.controller;



import tgpr.forms.model.Form;
import tgpr.forms.model.User;
import tgpr.forms.view.EditInstanceView;
import tgpr.forms.view.ViewInstancesView;
import tgpr.framework.Controller;

public class ViewInstancesController extends Controller<ViewInstancesView> {

    private ViewInstancesView view;
    private Form currentForm;


    public ViewInstancesController(Form currentForm) {
        this.currentForm = currentForm;
        this.view = new ViewInstancesView(this, this.currentForm.getId());
    }



    public Form getCurrentForm() {

        return currentForm;
    }

    @Override
    public ViewInstancesView getView() {
        return view;
    }
}
