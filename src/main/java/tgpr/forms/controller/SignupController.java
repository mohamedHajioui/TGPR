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
}


