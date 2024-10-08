package tgpr.forms.controller;

import tgpr.forms.FormsApp;
import tgpr.forms.model.Security;
import tgpr.forms.view.LoginView;
import tgpr.framework.Controller;
import tgpr.framework.ErrorList;
import tgpr.framework.Model;

public class LoginController extends Controller<LoginView> {
    public void exit() {
        System.exit(0);
    }

    public List<Error> login(String pseudo, String password) {
        var errors = new ErrorList();
        errors.add(MemberValidator.isValidPseudo(pseudo));
        errors.add(MemberValidator.isValidPassword(password));

        if (errors.isEmpty()) {
            var member = Member.checkCredentials(pseudo, password);
            if (member != null) {
                Security.login(member);
                navigateTo(new MemberListController());
            } else
                showError(new Error("invalid credentials"));
        } else
            showErrors(errors);

        return errors;
    }

    public void seedData() {
        Model.seedData(FormsApp.DATABASE_SCRIPT_FILE);
    }

    @Override
    public LoginView getView() {
        return new LoginView(this);
    }
}
