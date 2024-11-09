package tgpr.forms;

import tgpr.forms.controller.*;
import tgpr.forms.model.*;
import tgpr.forms.view.AddEditOptionListView;
import tgpr.framework.Controller;
import tgpr.framework.Model;
import tgpr.forms.model.Form;

public class FormsApp {
    public final static String DATABASE_SCRIPT_FILE = "/database/tgpr-2425-a01.sql";

    public static void main(String[] args) {
        if (!Model.checkDb(DATABASE_SCRIPT_FILE))
            Controller.abort("Database is not available!");
        else {
            User owner = User.getByKey(1);
            OptionList optionList = OptionList.getByKey(1);
            Controller.navigateTo(new AddEditOptionListController(owner,optionList, null));
            //Controller.navigateTo(new LoginController());
        }
    }
}
