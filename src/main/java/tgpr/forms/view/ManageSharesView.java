package tgpr.forms.view;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import org.apache.ibatis.annotations.Delete;
import tgpr.forms.controller.ManageSharesController;
import tgpr.forms.model.*;
import tgpr.framework.Controller;
import tgpr.framework.ViewManager;

import java.util.ArrayList;
import java.util.List;

import static tgpr.forms.model.AccessType.*;
import static tgpr.framework.Controller.askConfirmation;
import static tgpr.framework.Tools.ifNull;

public class ManageSharesView extends DialogWindow {


    private final Form form;
    private final ManageSharesController controller;
    private ObjectTable<UserFormAccess> table;
    private List<User> users;
    private ComboBox<User> cbUser;
    private ComboBox<AccessType> cbAccess;


    public ManageSharesView(ManageSharesController controller,Form form) {
        super("Manage Shares");
        this.controller = controller;
        this.form = form;
        refrenceList();
        affichage();
    }

    //---------------------------------------------------------------------------------------------------------------

    //La methode principale responsable de l'affichage
    private void affichage(){
        setHints(List.of(Hint.CENTERED,Hint.MODAL));
        setCloseWindowWithEscape(true);
        Panel root = new Panel();
        setComponent(root);
        getList().addTo(root);

        comboboxes().addTo(root);



        Button button = new Button("Add",this::Add);
        button.addTo(root);

    }

    //absent est la liste de User qui n'ont pas acces au form

    private List<User> absent(){
        List<User> absent = new ArrayList<>();
        List<User> present = new ArrayList<>();
        for(UserFormAccess a : refrenceList()){
            present.add(tgpr.forms.model.User.getByKey(a.getUserId()));
        }
        for(User user : tgpr.forms.model.User.getAll()) {
            System.out.println(user.getFullName());
            if (!present.contains(user)) {
                absent.add(user);
            }
        }return absent;
    }


    //comboboxes() est responsable de la cration des comboboxe: 1-cbUsesr qui donne le choix de user a partire de la liste de absent() et 2-cbAcces donne le choix des AccessType

    private Panel comboboxes(){
        Panel panel =  Panel.gridPanel(2);
        cbUser = new ComboBox<>();
        for(User user : absent()) {
            cbUser.addItem(user);
        }
        cbUser.addTo(panel);


        cbAccess = new ComboBox<>();
        cbAccess.addItem(Editor);
        cbAccess.addItem(User);
        cbAccess.addTo(panel);


        return panel;
    }




    // Add() cette methode s'appelle lors de l'appel du button Add est a pour but de ajouter a la liste les choix des combo box

    private void Add(){
        User selectedUser = cbUser.getSelectedItem();
        AccessType selectedAcess = cbAccess.getSelectedItem();
        UserFormAccess access = new UserFormAccess(selectedUser,form,selectedAcess);
        access.save();
        affichage();
    }


    //----------------------------------------------------------------------------------------------------------------------------------------------------------
    //tous se qui concerne la partie liste a afficher et les commandes sur la liste: -Enter, -Delete


    //refrenceListe() est une methode qui a pour simple bute de afficher tous les utilsateurs qui ont acces au form actuel

    private List<UserFormAccess> refrenceList(){
        List<UserFormAccess> users = new ArrayList<>();
        for (UserFormAccess a : UserFormAccess.getAll()){
            if (a.getFormId() == form.getId()){
                users.add(a);
            }
        }
        return users;
    }


    //getList() est la methode qui affiche dans la fenetre la liste des utilisateur avec leur role et accesType.

    private ObjectTable<UserFormAccess> getList(){
        table = new ObjectTable<UserFormAccess>(
                new ColumnSpec<>("Beneficiary", userFormAccess -> userFormAccess.getUser().getFullName() ),
                new ColumnSpec<>("Type", userFormAccess -> userFormAccess.getUser().getRole()),
                new ColumnSpec<>("Acess Right", UserFormAccess ::getAccessType )
        );
        table.sizeTo(ViewManager.getTerminalColumns(),10);
        table.add(refrenceList());

        table.setSelectAction(this::enterCommande);
        return table;
    }




    //enterCommande() est la methode qui se declanche lors de l'appui du button enter grace a table.setSelectionAction() elle rend switch le accesType d'un User

    private void enterCommande(){
        if (askConfirmation("Are you sure you want to delete this form?","Delete Form")){
            if (table.getSelected().getAccessType() == Editor){
                table.getSelected().setAccessType(User);
                table.getSelected().save();
            }else{
                table.getSelected().setAccessType(Editor);
                table.getSelected().save();
            }
        }

        affichage();
    }


}
