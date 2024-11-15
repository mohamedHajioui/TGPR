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
        super("Update" + " Option List");

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

        txtName.setText(optionList.getName());


        errAddOption.setVisible(controller.getTempOptions().isEmpty());
        updateAddButtonState();
        updateCreateButtonState();
    }

    public AddEditOptionListView(AddEditOptionListController controller, User owner) {
        super("Create" + " Option List");

        this.controller = controller;
        this.owner = owner;
        this.optionList = new OptionList();

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

        txtName.setText(optionList.getName());


        errAddOption.setVisible(controller.getTempOptions().isEmpty());
        updateAddButtonState();
        updateCreateButtonState();
    }
    private Panel getNamePanel() {
        final Panel namePanel = new Panel().addTo(root);
        namePanel.setLayoutManager(new GridLayout(2).setTopMarginSize(1));
        nameLabel = new Label("Name: ");
        txtName = new TextBox(new TerminalSize(37, 1))
                .setValidationPattern(Pattern.compile("[a-z A-Z][a-z A-Z\\d.;:/,()-_]{0,30}"))
                .setTextChangeListener((txt, byUser) -> validate());
        updateCreateButtonState();
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
        checkBoxSystem.addListener(controller::handleToggleSystem);
        checkBoxPanel.setVisible(owner.isAdmin());
        new EmptySpace().addTo(checkBoxPanel);
        return checkBoxPanel;
    }
    private ObjectTable<OptionValue> getTable() {
        final ObjectTable<OptionValue> table;
        table = new ObjectTable<>(
                new ColumnSpec<>("Index", OptionValue::getIdx),
                new ColumnSpec<>("Label", OptionValue::getLabel).setMaxWidth(40)
        ).addTo(root);
        table.add(optionList.getOptionValues());
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

        errAddOption = new Label("at least one value required") //!!!
                .setForegroundColor(TextColor.ANSI.RED)
                .setVisible(controller.getOptions().isEmpty());

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
                reloadData();
            }
        });
        addOptionPanel.addComponent(errAddOption);
        addOptionPanel.addComponent(new EmptySpace());
        addOptionPanel.addComponent(txtAddOption);
        addOptionPanel.addComponent(btnAddOption);
        // si liste SYSTEM        OU    liste USED       ET (user PAS admin   ou   user pas proprio de la liste)
        if (optionList.isSystem() || optionList.isUsed() && (!owner.isAdmin() || owner.getId() != optionList.getOwnerId())) {
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
            btnCreate.setEnabled(!controller.getTempOptions().isEmpty());
        }
    }

    private void affichageDesButtons(boolean normal){
        if (btnContainer == null) {
            btnContainer = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
            root.addComponent(btnContainer);
        } else {
            btnContainer.removeAllComponents();
        }
        if (!optionList.getOptionValues().isEmpty()) {
            if (normal) {   // AUTORISATION DE MODIFICATION : ADMIN, LIST PAS SYSTEM
                if (owner.isAdmin() || !optionList.isSystem()) {   //!!!!!
                    if (!optionList.isUsed()) {     // SI optionList PAS UTILISéE dans une question
                        new Button("Reorder", this::reorder).addTo(btnContainer);
                        btnDelete = new Button("Delete", this::deleteOptionList).addTo(btnContainer);
                        new Button("Save", this::save).addTo(btnContainer);
                    }
                }   // TOUJOURS dispo pour consultation
                new Button("Duplicate", this::duplicate).addTo(btnContainer);
                new Button("Close", this::closeAll).addTo(btnContainer);
            } else {// Mode REORDER
                new Button("Alphabetically", this::alphabetically).addTo(btnContainer);
                new Button("Confirm order", this::confirmOrder).addTo(btnContainer);
                new Button("Cancel", this::cancelOrder).addTo(btnContainer);
            }
        } else {    // Mode CREATE NEW LIST
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
            controller.cleanUpDeletedOptions();
            table.clear();
            table.add(controller.getTempOptions());
            table.refresh();
            errAddOption.setVisible(controller.getTempOptions().isEmpty());
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
        affichageDesButtons(normal);
        controller.reorder();
    }
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
        boolean hasOptions = !controller.getOptions().isEmpty();
        errAddOption.setVisible(!hasOptions);
        errName.setText(errors.getFirstErrorMessage(Form.Fields.Name));
    }

    public void reloadData() {
        table.clear();
        List<OptionValue> currentOptions = controller.getTempOptions();
        if (currentOptions != null && !currentOptions.isEmpty()) {
            table.add(currentOptions);
        }
        table.refresh();
        root.invalidate();
        errAddOption.setVisible(currentOptions == null || currentOptions.isEmpty());
        updateCreateButtonState();

    }

    public void initialize() {
        List<OptionValue> tempOptions = controller.getTempOptions();
        table.clear();
        table.add(tempOptions);
        table.refresh();
    }
}