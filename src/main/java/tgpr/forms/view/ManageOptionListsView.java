package tgpr.forms.view;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.table.Table;
import tgpr.forms.controller.ManageOptionListsController;
import tgpr.forms.model.OptionList;

public class ManageOptionListsView extends BasicWindow {
    private OptionList list;
    private ManageOptionListsController controller;
    public ManageOptionListsView(ManageOptionListsController controller) {
        this.controller = controller;
    }
    Panel root = new Panel();
    setComponent(root);

}
