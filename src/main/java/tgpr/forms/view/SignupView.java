package tgpr.forms.view;
import com.googlecode.lanterna.TerminalSize;
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
        Label mailLabel = new Label(" Mail:");
        mainPanel.addComponent(mailLabel);
        mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

        Label fullNameLabel = new Label(" Full Name:");
        mainPanel.addComponent(fullNameLabel);
        mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

        Label passwordLabel = new Label(" Password:");
        mainPanel.addComponent(passwordLabel);
        mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

        Label confirmPasswordLabel = new Label(" Confirm Password:");
        mainPanel.addComponent(confirmPasswordLabel);
        mainPanel.addComponent(new EmptySpace(new TerminalSize(3, 3)));



        //ajout des boutons signup et close
        Panel buttonsPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Button signupButton = new Button("Signup");
        buttonsPanel.addComponent(signupButton);
        Button closeButton = new Button("Close", this::close);
        buttonsPanel.addComponent(closeButton);
        buttonsPanel.center();
        mainPanel.addComponent(buttonsPanel);



    }




}
