package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.gui2.table.Table;
import tgpr.forms.controller.ViewInstancesController;

import java.time.LocalDateTime;
import java.util.List;

public class ViewInstancesView extends DialogWindow {
    private ViewInstancesController controller;
    private Panel mainPanel;
    private Table<String> instancesTable;

    public ViewInstancesView(ViewInstancesController controller) {
        super("Titre par defaut");
        this.controller = controller;
        ListInstancesSubmitted();
    }

    private void setViewTitle(String title) {
        setTitle(title); // Method to change the title dynamically
    }

    private void ListInstancesSubmitted() {
        setViewTitle("List of Submitted Instances"); // Title for RequestConfirmation
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(60, 20)); // Set preferred size for the panel

        // Create and add main labels (Title, Description, and Date)
        Label titleLabel = new Label("Title: ");
        Label descriptionLabel = new Label("Description: ");
        mainPanel.addComponent(titleLabel);
        mainPanel.addComponent(descriptionLabel);
        mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)), LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));

        // Create table with columns "ID", "User Name", "Date"
        instancesTable = new Table<>("ID", "User Name", "Date");
        instancesTable.setPreferredSize(new TerminalSize(50, 10)); // Set preferred size for the table

        // Example data (you can replace this with real data from the controller)
        instancesTable.getTableModel().addRow("1", "Alice", LocalDateTime.now().toString());
        instancesTable.getTableModel().addRow("2", "Bob", LocalDateTime.now().minusDays(1).toString());
        instancesTable.getTableModel().addRow("3", "Charlie", LocalDateTime.now().minusDays(2).toString());

        mainPanel.addComponent(instancesTable); // Add the table to the main panel

        // Add empty space after the table
        mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)), LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));

        // Create button panel and add buttons
        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("Delete Selected", this::ButtonDeleteSelected));
        buttonPanel.addComponent(new Button("Delete All", this::ButtonDeleteAll));
        buttonPanel.addComponent(new Button("Close", this::close));
        buttonPanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        mainPanel.addComponent(buttonPanel);

        // Add main panel to the container
        Panel container = new Panel(new LinearLayout(Direction.HORIZONTAL));
        container.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        container.addComponent(new EmptySpace(new TerminalSize(0, 1))); // Empty space before
        container.addComponent(mainPanel);
        container.addComponent(new EmptySpace(new TerminalSize(0, 1))); // Empty space after

        // Set the container panel as the main component of the window
        setComponent(container);
        setHints(List.of(Hint.CENTERED));
    }

    private void ButtonDeleteSelected() {
        if (instancesTable.getSelectedRow() >= 0) {
            String id = instancesTable.getTableModel().getCell(0, instancesTable.getSelectedRow());
            // Add logic to delete the selected instance by ID
            System.out.println("Delete instance with ID: " + id);
            // Refresh or update table if needed
        }
    }

    private void ButtonDeleteAll() {
        ConfirmationDeleteAll();
    }

    private void ConfirmationDeleteSelected() {
        setViewTitle("Delete Instance");
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(45, 5)); // Set preferred size for the panel

        Label textLabel = new Label("Are you sure you want to delete this instance?");
        mainPanel.addComponent(textLabel);

        mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)), LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("Yes", this::ButtonConfirmYes));
        buttonPanel.addComponent(new Button("No", this::ButtonConfirmNo));

        mainPanel.addComponent(buttonPanel);
        setComponent(mainPanel);
    }

    private void ButtonConfirmYes() {
        // Logic to confirm delete
    }

    private void ButtonConfirmNo() {
        close();
    }

    private void ConfirmationDeleteAll() {
        setViewTitle("Delete All Instances");
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(90, 4));

        Label textLabel = new Label("Are you sure you want to delete all the submitted instances?\nNote: This will not delete instances that are currently being edited (not submitted).");
        mainPanel.addComponent(textLabel);

        mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)), LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("Yes", this::ButtonDeleteAllYes));
        buttonPanel.addComponent(new Button("No", this::ButtonDeleteAllNo));

        mainPanel.addComponent(buttonPanel);
        setComponent(mainPanel);
    }

    private void ButtonDeleteAllYes() {
        // Logic to delete all instances
    }

    private void ButtonDeleteAllNo() {
        close();
    }
}