package tgpr.forms.view;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import tgpr.forms.controller.LoginController;
import tgpr.forms.controller.SignupController;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.forms.controller.SignupController;
import tgpr.framework.Controller;

import java.util.List;

public class SignupView extends DialogWindow {
    private final SignupController controller;
    private Panel mainPanel;
    private final Label errorLabel;


    public SignupView(SignupController controller) {
        super("Signup");
        this.controller = controller;

        //creation du panel
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(55, 12));
        setHints(List.of(Hint.CENTERED));
        setComponent(mainPanel);
        mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

        //creation du formulaire a remplir
        mail(controller);
        errorLabel = new Label("").setForegroundColor(TextColor.ANSI.RED);
        errorLabel.setPreferredSize(new TerminalSize(40, 0));
        mainPanel.addComponent(errorLabel);
        fullName();
        password();
        confirmPassword();

        //ajout des boutons signup et close
        buttonsSignupAndClose();
    }

    private void mail(SignupController controller) {
        Panel mailPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Label mailLabel = new Label(" Mail:             ");
        mailPanel.addComponent(mailLabel);
        TextBox mailBox = new TextBox(new TerminalSize(22, 1));
        mailPanel.addComponent(mailBox);
        mainPanel.addComponent(mailPanel);
        mailBox.setTextChangeListener((newText, changedByUser) -> controller.onMailChanged(newText));
        mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
    }

    private void buttonsSignupAndClose() {
        Panel buttonsPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Button signupButton = new Button("Signup");
        buttonsPanel.addComponent(signupButton);
        Button closeButton = new Button("Close", this::close);
        buttonsPanel.addComponent(closeButton);
        buttonsPanel.center();
        mainPanel.addComponent(buttonsPanel);
    }

    private void confirmPassword() {
        Panel confirmPasswordPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Label confirmPasswordLabel = new Label(" Confirm Password: ");
        confirmPasswordPanel.addComponent(confirmPasswordLabel);
        TextBox confirmPasswordBox = new TextBox(new TerminalSize(22, 1));
        confirmPasswordPanel.addComponent(confirmPasswordBox);
        mainPanel.addComponent(confirmPasswordPanel);
        mainPanel.addComponent(new EmptySpace(new TerminalSize(3, 3)));
    }

    private void password() {
        Panel passwordPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Label passwordLabel = new Label(" Password:         ");
        passwordPanel.addComponent(passwordLabel);
        TextBox passwordBox = new TextBox(new TerminalSize(22, 1));
        passwordPanel.addComponent(passwordBox);
        mainPanel.addComponent(passwordPanel);
        mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
    }

    private void fullName() {
        Panel fullNamePanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Label fullNameLabel = new Label(" Full Name:        ");
        fullNamePanel.addComponent(fullNameLabel);
        TextBox fullNameBox = new TextBox(new TerminalSize(40, 1));
        fullNamePanel.addComponent(fullNameBox);
        mainPanel.addComponent(fullNamePanel);
        mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
    }

    public void setMailErrorMessage(String message) {
        errorLabel.setText(message != null ? message : "");
    }





}