package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.forms.controller.AddEditOptionListController;
import tgpr.forms.model.Form;
import tgpr.forms.model.OptionList;
import tgpr.forms.model.OptionValue;
import tgpr.forms.model.User;

import java.util.ArrayList;
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
    private final Panel tablePanel;
    private final ObjectTable<OptionValue> table;
    private final Panel addOptionPanel;
    private final Label errAddOption;
    private TextBox txtAddOption;
    private final Button btnAddOption;
    private final Panel btnPanel;
    private final Panel btnContainer;
    private final Button btnCreate;
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

        root = new Panel().setLayoutManager(new LinearLayout(Direction.VERTICAL));

        namePanel = new Panel().addTo(root)//.setLayoutData()
                .setLayoutManager(new GridLayout(2)
                        .setTopMarginSize(1).setLeftMarginSize(1));
        new Label("Name:").addTo(namePanel);
        txtName = new TextBox(new TerminalSize(40, 1)).addTo(namePanel)
                .setValidationPattern(Pattern.compile("[a-z A-Z][a-z A-Z\\d.;:/,-_]{0,25}"))
                .setTextChangeListener((txt, byUser) -> validate());
        new EmptySpace().addTo(namePanel);
        errName = new Label("name required").addTo(namePanel).setForegroundColor(TextColor.ANSI.RED);

        tablePanel = new Panel().addTo(root)
                .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.FILL, true, false))
                .setLayoutManager(new LinearLayout(Direction.VERTICAL));
        table = new ObjectTable<>(
                new ColumnSpec<>("Index", optionValue -> options.indexOf(optionValue) + 1),
                new ColumnSpec<>("Label", OptionValue::getLabel)
        );
        //        root.addComponent(table);
        table.setPreferredSize(new TerminalSize(getTerminalColumns(), 10));
        tablePanel.addComponent(table);

        addOptionPanel = new Panel().addTo(root)
                .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.BEGINNING, true, false))
                .setLayoutManager(new LinearLayout(Direction.HORIZONTAL));

        errAddOption = new Label("at least one value required").addTo(addOptionPanel)
                .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.BEGINNING, true, false))
                .setForegroundColor(TextColor.ANSI.RED);
        errAddOption.setVisible(false);

        txtAddOption = new TextBox(new TerminalSize(40, 1)).addTo(addOptionPanel)
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Beginning));

        btnAddOption = new Button("Add", this::addOption).addTo(addOptionPanel)
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.End));

        btnPanel = new Panel().addTo(root)
                .setLayoutData(BorderLayout.Location.BOTTOM)
                .setLayoutManager(new LinearLayout(Direction.HORIZONTAL));

        new EmptySpace().addTo(btnPanel);

        btnContainer = new Panel(new LinearLayout(Direction.HORIZONTAL));
        btnCreate = new Button(optionList == null ? "Create" : "Save", this::createOrUpdateOptionList)
                .addTo(btnContainer).setEnabled(false);
        btnClose = new Button("Close", this::close).addTo(btnContainer);

        btnContainer.addTo(btnPanel);

        setComponent(root);

        if (optionList != null) {
            txtName.setText(optionList.getName());
        }
    }

    private void addOption() {
        String label = txtAddOption.getText().trim();
        if (label.isEmpty()) {
            errAddOption.setVisible(true);
            btnAddOption.setEnabled(false);
        } else {
            OptionValue newOption = new OptionValue(optionList, options.size(), label);
            boolean added = controller.addOption(newOption);
            if (added) {
                errAddOption.setVisible(false);
                table.setItems(controller.getOptions());
                txtAddOption.setText("");
                btnAddOption.setEnabled(controller.canCreateOptionList());
            } else {
                errAddOption.setVisible(true);
            }
        }
    }

    private void createOrUpdateOptionList() {
        if (optionList == null) {
            addOptionList();
        } else {
            updateOptionList();
        }
    }

    private void addOptionList() {
        controller.addOptionList(
                txtName.getText(),
                owner
        );
        close();
    }

    private void updateOptionList() {
        optionList.setName(txtName.getText());
        controller.updateOptionList(optionList);
        close();
    }

    private void validate() {
        var errors = controller.validate(
                txtName.getText()
        );

        errName.setText(errors.getFirstErrorMessage(Form.Fields.Name));

        btnCreate.setEnabled(errors.isEmpty());
    }

}