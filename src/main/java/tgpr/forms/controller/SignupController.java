package tgpr.forms.controller;

import com.googlecode.lanterna.gui2.TextBox;
import tgpr.forms.model.Security;
import tgpr.forms.model.User;
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

    public void close() {
        System.exit(0);
    }

    public void isValidEmail(String email) {
        if (email.isBlank()) {
            view.setMailErrorMessage("                    Mail is required");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            view.setMailErrorMessage("                    Invalid mail");
        } else {
            view.setMailErrorMessage("");
        }
        validateOtherFields();
    }

    public void isValidName(String name) {
        if (name.isBlank()) {
            view.setNameErrorMessage("                    Name is required");
        } else if (name.length() < 3) {
            view.setNameErrorMessage("                    Minimum 3 chars");
        } else {
            view.setNameErrorMessage("");
        }
        validateOtherFields();
    }

    public void isValidPassword(String password) {
        if (password.isBlank()) {
            view.setPasswordErrorMessage("                    Password is required");
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
        validateConfirmPassword();
        validateOtherFields();
    }

    public void isValidConfirmPassword(String confirmPassword) {
        if (confirmPassword.isBlank()) {
            view.setConfirmPasswordErrorMessage("                    Confirm password is required");
        } else {
            validateConfirmPassword();
        }
    }

    private void validateConfirmPassword() {
        String password = view.getPasswordText();
        String confirmPassword = view.getConfirmPasswordText();

        if (!confirmPassword.equals(password)) {
            view.setConfirmPasswordErrorMessage("                    Passwords do not match");
        } else {
            view.setConfirmPasswordErrorMessage("");
        }
    }

    // Méthode pour valider les autres champs
    private void validateOtherFields() {
        if (view.getEmailText().isBlank()) {
            view.setMailErrorMessage("                    Mail is required");
        }
        if (view.getFullNameText().isBlank()) {
            view.setNameErrorMessage("                    Name is required");
        }
        if (view.getPasswordText().isBlank()) {
            view.setPasswordErrorMessage("                    Password is required");
        }
        if (view.getConfirmPasswordText().isBlank()) {
            view.setConfirmPasswordErrorMessage("                    Confirm is required");
        }
    }

    public void signup(){
        User newUser = new User();
        newUser.setRole(User.Role.User);
        newUser.setEmail(view.getEmailText());
        newUser.setFullName(view.getFullNameText());
        newUser.setPassword(view.getPasswordText());
        newUser.save();
        Security.login(newUser);
        navigateTo(new ViewFormsController(newUser));
    }
}








