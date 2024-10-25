package tgpr.forms.controller;
import tgpr.forms.model.*;
import tgpr.forms.view.LoginView;
import tgpr.forms.view.ViewFormsView;
import tgpr.framework.Controller;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewFormsController extends Controller<ViewFormsView> {
    private final User currentUser;
    private final ViewFormsView view;
    private int currentPage = 0;
    private final int formsPerPage = 9;
    private List<Form> forms;
    private List<Form> filteredForms;

    public ViewFormsController(User user) {
        this.currentUser = user;
        this.view = new ViewFormsView(this, currentUser);
        this.forms = getUserForms();
        this.filteredForms = forms;
        showUserForms();
    }

    @Override
    public ViewFormsView getView() {
        return view;
    }

    public List<Form> getUserForms() {
        // Récupérer tous les formulaires auxquels l'utilisateur a accès
        return Form.getForUser(currentUser, "", 0, Integer.MAX_VALUE);
    }

    public void showUserForms() {
        if (forms != null && !forms.isEmpty()) {
            System.out.println("Nombre de formulaires récupérés : " + forms.size());
            view.displayForms(forms, currentPage, formsPerPage);
        } else {
            System.out.println("Aucun formulaire trouvé.");
        }
    }

    public void showProfile() {
        User loggedUser = Security.getLoggedUser();
        if (loggedUser != null) {
            // Logique pour afficher les détails du profil
            System.out.println("Profil de l'utilisateur : " + loggedUser.getFullName());
            // Ajouter ici un appel à une méthode de la vue pour afficher les informations dans une boîte de dialogue
            getView().showProfileDialog(loggedUser.getFullName(), loggedUser.getEmail());
        } else {
            System.out.println("Aucun utilisateur connecté.");
        }
    }

    public void manageOptionListMenu(){
        navigateTo(new TestController());
    }

    public void logout() {
        Security.logout();
        System.out.println("Utilisateur déconnecté.");
        navigateTo(new LoginController());
        // Ajoute la logique de redirection après la déconnexion, si nécessaire
    }

    public void exitApplication() {
        // Logique pour fermer l'application proprement
        System.exit(0);
    }


    //Pagination
    public void goToFirstPage(){
        if (currentPage != 0){
            currentPage = 0;
            showUserForms();
        }
    }

    public void goToPreviousPage(){
        if (currentPage > 0){
            currentPage--;
            showUserForms();
        }
    }

    public void goToNextPage(){
        int totalPages = (int) Math.ceil((double) forms.size() / formsPerPage);
        if (currentPage < totalPages - 1){
            currentPage++;
            showUserForms();
        }
    }

    public void goToLastPage(){
        int totalPages = (int) Math.ceil((double) forms.size() / formsPerPage);
        if (currentPage != totalPages - 1){
            currentPage = totalPages - 1;
            showUserForms();
        }
    }

    public void filterForms(String filter) {
        if (filter == null || filter.isEmpty()) {
            filteredForms = forms;  // Si le filtre est vide, afficher tous les formulaires
        } else {
            filteredForms = forms.stream()
                    .filter(form -> (form.getTitle() != null && form.getTitle().toLowerCase().startsWith(filter.toLowerCase())) ||
                            (form.getDescription() != null && form.getDescription().toLowerCase().startsWith(filter.toLowerCase())))
                    .collect(Collectors.toList());  // Filtrer la liste en fonction de la clé de recherche
        }
        // Utiliser la méthode displayForms pour afficher les formulaires filtrés
        view.displayForms(filteredForms, 0, 9);  // Réinitialiser à la page 0 et afficher 9 formulaires par page
    }

    public void openForm(){
        navigateTo(new TestController());
    }

    public void manageForm(Form form) {
        navigateTo(new formController(form));
    }

    public void createForm(){
        navigateTo(new TestController());
    }








}