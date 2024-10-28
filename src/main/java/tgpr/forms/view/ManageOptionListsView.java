package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.table.Table;
import tgpr.forms.controller.ManageOptionListsController;
import tgpr.forms.model.OptionList;
import tgpr.framework.ViewManager;

import java.util.List;

public class ManageOptionListsView extends BasicWindow { ;
    private final ObjectTable<OptionList> table;
    private final ManageOptionListsController controller;
    private final Button newListButton;
    private final Button closeButton;
    //private final Button Cancel;
    public ManageOptionListsView(ManageOptionListsController controller) {
        this.controller = controller;
        setTitle("Manage Option Lists");
        setHints(List.of(Hint.CENTERED));
        setFixedSize(new TerminalSize(70, 15));

        Panel root = new Panel();
        setComponent(root);

        Panel content = new Panel().addTo(root).setLayoutManager(new LinearLayout(Direction.VERTICAL));

        table = new ObjectTable<>(
                new ColumnSpec<>("Name",OptionList::getName),
                new ColumnSpec<>("Values", optionList -> String.valueOf(optionList.getOptionValues().size())),
                new ColumnSpec<>("Owner",optionList -> optionList.getOwner() != null ? optionList.getOwner().getName() : "System")
        );

        content.addComponent(table);
        table.setPreferredSize(new TerminalSize(ViewManager.getTerminalColumns(), 15));
        Panel buttonsPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        newListButton = new Button("New list", this::handleNewList).addTo(buttonsPanel);
        closeButton = new Button("Close", this::close).addTo(buttonsPanel);
        content.addComponent(buttonsPanel);

        reloadData();
        // Action pour ouvrir une liste d'options avec la touche Enter
        table.setSelectAction(() -> handleEditList(table.getSelected()));

    }
    private void reloadData() {
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
