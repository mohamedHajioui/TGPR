package tgpr.forms.controller;

import tgpr.forms.model.Form;
import tgpr.forms.view.FormsCardView;
import com.googlecode.lanterna.gui2.Panel;

import java.util.ArrayList;
import java.util.List;

public class FormsViewController {
    private List<Form> forms;  // Liste des formulaires récupérés depuis la base de données
    private String filterText = "";
    private int currentPage = 0;
    private final int formsPerPage = 10;

    public FormsViewController(List<Form> forms) {
        this.forms = forms;
    }

    public List<FormsCardView> getFormCards() {
        List<FormsCardView> cards = new ArrayList<>();
        List<Form> filteredForms = getFilteredForms();

        // Pagination logic
        int start = currentPage * formsPerPage;
        int end = Math.min(start + formsPerPage, filteredForms.size());

        for (int i = start; i < end; i++) {
            FormCardController formCardController = new FormCardController(filteredForms.get(i), canManage(filteredForms.get(i)));
            FormsCardView formCardView = new FormsCardView(formCardController);
            cards.add(formCardView);
        }

        return cards;
    }

    private List<Form> getFilteredForms() {
        // Filtrage des formulaires en fonction du texte du filtre
        List<Form> filtered = new ArrayList<>();
        for (Form form : forms) {
            if (form.getTitle().contains(filterText) || form.getDescription().contains(filterText)) {
                filtered.add(form);
            }
        }
        return filtered;
    }

    public void onFilterChanged(String newText, boolean changedByUser) {
        filterText = newText;
        currentPage = 0;  // Réinitialiser la pagination
    }

    public boolean canManage(Form form) {
        // Logique pour déterminer si l'utilisateur peut gérer ce formulaire
        return true;  // Exemple simplifié
    }

    public Panel getPaginatorPanel() {
        // Retourne un panel pour gérer la pagination (avec des boutons "Suivant", "Précédent", etc.)
        return new Panel();  // Remplacer par la vraie implémentation du paginator
    }
}