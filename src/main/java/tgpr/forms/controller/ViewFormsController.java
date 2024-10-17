package tgpr.forms.controller;

import tgpr.forms.model.Form;
import tgpr.forms.model.Security;
import tgpr.forms.model.User;
import tgpr.forms.view.ViewFormsView;
import tgpr.framework.Controller;

import java.util.List;

public class ViewFormsController extends Controller<ViewFormsView> {
    private final User currentUser;
    private final ViewFormsView view;
    private int currentPage = 0;
    private final int formsPerPage = 9;
    private List<Form> forms;

    public ViewFormsController(User user) {
        this.currentUser = user;
        this.view = new ViewFormsView(this);
        this.forms = getUserForms();
        showUserForms();
    }

    @Override
    public ViewFormsView getView() {
        return view;
    }

    public List<Form> getForms() {
        return currentUser.getForms();
    }

    public List<Form> getUserForms() {
        return currentUser.getForms();
    }

    public void showUserForms(){
        if (forms != null){
            view.displayForms(forms, currentPage, formsPerPage);
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

    public void logout() {
        Security.logout();
        System.out.println("Utilisateur déconnecté.");
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




}