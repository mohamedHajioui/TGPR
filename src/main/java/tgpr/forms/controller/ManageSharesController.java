package tgpr.forms.controller;

import tgpr.forms.model.Form;
import tgpr.forms.view.ManageSharesView;
import tgpr.framework.Controller;

public class ManageSharesController extends Controller<ManageSharesView> {
    private final Form form;
    private ManageSharesView view;

    public ManageSharesController(Form form) {
        this.form = form;
        this.view = new ManageSharesView(this,form);
    }

    @Override
    public ManageSharesView getView() {
        return view ;
    }
}
