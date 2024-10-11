package tgpr.forms.controller;

import com.googlecode.lanterna.screen.Screen;
import tgpr.forms.model.Form;
import tgpr.forms.model.User;
import tgpr.forms.view.ViewFormsView;
import java.util.List;

public class ViewFormsController {

    private User currentUser;
    private ViewFormsView view;

    public ViewFormsController(User user, Screen screen) {
        this.currentUser = user;
        this.view = new ViewFormsView(this, screen);
        loadForms();
    }

    public void loadForms() {
        // Charger les formulaires accessibles à l'utilisateur actuel
        List<Form> forms = Form.getAll();

        // Afficher les formulaires dans la vue
        view.displayForms(forms);
    }

    /*public void onOpenForm(Form form) {
        // Logique pour ouvrir le formulaire en mode réponse (si des questions existent)
        if (form.getQuestions()) {
            System.out.println("Opening form: " + form.getTitle());
            // Exemple : Logique pour passer à la vue d'édition d'une instance du formulaire
            // new ViewEditInstanceController(form, currentUser).show();
        } else {
            System.out.println("No questions available for this form.");
        }
    }

    public void onManageForm(Form form) {
        // Logique pour ouvrir le formulaire en mode édition si l'utilisateur a les droits
        if (form.isEditableBy(currentUser)) {
            System.out.println("Managing form: " + form.getTitle());
            // Exemple : Logique pour passer à la vue de gestion du formulaire
            // new ViewFormController(form, currentUser).show();
        } else {
            System.out.println("You do not have the rights to manage this form.");
        }
    }
*/
    public void createNewForm() {
        // Logique pour créer un nouveau formulaire
        System.out.println("Creating a new form...");
        // Exemple : Passer à une nouvelle vue pour la création d'un formulaire
        // new AddEditFormController(currentUser).show();
    }
}
