package tgpr.forms.controller;

import tgpr.forms.view.SignupView;
import tgpr.framework.Controller;

public class SignupController extends Controller<SignupView> {
    private final SignupView view;


    public SignupController() {
        this.view = new SignupView(this);
    }

    public SignupView getView() {
        return view;
    }

    public void close(){
        System.exit(0);
    }

    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public void onMailChanged(String email) {
        if (!isValidEmail(email)) {
            view.setMailErrorMessage("Not a valid email");
        } else {
            view.setMailErrorMessage(null);  // Efface le message d'erreur si l'email est valide
        }
    }
}


