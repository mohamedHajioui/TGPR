package tgpr.forms.view;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import tgpr.forms.controller.SignupController;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;

import java.util.List;

public class SignupView extends DialogWindow {
    private final SignupController controller;
    private Panel mainPanel;
    private Label errorLabelMail;
    private Label errorLabelName;
    private Label errorLabelPassword;
    private TextBox emailBox;
    private TextBox fullNameBox;
    private TextBox passwordBox;
    private TextBox confirmPasswordBox;
    private Label errorLabelConfirmPassword;
    private Button signupButton;


    public SignupView(SignupController controller) {
        super("Signup");
        this.controller = controller;

        //creation du panel
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(55, 12));
        setHints(List.of(Hint.CENTERED));
        setComponent(mainPanel);
        mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));


        mail(controller);
        fullName();
        password();
        confirmPassword();
        buttonsSignupAndClose();
    }

    private void mail(SignupController controller) {
        errorLabelMail = new Label("").setForegroundColor(TextColor.ANSI.RED);
        errorLabelMail.setPreferredSize(new TerminalSize(40, 1));
        Panel mailPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Label mailLabel = new Label(" Mail:             ");
        mailPanel.addComponent(mailLabel);
        emailBox = new TextBox(new TerminalSize(22, 1));
        mailPanel.addComponent(emailBox);
        mainPanel.addComponent(mailPanel);
        emailBox.setTextChangeListener((newText, changedByUser) -> controller.isValidEmail(newText));
        mainPanel.addComponent(errorLabelMail);
    }

    private void fullName() {
        errorLabelName = new Label("").setForegroundColor(TextColor.ANSI.RED);
        errorLabelName.setPreferredSize(new TerminalSize(40, 1));
        Panel fullNamePanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Label fullNameLabel = new Label(" Full Name:        ");
        fullNamePanel.addComponent(fullNameLabel);
        fullNameBox = new TextBox(new TerminalSize(40, 1));
        fullNamePanel.addComponent(fullNameBox);
        mainPanel.addComponent(fullNamePanel);
        fullNameBox.setTextChangeListener((newText, changedByUser) -> controller.isValidName(newText));
        mainPanel.addComponent(errorLabelName);
    }

    private void password() {
        errorLabelPassword = new Label("").setForegroundColor(TextColor.ANSI.RED);
        errorLabelPassword.setPreferredSize(new TerminalSize(40, 1));
        Panel passwordPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Label passwordLabel = new Label(" Password:         ");
        passwordPanel.addComponent(passwordLabel);
        passwordBox = new TextBox(new TerminalSize(22, 1));
        passwordBox.setMask('*');
        passwordPanel.addComponent(passwordBox);
        mainPanel.addComponent(passwordPanel);
        passwordBox.setTextChangeListener((newText, changedByUser) -> controller.isValidPassword(newText));
        mainPanel.addComponent(errorLabelPassword);
    }

    private void buttonsSignupAndClose() {
        Panel buttonsPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        signupButton = new Button("Signup", () -> controller.signup());
        signupButton.setEnabled(false);
        buttonsPanel.addComponent(signupButton);
        Button closeButton = new Button("Close", this::close);
        buttonsPanel.addComponent(closeButton);
        buttonsPanel.center();
        mainPanel.addComponent(buttonsPanel);
    }

    private void confirmPassword() {
        errorLabelConfirmPassword = new Label("").setForegroundColor(TextColor.ANSI.RED);
        errorLabelConfirmPassword.setPreferredSize(new TerminalSize(40, 1));
        Panel confirmPasswordPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Label confirmPasswordLabel = new Label(" Confirm Password: ");
        confirmPasswordPanel.addComponent(confirmPasswordLabel);
        confirmPasswordBox = new TextBox(new TerminalSize(22, 1));
        confirmPasswordBox.setMask('*');
        confirmPasswordPanel.addComponent(confirmPasswordBox);
        mainPanel.addComponent(confirmPasswordPanel);
        confirmPasswordBox.setTextChangeListener((newText, changedByUser) -> controller.isValidConfirmPassword(newText));
        mainPanel.addComponent(errorLabelConfirmPassword);
        mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 10)));
    }


    public void setSignupButtonEnabled(boolean enabled) {
        signupButton.setEnabled(enabled);
    }


    public void setMailErrorMessage(String message) {
        errorLabelMail.setText(message != null ? message : "");
    }

    public void setNameErrorMessage(String message) {
        errorLabelName.setText(message != null ? message : "");
    }

    public void setPasswordErrorMessage(String password){
        errorLabelPassword.setText(password != null ? password : "");
    }

    public void setConfirmPasswordErrorMessage(String password){
        errorLabelConfirmPassword.setText(password != null ? password : "");
    }

    public String getEmailText() {
        return emailBox.getText();
    }

    public String getFullNameText() {
        return fullNameBox.getText();
    }

    public String getPasswordText() {
        return passwordBox.getText();
    }

    public String getConfirmPasswordText() {
        return confirmPasswordBox.getText();
    }

    public String getMailErrorMessage() {
        return errorLabelMail.getText();
    }

    public String getNameErrorMessage() {
        return errorLabelName.getText();
    }

    public String getPasswordErrorMessage() {
        return errorLabelPassword.getText();
    }

    public String getConfirmPasswordErrorMessage() {
        return errorLabelConfirmPassword.getText();
    }

}