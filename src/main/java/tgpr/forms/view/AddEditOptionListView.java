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
import tgpr.forms.model.Form;
import tgpr.forms.model.OptionList;
import tgpr.forms.model.OptionValue;
import tgpr.forms.model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import static tgpr.framework.ViewManager.getTerminalColumns;

public class AddEditOptionListView extends DialogWindow {
    private final AddEditOptionListController controller;
    private final User owner;
    private OptionList optionList;
    private final Panel root;
    private final Panel namePanel;
    private TextBox txtName;
    private final Label errName;
    private final ObjectTable<OptionValue> table;
    private final Panel addOptionPanel;
    private final Label errAddOption;
    private TextBox txtAddOption;
    private Button btnAddOption;
    private final Panel btnPanel;
    private final Panel btnContainer;
    private final Button btnReorder;
    private final Button btnDuplicate;
    private final Button btnAlpha;
    private final Button btnConfirm;
//    private final Button btnCancel;
    private Button btnCreate;
    private final Button btnClose;
    private List<OptionValue> options = new ArrayList<>();

    public AddEditOptionListView(AddEditOptionListController controller, User owner, OptionList optionList) {
        super((optionList == null ? "Create" : "Update") + " Option List");

        this.controller = controller;
        this.owner = owner;
        this.optionList = optionList;

        setHints(List.of(Hint.CENTERED, Hint.FIXED_SIZE));
//        setCloseWindowWithEscape(true);
        setFixedSize(new TerminalSize(50, 15));

        root = new Panel().setLayoutManager(new GridLayout(1));
        setComponent(root);

        namePanel = new Panel().addTo(root)//.setLayoutData()
                .setLayoutManager(new GridLayout(2)
                        .setTopMarginSize(1).setLeftMarginSize(1));
        new Label("Name:").addTo(namePanel);
        txtName = new TextBox(new TerminalSize(40, 1)).addTo(namePanel)
                .setValidationPattern(Pattern.compile("[a-z A-Z][a-z A-Z\\d.;:/,-_]{0,25}"))
                .setTextChangeListener((txt, byUser) -> validate());
        new EmptySpace().addTo(namePanel);
        errName = new Label("name required").addTo(namePanel).setForegroundColor(TextColor.ANSI.RED);

        table = new ObjectTable<>(
                new ColumnSpec<>("Index", optionValue -> options.indexOf(optionValue) + 1),
                new ColumnSpec<>("Label", OptionValue::getLabel)
        );
        root.addComponent(table);
        table.setPreferredSize(new TerminalSize(getTerminalColumns(), 8));
        table.setKeyStrokeHandler(keyStroke -> {
            if (keyStroke.getKeyType() == KeyType.Delete) {
                OptionValue selectedOption = table.getSelected();
                if (selectedOption != null) {
                    controller.optionValueDelete(selectedOption);
                }
            }
        });
        new EmptySpace().addTo(root);

        addOptionPanel = new Panel().addTo(root)
                .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.BEGINNING, true, false))
                .setLayoutManager(new LinearLayout(Direction.HORIZONTAL));

        errAddOption = new Label("at least one value required").addTo(addOptionPanel)
                .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.BEGINNING, true, false))
                .setForegroundColor(TextColor.ANSI.RED);
        errAddOption.setVisible(false);

        txtAddOption = new TextBox(new TerminalSize(40, 1)).addTo(addOptionPanel)
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Beginning))
                .setTextChangeListener((txt, byUser) -> {
                    btnAddOption.setEnabled(!txt.isEmpty());
                    btnCreate.setEnabled(controller.canCreateOptionList());
                });

        btnAddOption = new Button("Add", this::addOptionValue).addTo(addOptionPanel)
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.End));

        new EmptySpace().addTo(root);

        btnPanel = new Panel().addTo(root)
                .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.END, GridLayout.Alignment.BEGINNING))
                .setLayoutManager(new LinearLayout(Direction.HORIZONTAL));


        btnContainer = new Panel(new LinearLayout(Direction.HORIZONTAL)).addTo(btnPanel);

        btnReorder = new Button("Reorder", this::reorder).addTo(btnContainer);

        btnDuplicate = new Button("Duplicate", this::duplicate).addTo(btnContainer);

        btnAlpha = new Button("Alphabetically", this::alphabetically).addTo(btnContainer);

        btnConfirm = new Button("Confirm", this::confirmOrder).addTo(btnContainer);

//        btnCancel = new Button("Cancel", this::cancelOrder).addTo(btnContainer);

        btnCreate = new Button(optionList == null ? "Create" : "Save", this::createOrUpdateOptionList)
                .addTo(btnContainer).setEnabled(false);

        btnClose = new Button("Close", this::close).addTo(btnContainer);

        if (optionList != null) {
            txtName.setText(optionList.getName());
            reloadData();
        }
    }

    private void reorder() {
        controller.reorder();
    }

    private void duplicate() {
        controller.duplicate();
    }

    private void alphabetically() {
        controller.alphabetically();
    }
    private void confirmOrder() {
        controller.confirmOrder();
    }
/*
    private void cancelOrder() {
        controller.cancelOrder(options);   // !!!
    }
*/

    private void createOrUpdateOptionList() {
        if (optionList == null) {
            optionList = controller.createOptionList(txtName.getText());
        } else {
            controller.updateOptionList(optionList);
        }
        controller.saveOptionValues();
    }

    private void validate() {
        var errors = controller.validate(txtName.getText());
        boolean hasOptions = !options.isEmpty();
        if (!hasOptions) {
            errAddOption.setVisible(true);
            btnCreate.setEnabled(false);
        } else {
            errAddOption.setVisible(false);
        }
        errName.setText(errors.getFirstErrorMessage(Form.Fields.Name));
        btnCreate.setEnabled(errors.isEmpty() && hasOptions);
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


}