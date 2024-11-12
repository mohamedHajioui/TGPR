package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.input.KeyType;
import tgpr.forms.controller.AddEditOptionListController;
import tgpr.forms.model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import static tgpr.framework.ViewManager.getTerminalColumns;

public class AddEditOptionListView extends DialogWindow {
    private boolean normal = true;
    private boolean moving = false;
    private final AddEditOptionListController controller;
    private final User owner;
    private OptionList optionList;
    private OptionValue optionValue;
    private final Panel root;
    private final Panel namePanel;
    private Label nameLabel;
    private TextBox txtName;
    private Label errName;
    private Panel systemCheckBox;
    private CheckBox checkBoxSystem;
    private final ObjectTable<OptionValue> table;
    private final Panel addOptionPanel;
    private Label errAddOption;
    private TextBox txtAddOption;
    private Button btnAddOption;
    //private final Panel btnPanel;
    private Panel btnContainer;
    private Button btnCreate;
    private Button btnDelete;
    private List<OptionValue> options = new ArrayList<>();
    private List<OptionValue> optionsToDelete = new ArrayList<>();
    public AddEditOptionListView(AddEditOptionListController controller, User owner, OptionList optionList) {
        super((optionList == null ? "Create" : "Update") + " Option List");

        this.controller = controller;
        this.owner = owner;
        this.optionList = optionList;

        setHints(List.of(Hint.CENTERED, Hint.FIXED_SIZE));
        setFixedSize(new TerminalSize(47, 17));

        root = new Panel().setLayoutManager(new LinearLayout(Direction.VERTICAL));
        setComponent(root);

        namePanel = getNamePanel();
        systemCheckBox = getCheckbox();
        table = getTable();
        addOptionPanel = getAddOptionPanel();
        new EmptySpace().addTo(root);
        new EmptySpace().addTo(root);
        affichageDesButtons(normal);

        txtName.setText(optionList != null ? optionList.getName() : "");
        updateAddButtonState();
        updateCreateButtonState();
        errAddOption.setVisible(options.isEmpty());
    }
    private Panel getNamePanel() {
        final Panel namePanel = new Panel().addTo(root);
        namePanel.setLayoutManager(new GridLayout(2).setTopMarginSize(1));
        nameLabel = new Label("Name: ");
        txtName = new TextBox(new TerminalSize(37, 1))
                .setValidationPattern(Pattern.compile("[a-z A-Z][a-z A-Z\\d.;:/,()-_]{0,25}"))
                .setTextChangeListener((txt, byUser) -> validate());
        errName = new Label("").setForegroundColor(TextColor.ANSI.RED);

        namePanel.addComponent(nameLabel);
        namePanel.addComponent(txtName);
        namePanel.addComponent(new EmptySpace());
        namePanel.addComponent(errName);

        return namePanel;
    }

    private Panel getCheckbox() {
        final Panel checkBoxPanel = new Panel().addTo(root)
                .setLayoutManager(new GridLayout(2).setLeftMarginSize(1));
        new Label("System: ").addTo(checkBoxPanel);
        checkBoxSystem = new CheckBox().addTo(checkBoxPanel);
        checkBoxSystem.setChecked(optionList != null && optionList.isSystem());
        checkBoxSystem.addListener((newState) -> controller.handleToggleSystem(newState));
        checkBoxPanel.setVisible(owner.isAdmin());
        new EmptySpace().addTo(checkBoxPanel);
        return checkBoxPanel;
    }
    private ObjectTable<OptionValue> getTable() {
        final ObjectTable<OptionValue> table;
        table = new ObjectTable<>(
                new ColumnSpec<>("Index", OptionValue::getIdx),
                new ColumnSpec<>("Label", OptionValue::getLabel).setMinWidth(40)
        ).addTo(root);
        System.out.println("Table created and added to root");
        table.setPreferredSize(new TerminalSize(getTerminalColumns(), 9));
        //table.setSelectAction(this::choice);
        table.addSelectionChangeListener(this::change);
        if(!normal){
            reorder();
        }
        addKeyboardListener(table,KeyType.Backspace,this::deleteSelectedOption);
        addKeyboardListener(table,KeyType.Delete,this::deleteSelectedOption);
        return table;
    }

    private Panel getAddOptionPanel() {
        final Panel addOptionPanel = new Panel().addTo(root);
        addOptionPanel.setLayoutManager(new GridLayout(2).setLeftMarginSize(1).setRightMarginSize(1));

        errAddOption = new Label("at least one value required")
                .setForegroundColor(TextColor.ANSI.RED)
                .setVisible(controller.getTempOptions().isEmpty());

        txtAddOption = new TextBox(new TerminalSize(35, 1))
                .setTextChangeListener((txt, byUser) -> {
                    updateAddButtonState();
                });
        btnAddOption = new Button("Add", () -> {
            String label = txtAddOption.getText().trim();
            if (!label.isEmpty()) {
                controller.addOptionInMemory(label);
                txtAddOption.setText("");
                errAddOption.setVisible(false);
                updateCreateButtonState();
            }
        });
        addOptionPanel.addComponent(errAddOption);
        addOptionPanel.addComponent(new EmptySpace());
        addOptionPanel.addComponent(txtAddOption);
        addOptionPanel.addComponent(btnAddOption);

        if (optionList.isSystem() && !owner.isAdmin()) {
            addOptionPanel.setVisible(false);
            return addOptionPanel;
        }

        return addOptionPanel;
    }
    private void updateAddButtonState() {
        String text = txtAddOption.getText().trim();
        btnAddOption.setEnabled(!text.isEmpty() && !controller.isOptionDuplicate(text));
    }

    public void updateCreateButtonState() {
        if (btnCreate != null) {
            btnCreate.setEnabled(!controller.getOptions().isEmpty());
        }
    }

    private void affichageDesButtons(boolean normal){
        if (btnContainer == null) {
            btnContainer = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
            root.addComponent(btnContainer);
        } else {
            btnContainer.removeAllComponents();
        }
        if (optionList != null) {
            if (normal) {
                if (owner.isAdmin() || !optionList.isSystem()) {
                    new Button("Reorder", this::reorder).addTo(btnContainer);
                    if (!optionList.isUsed()) {
                        btnDelete = new Button("Delete", this::deleteOptionList).addTo(btnContainer);
                    }
                    new Button("Save", this::save).addTo(btnContainer);
                }
                new Button("Duplicate", this::duplicate).addTo(btnContainer);
                new Button("Close", this::closeAll).addTo(btnContainer);
            } else {
                new Button("Alphabetically", this::alphabetically).addTo(btnContainer);
                new Button("Confirm order", this::confirmOrder).addTo(btnContainer);
                new Button("Cancel", this::cancelOrder).addTo(btnContainer);
            }
        } else {
            btnCreate = new Button("Create", this::createOptionList).addTo(btnContainer);
            btnCreate.setEnabled(false);
            new Button("Close", this::closeAll).addTo(btnContainer);
        }
        root.invalidate();
    }

    private void createOptionList() {
        String name = txtName.getText().trim();
        if (!name.isEmpty()) {
            controller.createAndSaveOptionList(name);
            close();
        } else {
            errName.setText("name required");
        }
    }
    public void updateButtonDisplay(boolean normal) {
        affichageDesButtons(normal);
    }
    private void choice(){
        if (!normal){
            //on change juste l'etat de moving pour dire si on bouge ou paS
            moving = !moving;
        } else {
            return;
        }
    }
    private void change(int prec, int current, boolean byUser){
        if (!moving) return;
        System.out.println("sectionChanged");
        swap(prec, current);
        System.out.println("return");
    }
    private void swap(int prec, int current){
        System.out.println("swap");
        OptionValue item1 = table.getItem(prec);
        OptionValue item2 = table.getItem(current);
        String tmpLabel = item1.getLabel();
        item1.setLabel(item2.getLabel());
        item2.setLabel(tmpLabel);
        table.refresh();
    }
    private boolean deleteSelectedOption() {
        OptionValue selectedOption = table.getSelected();
        if (selectedOption != null) {
            controller.addToDeleteList(selectedOption);
            controller.getTempOptions().remove(selectedOption);
            controller.reindexInMemory(controller.getTempOptions());
            table.clear();
            table.add(controller.getTempOptions());
            table.refresh();
        }
        return true;
    }
    private void reindexOptions() {
        for (int i = 0; i < options.size(); i++) {
            options.get(i).setIdx(i + 1);
        }
    }
    private void reorder() {
        normal = false;
        table.setSelectAction(this::choice);
        affichageDesButtons(normal);}
    private void deleteOptionList() {
        boolean canDelete = controller.canDeleteOptionList(optionList);
        btnDelete.setVisible(canDelete);
        controller.deleteOptionList(optionList);}
    private void duplicate() {controller.duplicate();}
    private void alphabetically() {controller.alphabetically();}
    private void confirmOrder() {controller.confirmOrder();}
    private void cancelOrder() {controller.cancelOrder();}
    private void save() {controller.save();}

    private void closeAll() {controller.closeAll();}
    private void validate() {
        var errors = controller.validate(txtName.getText());
        boolean hasOptions = !options.isEmpty();
        if (!hasOptions) {
            errAddOption.setVisible(true);
        } else {
            errAddOption.setVisible(false);
        }
        errName.setText(errors.getFirstErrorMessage(Form.Fields.Name));
    }

    public void reloadData() {
        table.clear();
        List<OptionValue> currentOptions = controller.getTempOptions();
        //for (int i = 0; i < currentOptions.size(); i++) {
        //    currentOptions.get(i).setIdx(i + 1);
        //}
//        currentOptions.sort(Comparator.comparingInt(OptionValue::getIdx));
        table.add(currentOptions);
        table.refresh();
        root.invalidate();

    }

    public void initialize() {
        List<OptionValue> tempOptions = controller.getTempOptions();
        table.clear();
        table.add(tempOptions);
        table.refresh();
    }
}