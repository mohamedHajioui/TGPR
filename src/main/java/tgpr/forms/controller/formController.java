package tgpr.forms.controller;

import tgpr.forms.model.Form;
import tgpr.framework.Controller;
import tgpr.framework.ErrorList;
import tgpr.forms.view.view_form;
import tgpr.forms.model.User;

import java.lang.reflect.Member;

public class formController extends Controller<view_form> {

    private final view_form view;
    private  Form form;

    public formController(Form form) {
        this.form = form;
        view = new view_form(this,form);
    }

    public view_form getView() {return new view_form(this,form);}

    public void delete(){
        if (askConfirmation("Are you sure you want to delete this form?","Delete Form")){
            form.delete();
            view.close();
            form = null;
        }
    }



}
