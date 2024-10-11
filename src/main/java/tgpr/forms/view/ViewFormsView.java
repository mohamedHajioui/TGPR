package tgpr.forms.view;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.gui2.table.Table;
import tgpr.forms.controller.ViewFormsController;
import tgpr.forms.model.Form;

import java.util.List;

public class ViewFormsView {

    private ViewFormsController controller;
    private Screen screen;

    public ViewFormsView(ViewFormsController controller, Screen screen) {
        this.controller = controller;
        this.screen = screen;
    }

    public void displayForms(List<Form> forms) {
        // Créer la fenêtre principale
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);
        BasicWindow window = new BasicWindow("Forms");

        // Créer une table pour afficher les formulaires sous forme de carte
        Table<String> table = new Table<>("Title", "Description", "Creator", "Status");

        for (Form form : forms) {
            String description = form.getDescription().isEmpty() ? "No description" : form.getDescription();
            String status = form.getDescription();
            String creator = form.getOwner().getFullName();

            table.getTableModel().addRow(form.getTitle(), description, creator, status);
        }

        // Ajouter la table à la fenêtre
        Panel contentPanel = new Panel();
        contentPanel.addComponent(table);

        // Ajouter un bouton pour fermer la vue
        contentPanel.addComponent(new Button("Close", () -> window.close()));

        // Attacher le panneau de contenu à la fenêtre
        window.setComponent(contentPanel);

        // Afficher la fenêtre
        gui.addWindowAndWait(window);
    }
}
