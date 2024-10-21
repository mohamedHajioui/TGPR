package tgpr.forms;

import tgpr.forms.controller.AddEditQuestionController;
import tgpr.forms.controller.LoginController;
import tgpr.forms.controller.TestController;
import tgpr.forms.model.Form;
import tgpr.forms.model.Question;
import tgpr.framework.Controller;
import tgpr.framework.Model;

public class FormsApp {
    public final static String DATABASE_SCRIPT_FILE = "/database/tgpr-2425-a01.sql";

    public static void main(String[] args) {
        if (!Model.checkDb(DATABASE_SCRIPT_FILE))
            Controller.abort("Database is not available!");
        else {
            Question newQuestion = null;
            Form form = Form.getByKey(1);
            Controller.navigateTo(new AddEditQuestionController(newQuestion,form));
        }
    }
}
