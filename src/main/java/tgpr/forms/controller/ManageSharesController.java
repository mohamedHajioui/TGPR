package tgpr.forms.controller;

import tgpr.forms.model.Form;
import tgpr.forms.view.ManageSharesView;
import tgpr.framework.Controller;

public class ManageSharesController extends Controller<ManageSharesView> {
    private final Form form;
    private ManageSharesView view;


    // Vu que Manage Shares depend du form on le demande dans les parametre
    public ManageSharesController(Form form) {
        this.form = form;
        this.view = new ManageSharesView(this,form);
    }


    //On charge de la vue
    @Override
    public ManageSharesView getView() {
        return view ;
    }


    //le mediateur vers la view normal de controller
    public void versForm(){
        view.close();
        Controller.navigateTo(new formController(form));
    }
}
