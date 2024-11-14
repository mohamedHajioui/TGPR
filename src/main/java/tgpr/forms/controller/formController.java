package tgpr.forms.controller;
import tgpr.forms.model.*;
import tgpr.framework.Controller;
import tgpr.framework.ErrorList;
import tgpr.forms.view.view_form;
import tgpr.forms.model.Question;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;



public class formController extends Controller<view_form> {

    private final view_form view;
    private Form form;
    private User logedUser;
    private boolean normal = true;

    public formController(Form form,User logedUser) {
        this.form = form;
        this.logedUser = logedUser;
        view = new view_form(this,form,normal);
    }

    public view_form getView() {return view;}


    public void makePublic(){
        if (askConfirmation("Are you sure you want to make  public?", "Make Public")){
            refrenceList();
        }
    }

    private void refrenceList(){
        List<UserFormAccess> users = new ArrayList<>();
        for (UserFormAccess a : UserFormAccess.getAll()){
            if (a.getFormId() == form.getId()){
                if (a.getAccessType() == AccessType.User)
                    a.delete();
            }
        }
    }

    public void versAnalyse(){
        Controller.navigateTo(new AnalyseController(form, logedUser));
    }



    public void versShare(){
        Controller.navigateTo(new ManageSharesController(form,logedUser));
    }

    public void versEditForm(){
        Controller.navigateTo(new AddEditFormController(form.getOwner(), form));
        form.save();
    }

    public void versNouvelleQuestion(){
        Controller.navigateTo(new AddEditQuestionController(null,form,logedUser));
        form.save();
    }

    public void versEditQuestion(Question question){
        Controller.navigateTo(new AddEditQuestionController(question,form,logedUser));
        form.save();
    }


    public void delete(){
        if (askConfirmation("Are you sure you want to delete this form?","Delete Form")){
            User owner = form.getOwner();
            form.delete();
            view.close();
            form = null;
            Controller.navigateTo(new ViewFormsController(owner));
        }
    }

    public void versViewForms(){
        view.close();
        Controller.navigateTo(new ViewFormsController(logedUser));
    }

    public void versViewInstance(){
        Controller.navigateTo(new ViewInstancesController(form));
    }


}
