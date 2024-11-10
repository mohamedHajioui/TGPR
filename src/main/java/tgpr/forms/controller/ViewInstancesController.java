package tgpr.forms.controller;

import tgpr.forms.controller.EditInstanceController;
import tgpr.forms.model.Form;
import tgpr.forms.model.User;
import tgpr.forms.view.ViewInstancesView;
import tgpr.framework.Controller;

public class ViewInstancesController extends Controller<ViewInstancesView> {

    private ViewInstancesView view;

    public ViewInstancesController() {
        this.view = new ViewInstancesView(this);
    }



    @Override
    public ViewInstancesView getView() {
        return view;
    }
}
