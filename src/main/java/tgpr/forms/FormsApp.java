package tgpr.forms;
import tgpr.forms.controller.ViewFormsController;
import tgpr.forms.model.Security;
import tgpr.forms.model.User;
import tgpr.framework.Controller;
import tgpr.framework.Model;

public class FormsApp {
    public final static String DATABASE_SCRIPT_FILE = "/database/tgpr-2425-a01.sql";

    public static void main(String[] args) {
        if (!Model.checkDb(DATABASE_SCRIPT_FILE)) {
            Controller.abort("Database is not available!");
        } else {
            User user = User.getByFullName("Administrator");
            Security.login(user);
            Controller.navigateTo(new ViewFormsController(user));
        }
    }
}