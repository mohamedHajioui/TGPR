package tgpr.forms;


import tgpr.forms.controller.LoginController;

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
