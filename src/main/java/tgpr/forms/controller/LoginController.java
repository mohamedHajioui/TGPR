package tgpr.forms.controller;

import tgpr.forms.FormsApp;
import tgpr.forms.model.Security;
import tgpr.forms.model.User;
import tgpr.forms.model.UserFormAccess;
import tgpr.forms.model.UserValidator;

import tgpr.forms.view.LoginView;
import tgpr.framework.*;
import tgpr.framework.Error;

import java.util.List;

public class LoginController extends Controller<LoginView> {
    public void exit() {
        System.exit(0);
    }

    public List<Error> login(String mail, String password) {
        var errors = new ErrorList();
        errors.add(UserValidator.isValidMail(mail));
        errors.add(UserValidator.isValidPassword(password));

        if (errors.isEmpty()) {
            var member = User.checkCredentials(mail, password);
            if (member != null) {
                Security.login(member);
                navigateTo(new ViewFormsController(member));
            } else
                showError(new Error("invalid credentials"));
        } else
            showErrors(errors);

        return errors;
    }

    public void loginAsGuest() {
        var guestUser = User.getByEmail(Configuration.get("default.user.mail"));
        if (guestUser != null) {
            guestUser.setRole(User.Role.Guest); // Assure que le rôle est défini sur Guest
            Security.login(guestUser);
            navigateToGuestView(guestUser); // Redirige vers une vue limitée pour les guests
        } else {
            showError(new Error("Guest account not found"));
        }
    }

    // Méthode pour naviguer vers la vue guest
    private void navigateToGuestView(User guestUser) {
        navigateTo(new ViewFormsController(guestUser));
    }
    public void navigateToSignup() {
        navigateTo(new SignupController());
    }


    public void seedData() {
        Model.seedData(FormsApp.DATABASE_SCRIPT_FILE);
    }

    @Override
    public LoginView getView() {
        return new LoginView(this);
    }
}
