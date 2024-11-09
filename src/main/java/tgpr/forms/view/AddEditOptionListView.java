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
    //private Button btnCreate;
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

        if (optionList != null) {
            txtName.setText(optionList.getName());
            reloadData();
        }
    }
    private Panel getNamePanel() {
        final Panel namePanel;
        namePanel = new Panel().addTo(root)
                .setLayoutManager(new GridLayout(2).setTopMarginSize(1).setLeftMarginSize(1).setRightMarginSize(2).setHorizontalSpacing(2));
        new Label("Name: ").addTo(namePanel);
        txtName = new TextBox(new TerminalSize(37, 1)).addTo(namePanel)
                .setValidationPattern(Pattern.compile("[a-z A-Z][a-z A-Z\\d.;:/,-_]{0,25}"))
                .setTextChangeListener((txt, byUser) -> validate());
        new EmptySpace().addTo(namePanel);
        errName = new Label("name required").addTo(namePanel).setForegroundColor(TextColor.ANSI.RED);
        return namePanel;
    }

    private Panel getCheckbox() {
        final Panel checkBoxPanel = new Panel().addTo(root)
                .setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        new Label("System").addTo(checkBoxPanel);
        checkBoxSystem = new CheckBox().addTo(checkBoxPanel);
        new EmptySpace().addTo(checkBoxPanel);
        return checkBoxPanel;
    }
    private ObjectTable<OptionValue> getTable() {
        final ObjectTable<OptionValue> table;
        table = new ObjectTable<>(
                new ColumnSpec<>("Index", optionValue -> options.indexOf(optionValue) + 1),
                new ColumnSpec<>("Label", OptionValue::getLabel).setMinWidth(40)
        ).addTo(root);
        table.setPreferredSize(new TerminalSize(getTerminalColumns(), 9));
        table.setSelectAction(this::choice);
        table.addSelectionChangeListener(this::change);
        if(!normal){
            reorder();
        }
        addKeyboardListener(table,KeyType.Backspace,this::deleteSelectedOption);
        return table;
    }

    private Panel getAddOptionPanel() {
        final Panel addOptionPanel;
        errAddOption = new Label("at least one value required")
                .addTo(root)
                .setForegroundColor(TextColor.ANSI.RED);
        errAddOption.setVisible(false);

        addOptionPanel = new Panel().addTo(root)
                .setLayoutManager(new GridLayout(2).setLeftMarginSize(1).setRightMarginSize(1));

        txtAddOption = new TextBox(new TerminalSize(35, 1)).addTo(addOptionPanel)
                .setTextChangeListener((txt, byUser) -> {
                    btnAddOption.setEnabled(!txt.isEmpty());
                    errAddOption.setVisible(options.isEmpty());
                });
        btnAddOption = new Button("Add", this::addOptionValue).addTo(addOptionPanel);
        btnAddOption.setEnabled(!options.isEmpty());
        //btnCreate.setEnabled(controller.canCreateOptionList());
        return addOptionPanel;
    }

    private void affichageDesButtons(boolean normal){
        if (btnContainer == null) {
            btnContainer = new Panel().addTo(root).setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
            btnContainer.addTo(root);
        } else {
            btnContainer.removeAllComponents();
        }
        if (optionList != null) {
            if (normal) {
                new Button("Reorder", this::reorder).addTo(btnContainer);
                //new Button("Delete", this::deleteValue).addTo(btnContainer);
                new Button("Duplicate", this::duplicate).addTo(btnContainer);
                new Button("Save", this::save).addTo(btnContainer);
                new Button("Close", this::close).addTo(btnContainer);
            } else {
                new Button("Alphabetically", this::alphabetically).addTo(btnContainer);
                new Button("Confirm order", this::confirmOrder).addTo(btnContainer);
                new Button("Cancel", this::cancelOrder).addTo(btnContainer);
            }
        } else {
            new Button("Create", this::createOptionList).addTo(btnContainer);
            //new Button("Close", this::closeAll).addTo(btnContainer);
        }
        root.invalidate();
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
            //options.remove(selectedOption);
            //reorder();
            //table.clear();
            //table.add(options);
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
        affichageDesButtons(normal);}

    private void duplicate() {controller.duplicate();}
    private void alphabetically() {controller.alphabetically();}
    private void confirmOrder() {controller.confirmOrder();}
    private void cancelOrder() {controller.cancelOrder();}
    public void createOptionList() {controller.createOptionList(txtName.getText(), txtAddOption.getText());}
    private void save() {controller.save(optionList);}
    //    private void closeAll() {controller.closeAll();}

    private void validate() {
        var errors = controller.validate(txtName.getText());
        boolean hasOptions = !options.isEmpty();
        if (!hasOptions) {
            errAddOption.setVisible(true);
            //btnCreate.setEnabled(false);
        } else {
            errAddOption.setVisible(false);
        }
        errName.setText(errors.getFirstErrorMessage(Form.Fields.Name));
        //btnCreate.setEnabled(errors.isEmpty() && hasOptions);
    }

    public void reload(List<OptionValue> options) {
        table.getItems().clear();
        table.getItems().addAll(options);
    }
    public void reloadData() {
        if (optionList != null && options != null) {
            for (OptionValue option : options) {
                option.save();
            }
            options.clear();
            List<OptionValue> newOptions = optionList.getOptionValues();
            if (newOptions != null) {
                options.addAll(newOptions);
                options.sort(Comparator.comparingInt(OptionValue::getIdx));
            }
            table.clear();
            table.add(options);
        }
    }

    private void addOptionValue() {
        String label = txtAddOption.getText();
        controller.addOptionValue(label);
        txtAddOption.setText("");
    }
/*
15. Si l'utilisateur courant est un admin, il peut éditer toutes les listes d'options,
y compris les listes "Système".

public void editOptionList(OptionList optionList) {
    User currentUser = getCurrentUser(); // Méthode pour récupérer l'utilisateur courant

    // Vérifie si la liste est une liste système et si l'utilisateur n'est pas admin
    if (optionList.isSystem() && !currentUser.isAdmin()) {
        throw new SecurityException("Vous n'avez pas l'autorisation de modifier cette liste.");
    }

    }
if (!currentUser.isAdmin()) {
    systemCheckbox.setDisable(true); // Désactiver la checkbox pour les non-admins
}

    public void initializeView(OptionList optionList) {
        this.optionList = optionList;

        systemCheckBox.setChecked(optionList.isSystem());
    systemCheckBox.addListener(e -> onSystemCheckBoxChanged());
    }

    public void onSystemCheckBoxChanged() {
        controller.handleToggleSystem(systemCheckBox.isChecked());
    }
*/
}