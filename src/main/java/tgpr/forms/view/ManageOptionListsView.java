package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.table.Table;
import tgpr.forms.controller.ManageOptionListsController;
import tgpr.forms.model.OptionList;
import tgpr.framework.ViewManager;

import java.util.List;


public class ManageOptionListsView extends DialogWindow { ;
    private final ObjectTable<OptionList> table;
    private final ManageOptionListsController controller;
    private final Button newListButton;
    private final Button cancelButton;

    public ManageOptionListsView(ManageOptionListsController controller) {
        super("Manage Option Lists");


        this.controller = controller;

        setHints(List.of(Hint.CENTERED));
        setFixedSize(new TerminalSize(60, 15));

        Panel root = new Panel();
        setComponent(root);

        Panel content = new Panel().addTo(root).setLayoutManager(new LinearLayout(Direction.VERTICAL));

        table =  new ObjectTable<>(
                new ColumnSpec<>("Name",OptionList::getName).setMinWidth(35),
                new ColumnSpec<>("Values", (OptionList optionList) -> String.valueOf(optionList.getOptionValues().size())).setMaxWidth(10),
                new ColumnSpec<>("Owner",(OptionList optionList) -> optionList.getOwner() != null ? optionList.getOwner().getName() : "System").setMinWidth(15)
        );

        //table.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        table.setPreferredSize(new TerminalSize(ViewManager.getTerminalColumns(), 15));

        content.addComponent(table);

        content.addComponent(new EmptySpace(new TerminalSize(80, 6)));
        Panel buttonsPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        newListButton = new Button("New list", this::handleNewList).addTo(buttonsPanel);
        cancelButton = new Button("Cancel", this::close).addTo(buttonsPanel);
        buttonsPanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        content.addComponent(buttonsPanel);

        reloadData();
        // Action pour ouvrir une liste d'options avec la touche Enter
        table.setSelectAction(() -> handleEditList(table.getSelected()));

    }
    public void reloadData() {
        List<OptionList> optionLists = controller.getOptionLists();
        table.clear();
        table.add(optionLists);
        table.invalidate();
    }
    private void handleNewList() {
        controller.navigateToOptionList(new OptionList());
    }
    private void handleEditList(OptionList optionList) {
        if(optionList != null) {
            controller.navigateToOptionList(optionList);
        }
    }




}
