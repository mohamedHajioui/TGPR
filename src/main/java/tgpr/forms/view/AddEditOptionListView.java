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
    private final ObjectTable<OptionValue> table;
    private final Panel addOptionPanel;
    private Label errAddOption;
    private TextBox txtAddOption;
    private Button btnAddOption;
    private final Panel btnPanel;
    //private Button btnCreate;
    private List<OptionValue> options = new ArrayList<>();

    public AddEditOptionListView(AddEditOptionListController controller, User owner, OptionList optionList) {
        super((optionList == null ? "Create" : "Update") + " Option List");

        this.controller = controller;
        this.owner = owner;
        this.optionList = optionList;

        setHints(List.of(Hint.CENTERED, Hint.FIXED_SIZE));
//        setCloseWindowWithEscape(true);
        setFixedSize(new TerminalSize(50, 17));

        root = new Panel().setLayoutManager(new LinearLayout(Direction.VERTICAL));
        setComponent(root);

        namePanel = getNamePanel();
        table = getTable();
        addOptionPanel = getAddOptionPanel();
        new EmptySpace().addTo(root);
        new EmptySpace().addTo(root);
        btnPanel = affichageDesButtons(normal);

        if (optionList != null) {
            txtName.setText(optionList.getName());
            reloadData();
        }
    }
    private Panel getNamePanel() {
        final Panel namePanel;
        namePanel = new Panel().addTo(root)
                .setLayoutManager(new GridLayout(2)
                        .setTopMarginSize(1).setLeftMarginSize(1).setRightMarginSize(2).setHorizontalSpacing(2));
        new Label("Name:").addTo(namePanel)
                .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.BEGINNING));
        txtName = new TextBox(new TerminalSize(35, 1)).addTo(namePanel)
                .setValidationPattern(Pattern.compile("[a-z A-Z][a-z A-Z\\d.;:/,-_]{0,25}"))
                .setTextChangeListener((txt, byUser) -> validate())
                .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.BEGINNING));;
        new EmptySpace().addTo(namePanel);
        errName = new Label("name required").addTo(namePanel).setForegroundColor(TextColor.ANSI.RED);
        return namePanel;
    }

    private ObjectTable<OptionValue> getTable() {
        final ObjectTable<OptionValue> table;
        table = new ObjectTable<>(
                new ColumnSpec<>("Index", optionValue -> options.indexOf(optionValue) + 1),
                new ColumnSpec<>("Label", OptionValue::getLabel)
        ).addTo(root);
        table.setPreferredSize(new TerminalSize(getTerminalColumns(), 10));
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
        errAddOption = new Label("at least one value required").addTo(root)
                .setForegroundColor(TextColor.ANSI.RED);
        errAddOption.setVisible(false);

        addOptionPanel = new Panel().addTo(root)
                .setLayoutManager(new GridLayout(2).setLeftMarginSize(1).setRightMarginSize(1));

        txtAddOption = new TextBox(new TerminalSize(35, 1)).addTo(addOptionPanel)
                .setTextChangeListener((txt, byUser) -> {
                    btnAddOption.setEnabled(!txt.isEmpty());
                    //btnCreate.setEnabled(controller.canCreateOptionList());
                });
        btnAddOption = new Button("Add", this::addOptionValue).addTo(addOptionPanel);

        return addOptionPanel;
    }

    private Panel affichageDesButtons(boolean normal){
        Panel btnContainer = new Panel(new LinearLayout(Direction.HORIZONTAL)).addTo(root);
        if (optionList != null) {
            if (normal) {
                new Button("Reorder", this::reorder).addTo(btnContainer);
                new Button("Delete", this::deleteValue).addTo(btnContainer);
                new Button("Duplicate", this::duplicate).addTo(btnContainer);
                new Button("Save", this::saveOptionList).addTo(btnContainer).setEnabled(false);
                new Button("Close", this::close).addTo(btnContainer);
            } else {
                new Button("Alphabetically", this::alphabetically).addTo(btnContainer);
                new Button("Confirm", this::confirmOrder).addTo(btnContainer);
                new Button("Cancel", this::cancelOrder).addTo(btnContainer);
            }
        } else {
            new Button("Create", this::createOptionList).addTo(btnContainer);
            new Button("Close", this::close).addTo(btnContainer);
        }
        return btnContainer;
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
        OptionValue tmp = table.getItem(current);
        table.setItem(current, table.getItem(prec));
        table.setItem(prec, tmp);
        table.refresh();
    }
    private void deleteSelectedOption() {
        OptionValue selectedOption = table.getSelected();
        if (selectedOption != null) {
            options.remove(selectedOption);
            reindexOptions();  // changer la table en optionList
            table.clear();
            table.add(options);
        }
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
    private void createOptionList() {controller.createOptionList(txtName.getText(), optionValue);}
    private void saveOptionList() {controller.saveOptionList(optionList);}
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

    public void reloadData() {
        options.clear();
        options.addAll(optionList.getOptionValues());
        options.sort(Comparator.comparingInt(OptionValue::getIdx));
        table.clear();
        table.add(options);
    }

    private void addOptionValue() {
        String label = txtAddOption.getText();
        controller.addOptionValue(label);
        txtAddOption.setText("");
    }

    private void save(){
        System.out.println("save");
        optionList.reorderValues(table.getItems());
        normal = true;
        affichageDesButtons(true);
    }
}