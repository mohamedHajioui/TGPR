package tgpr.forms.view;

import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.forms.controller.EditInstanceController;
import tgpr.forms.controller.ViewInstancesController;

public class ViewInstancesView extends DialogWindow {
    private ViewInstancesController controller;
    private Panel mainPanel;

    public ViewInstancesView(ViewInstancesController controller) {
        super("Titre par defaut");
        this.controller = controller;



    }
    private void setViewTitle(String title) {
        setTitle(title); // Méthode pour changer le titre dynamiquement
    }}
