package tgpr.forms;

//<<<<<<< HEAD
import tgpr.forms.controller.*;
import tgpr.forms.model.Form;
import tgpr.forms.model.User;
import tgpr.forms.view.ViewFormsView;
//=======
import tgpr.forms.controller.AddEditQuestionController;
import tgpr.forms.controller.LoginController;
import tgpr.forms.controller.SignupController;
import tgpr.forms.controller.TestController;
import tgpr.forms.model.Form;
import tgpr.forms.model.Question;
//>>>>>>> 119e98b2fa5ae600c5dc47e201ad02fa71784183
import tgpr.framework.Controller;
import tgpr.forms.controller.formController;
import tgpr.framework.Model;
import tgpr.forms.model.User;
import tgpr.forms.model.Form;

public class FormsApp {
    public final static String DATABASE_SCRIPT_FILE = "/database/tgpr-2425-a01.sql";

    public static void main(String[] args) {
        if (!Model.checkDb(DATABASE_SCRIPT_FILE))
            Controller.abort("Database is not available!");
        else {
            Controller.navigateTo(new LoginController());
        }
    }
}
