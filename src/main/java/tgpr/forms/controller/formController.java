package tgpr.forms.controller;
import tgpr.forms.model.Form;
import tgpr.forms.model.Question;
import tgpr.framework.Controller;
import tgpr.framework.ErrorList;
import tgpr.forms.view.view_form;
import tgpr.forms.model.User;
import tgpr.forms.model.Question;
import java.lang.reflect.Member;
import java.util.List;



public class formController extends Controller<view_form> {

    private final view_form view;
    private  Form form;
    private boolean normal = true;

    public formController(Form form) {
        this.form = form;
        view = new view_form(this,form,normal);
    }

    public void makePublic(){
        if (askConfirmation("Are you sure you want to delete all instances?"+"\n Note: This will delete instances currently being edited (not submited).", "Delete All Instances")){
            form.deleteAllInstances();
            form.save();
        }
    }

    public void versAnalyse(){
        Controller.navigateTo(new TestController());
    }

    public void versShare(){
        Controller.navigateTo(new TestController());
    }

    public void versEditForm(){
        Controller.navigateTo(new AddEditFormController(form.getOwner(), form));
        form.save();
    }

    public void versNouvelleQuestion(){
        Controller.navigateTo(new AddEditQuestionController(null,form));
        form.save();
    }

    public void versEditQuestion(Question question){
        Controller.navigateTo(new AddEditQuestionController(question,form));
        form.save();
    }

    public view_form getView() {return view;}

    public void delete(){
        if (askConfirmation("Are you sure you want to delete this form?","Delete Form")){
            User owner = form.getOwner();
            form.delete();
            view.close();
            form = null;
            Controller.navigateTo(new ViewFormsController(owner));
        }
    }


}
