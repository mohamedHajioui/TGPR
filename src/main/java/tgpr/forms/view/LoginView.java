package tgpr.forms.view;

import com.googlecode.lanterna.gui2.*;
import tgpr.framework.Configuration;
import tgpr.forms.controller.LoginController;

import java.util.List;

public class LoginView extends BasicWindow {

    private final LoginController controller;
    private final TextBox txtMail;
    private final TextBox txtPassword;
    private final Button btnLogin;

    private final Button btnExit;
    private final Button btnLoginAsGuest;


    public LoginView(LoginController controller) {
        this.controller = controller;

        setTitle("Login");
        setHints(List.of(Hint.CENTERED));

        Panel root = new Panel();
        setComponent(root);

        Panel panel = new Panel().setLayoutManager(new GridLayout(2).setTopMarginSize(1).setVerticalSpacing(1))
                .setLayoutData(Layouts.LINEAR_CENTER).addTo(root);
        panel.addComponent(new Label("Mail :"));
        txtMail = new TextBox().addTo(panel);
        panel.addComponent(new Label("Password:"));
        txtPassword = new TextBox().setMask('*').addTo(panel);

        new EmptySpace().addTo(root);

        Panel buttons = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL))
                .setLayoutData(Layouts.LINEAR_CENTER).addTo(root);
        btnLogin = new Button("Login", this::login).addTo(buttons);
        //btnSignup = new Button("signup", this::signup).addTo(buttons);
        btnExit = new Button("Exit", this::exit).addTo(buttons);
        btnLoginAsGuest = new Button("Login as Guest", this::loginAsGuest).addTo(buttons);
        btnLoginAsGuest.addTo(buttons);


        new EmptySpace().addTo(root);

        Button btnSeedData = new Button("Reset Database", this::seedData);
        Panel debug = Panel.verticalPanel(LinearLayout.Alignment.Center,
                new Button("Login as default admin", this::logAsDefaultAdmin),
                btnSeedData
        );
        debug.withBorder(Borders.singleLine(" For debug purpose ")).addTo(root);

        txtMail.takeFocus();
    }

    private void seedData() {
        controller.seedData();
        btnLogin.takeFocus();
    }

    private void exit() {
        controller.exit();
    }

    private void login() {
        var errors = controller.login(txtMail.getText(), txtPassword.getText());
        if (!errors.isEmpty()) {
            txtMail.takeFocus();
        }
    }

    private void logAsDefaultAdmin() {
        controller.login(Configuration.get("default.admin.mail"), Configuration.get("default.admin.password"));
    }

    private void loginAsGuest() {
        controller.login(Configuration.get("default.user.mail"), Configuration.get("default.user.password"));
    }
}
