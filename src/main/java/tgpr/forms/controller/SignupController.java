package tgpr.forms.controller;

import tgpr.forms.view.SignupView;
import tgpr.framework.Controller;

import java.util.regex.Pattern;

public class SignupController extends Controller<SignupView> {
    private final SignupView view;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Z])(?=.*[0-9])(?=.*\\W).{8,}$");


    public SignupController() {
        this.view = new SignupView(this);
    }

    public SignupView getView() {
        return view;
    }

    public void close(){
        System.exit(0);
    }

    public void isValidEmail(String email) {
        if (email.isBlank()){
            view.setMailErrorMessage("");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            view.setMailErrorMessage("                    Invalid mail");
        } else {
            view.setMailErrorMessage("");
        }
    }


    public void isValidName(String name) {
        if (name.isBlank()){
            view.setNameErrorMessage("");
        } else if (name.length() < 3) {
            view.setNameErrorMessage("                    Minimum 3 chars");
        } else {
            view.setNameErrorMessage("");
        }
    }

    public void isValidPassword(String password) {
        if (password.isEmpty()){
            view.setPasswordErrorMessage("");
        } else {
            if (password.length() < 8) {
                view.setPasswordErrorMessage("                    Minimum 8 chars");
            } else if (!password.matches(".*\\d.*")) {
                view.setPasswordErrorMessage("                    Minimum 1 digit.");
            } else if (!password.matches(".*[A-Z].*")) {
                view.setPasswordErrorMessage("                    Minimum 1 uppercase letter.");
            } else if (!password.matches(".*[^a-zA-Z0-9].*")) {
                view.setPasswordErrorMessage("                    Minimum 1 special character.");
            } else {
                view.setPasswordErrorMessage("");  // Mot de passe valide
            }
        }
    }




}


