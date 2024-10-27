package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import org.slf4j.helpers.LegacyAbstractLogger;
import tgpr.forms.controller.AnalyseController;
import tgpr.forms.model.Form;

import java.util.List;

public class AnalyseView extends DialogWindow {
    private final AnalyseController controller;
    private Panel mainPanel;

    public AnalyseView(AnalyseController controller, Form currentForm) {
        super("Statistical Analysis of Submitted Instances");
        this.controller = controller;

        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(90, 23));
        setHints(List.of(Hint.CENTERED));
        setComponent(mainPanel);

        mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        Panel titlePanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Label titleLabel = new Label("Title:                        ");
        Label titleForm = new Label(currentForm.getTitle());
        titlePanel.addComponent(titleLabel);
        titlePanel.addComponent(titleForm);
        mainPanel.addComponent(titlePanel);


        Panel descriptionPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Label description = new Label("Description:                  ");
        descriptionPanel.addComponent(description);
        Label descriptionForm = new Label(currentForm.getDescription());
        descriptionPanel.addComponent(descriptionForm);
        mainPanel.addComponent(descriptionPanel);
        Panel instancesPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Label nbInstances = new Label("Number of Submitted Instances: " + controller.getSubmittedInstancesCount());

        mainPanel.addComponent(nbInstances);

    }
}
