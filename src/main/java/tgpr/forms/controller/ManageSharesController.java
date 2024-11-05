package tgpr.forms.controller;

import tgpr.forms.model.Form;
import tgpr.forms.model.User;
import tgpr.forms.view.ManageSharesView;
import tgpr.framework.Controller;

public class ManageSharesController extends Controller<ManageSharesView> {
    private final Form form;
    private ManageSharesView view;
    private final User loggeduser;


    // Vu que Manage Shares depend du form on le demande dans les parametre
    public ManageSharesController(Form form, User loggeduser) {
        this.form = form;
        this.loggeduser = loggeduser;
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
        Controller.navigateTo(new formController(form,loggeduser));
    }
}
