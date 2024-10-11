package tgpr.forms.controller;

import tgpr.forms.model.Form;
import tgpr.forms.view.*;

import java.util.Date;

public class FormCardController {
    private final Form form;
    private final boolean canManage;

    public FormCardController(Form form, boolean canManage) {
        this.form = form;
        this.canManage = canManage;
    }

    // Méthodes pour récupérer les informations du modèle
    public String getTitle() {
        return form.getTitle();
    }

    public String getDescription() {
        return form.getDescription();
    }

    public String getCreator() {
        return form.getOwner().getFullName();
    }


    public boolean hasQuestions() {
        return form.getQuestions().size() > 0;
    }

    public boolean canManage() {
        return canManage;
    }

    // Actions associées aux boutons "Open" et "Manage"
    public void onOpen() {
        // Logique pour ouvrir le formulaire (en tant qu'utilisateur)
        System.out.println("Opening form: " + form.getTitle());
    }

    public void onManage() {
        // Logique pour gérer le formulaire (en tant qu'éditeur)
        System.out.println("Managing form: " + form.getTitle());
    }
}
