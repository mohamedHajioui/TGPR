package tgpr.forms.controller;



import tgpr.forms.view.ViewInstanceView;
import tgpr.framework.Controller;



public class ViewInstancesController extends Controller<ViewInstanceView> {

    private ViewInstanceView view;

    public ViewInstancesController() {
        this.view = new ViewInstanceView(this);
    }

    @Override
    public ViewInstanceView getView() {
        return view;
    }
}