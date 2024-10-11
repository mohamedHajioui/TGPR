package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import tgpr.forms.controller.FormsViewController;

import java.util.List;

public class FormsView {
    private final Panel mainPanel;
    private final FormsViewController controller;

    public FormsView(FormsViewController controller) {
        this.controller = controller;
        this.mainPanel = new Panel();
        mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        // Filtre et pagination
        TextBox filterBox = new TextBox();
        filterBox.setPreferredSize(new TerminalSize(30, 1));
        filterBox.setTextChangeListener(controller::onFilterChanged);
        mainPanel.addComponent(filterBox);

        // Ajout des cartes de formulaire
        updateView();

        // Ajout du composant de pagination
        Panel paginationPanel = controller.getPaginatorPanel();
        mainPanel.addComponent(paginationPanel);
    }

    private void updateView() {
        List<FormsCardView> formCards = controller.getFormCards();
        mainPanel.removeAllComponents();
        for (FormsCardView card : formCards) {
            mainPanel.addComponent(card.getPanel());
        }
    }

    public Panel getMainPanel() {
        return mainPanel;
    }
}
