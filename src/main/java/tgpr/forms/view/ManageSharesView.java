package tgpr.forms.view;

import com.googlecode.lanterna.gui2.ColumnSpec;
import com.googlecode.lanterna.gui2.ComboBox;
import com.googlecode.lanterna.gui2.ObjectTable;
import com.googlecode.lanterna.gui2.Panel;
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

    public ManageSharesView(ManageSharesController controller,Form form) {
        super("Manage Shares");
        this.controller = controller;
        this.form = form;
        refrenceList();
        affichage();
    }

    private void affichage(){
        setHints(List.of(Hint.CENTERED,Hint.MODAL));
        setCloseWindowWithEscape(true);
        Panel root = new Panel();
        setComponent(root);
        getList().addTo(root);

        ComboBox<User> cbUser = new ComboBox<>();
        for(User user : absent()) {
            cbUser.addItem(user);
        }
        cbUser.addTo(root);



    }


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


    private List<UserFormAccess> refrenceList(){
        List<UserFormAccess> users = new ArrayList<>();
        for (UserFormAccess a : UserFormAccess.getAll()){
            if (a.getFormId() == form.getId()){
                users.add(a);
            }
        }

        return users;

    }

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

    private void getCommande(){
    }


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
