package tgpr.forms.view;


import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.forms.model.*;


import tgpr.forms.controller.EditInstanceController;

import java.time.LocalDateTime;
import java.util.List;


public class EditInstanceView extends DialogWindow {
    private EditInstanceController controller;
    private Panel mainPanel;

    public EditInstanceView(EditInstanceController controller) {
        super("Answer a form");
        this.controller = controller;
        RequestConfirmation();
    }

    private void RequestConfirmation() {
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(55, 5));

        Label textLabel = new Label("You Have already answered this form.\nYou can view your submission or submit again.\nWhat would you like to do?");
        mainPanel.addComponent(textLabel);

        mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)), LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("view submission", this::buttonViewSubmission));
        buttonPanel.addComponent(new Button("submit again", this::SubmitAgain));
        buttonPanel.addComponent(new Button("cancel", this::close));

        setHints(List.of(Hint.CENTERED));

        buttonPanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        mainPanel.addComponent(buttonPanel);

        Panel container = new Panel(new LinearLayout(Direction.HORIZONTAL));
        container.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        container.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        container.addComponent(mainPanel);
        container.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        setComponent(container);
    }

    private void buttonViewSubmission() {
    }

    private void SubmitAgain() {
        AnswerForm();
    }

    private void buttonCancel() {
    }
}
