package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import tgpr.forms.controller.ViewInstancesController;
import tgpr.forms.model.Form;
import tgpr.forms.model.Instance;
import tgpr.forms.model.User;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;



import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;


public class ViewInstancesView extends DialogWindow {
    private ViewInstancesController controller;
    private Panel mainPanel;
    private Table<String> instancesTable;
    private int idForm = 15;

    public ViewInstancesView(ViewInstancesController controller) {
        super("Titre par defaut");
        this.controller = controller;
        ListInstancesSubmitted();
        addDeleteKeyListener(); // Add Delete key listener
    }

    private void setViewTitle(String title) {
        setTitle(title); // Method to change the title dynamically
    }

    private void ListInstancesSubmitted() {
        Form dataForm = Form.getByKey(idForm);
        setViewTitle("List of Submitted Instances");
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(60, 20));

        // Create and add main labels (Title, Description, and Date)
        Label titleLabel = new Label("Title: " + dataForm.getTitle());
        Label descriptionLabel = new Label("Description: " + dataForm.getDescription());
        mainPanel.addComponent(titleLabel);
        mainPanel.addComponent(descriptionLabel);
        mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)), LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));

        // Create table with columns "ID", "User Name", "Date"
        instancesTable = new Table<>("ID", "User Name", "Date");
        instancesTable.setPreferredSize(new TerminalSize(50, 10));

        Form form = new Form();
        form.setId(idForm);
        List<Instance> completedInstances = form.getCompletedInstances();
        instancesTable.getTableModel().clear();

        for (Instance instance : completedInstances) {
            String id = String.valueOf(instance.getId());
            User user = User.getByKey(instance.getUserId());
            String userName = user.getName();
            String completedDate = instance.getCompleted().toString();

            instancesTable.getTableModel().addRow(id, userName, completedDate);
        }
        mainPanel.addComponent(instancesTable);

        mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)), LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("Delete Selected", this::confirmDeleteSelected));
        buttonPanel.addComponent(new Button("Delete All", this::ButtonDeleteAll));
        buttonPanel.addComponent(new Button("Close", this::close));
        Button openButton = new Button("Open");

        int selectedRow = instancesTable.getSelectedRow();
        User user;
        if (selectedRow >= 0) {
            String id = instancesTable.getTableModel().getCell(0, selectedRow);

            Instance instance = Instance.getByKey(Integer.parseInt(id));
            user = User.getByKey(instance.getUserId());
        } else {
            user = null;
        }


        buttonPanel.addComponent(openButton);
        buttonPanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        mainPanel.addComponent(buttonPanel);

        Panel container = new Panel(new LinearLayout(Direction.HORIZONTAL));
        container.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        container.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        container.addComponent(mainPanel);
        container.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        setComponent(container);
        setHints(List.of(Hint.CENTERED));
    }


}