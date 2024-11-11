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


    private void addDeleteKeyListener() {
        this.addWindowListener(new WindowListenerAdapter() {
            @Override
            public void onUnhandledInput(Window basePane, KeyStroke keyStroke, AtomicBoolean hasBeenHandled) {
                if (keyStroke.getKeyType() == KeyType.Delete) {
                    confirmDeleteSelected();  // Trigger delete action with confirmation
                    hasBeenHandled.set(true);
                }
            }
        });
    }

    private void confirmDeleteSelected() {
        if (instancesTable.getSelectedRow() >= 0) {
            MessageDialogButton result = MessageDialog.showMessageDialog(
                    getTextGUI(),
                    "Confirm Delete",
                    "Are you sure you want to delete the selected instance?",
                    MessageDialogButton.Yes, MessageDialogButton.No
            );

            if (result == MessageDialogButton.Yes) {
                deleteSelectedInstance();
            }
        }
    }

    private void deleteSelectedInstance() {
        int selectedRow = instancesTable.getSelectedRow();
        if (selectedRow >= 0) {
            String id = instancesTable.getTableModel().getCell(0, selectedRow);
            System.out.println("Delete instance with ID: " + id);
            Instance instanceDelete = new Instance();
            instanceDelete.setId(Integer.parseInt(id));
            instanceDelete.delete();

            instancesTable.getTableModel().removeRow(selectedRow); // Remove row from the table
        }
    }
    private void ButtonDeleteAll() {
        // Show confirmation dialog
        MessageDialogButton result = MessageDialog.showMessageDialog(
                getTextGUI(),
                "Confirm Delete All",
                "Are you sure you want to delete all submitted instances?\nThis action cannot be undone.",
                MessageDialogButton.Yes, MessageDialogButton.No
        );

        // Proceed with deletion if the user confirms
        if (result == MessageDialogButton.Yes) {
            Form form = new Form();
            form.setId(idForm);
            form.deleteAllSubmittedInstances();  // Delete all instances
            instancesTable.getTableModel().clear();  // Clear the table
        }
        // If "No" is selected, the dialog simply closes and no action is taken
    }






}