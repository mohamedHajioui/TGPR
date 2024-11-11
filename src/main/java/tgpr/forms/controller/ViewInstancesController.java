package tgpr.forms.controller;



import tgpr.forms.model.Form;
import tgpr.forms.view.ViewInstancesView;
import tgpr.framework.Controller;

public class ViewInstancesController extends Controller<ViewInstancesView> {

    private ViewInstancesView view;
    private Form currentForm;

    public ViewInstancesController(Form currentForm) {
        this.view = new ViewInstancesView(this);
    }

    public Form getCurrentForm() {

        return this.currentForm;
    }

    @Override
    public ViewInstancesView getView() {
        return view;
    }
}
