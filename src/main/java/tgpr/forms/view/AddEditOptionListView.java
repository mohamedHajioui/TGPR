package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.forms.controller.AddEditFormController;
import tgpr.forms.controller.TestController;
import tgpr.forms.model.OptionList;
import tgpr.forms.model.User;

import java.util.List;

public class AddEditOptionListView extends DialogWindow {
//    private final AddEditOptionListController controller;
    private final TestController controller;
    private final User owner;
    private final OptionList optionList;
    private final Panel root;
    private final Panel namePanel;
    private TextBox txtName;
    private final Label errName;
    private final Panel oLPanel;
    private final Panel addOptionPanel;
    private final Label errAddOption;
    private TextBox txtAddOption;
    private final Panel btnPanel;
    private final Button btnClose;



    public AddEditOptionListView(TestController controller, User owner, OptionList optionList) {
        super((optionList == null ? "Create" : "Update") + " Opton List");

        this.controller = controller;
        this.owner = owner;
        this.optionList = optionList;

        setHints(List.of(Hint.CENTERED, Hint.FIXED_SIZE));
        setCloseWindowWithEscape(true);
        setFixedSize(new TerminalSize(60, 30));

        root = new Panel().setLayoutManager(new LinearLayout());

        namePanel = new Panel().addTo(root).setLayoutManager(new GridLayout(2)
                .setTopMarginSize(1).setLeftMarginSize(1).setRightMarginSize(1));
        new Label("Name:").addTo(namePanel);
        txtName = new TextBox(new TerminalSize(40, 1)).addTo(namePanel);
        new EmptySpace().addTo(namePanel);
        errName = new Label("name required").addTo(namePanel).setForegroundColor(TextColor.ANSI.RED);

        oLPanel = new Panel().addTo(root).setLayoutManager(new GridLayout(2));
        new EmptySpace().addTo(oLPanel);

        addOptionPanel = new Panel().addTo(root).setLayoutManager(new GridLayout(2));
        errAddOption = new Label("at least one value required").addTo(addOptionPanel).setForegroundColor(TextColor.ANSI.RED);
        txtAddOption = new TextBox(new TerminalSize(40, 1)).addTo(addOptionPanel);
        new EmptySpace().addTo(addOptionPanel);

        btnPanel = new Panel().addTo(root).setLayoutManager(new LinearLayout(Direction.HORIZONTAL));

        btnClose = new Button("Close", this::close).addTo(btnPanel);

    }
}
