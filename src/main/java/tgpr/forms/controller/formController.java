package tgpr.forms.controller;

import tgpr.forms.model.Form;
import tgpr.framework.Controller;
import tgpr.framework.ErrorList;
import tgpr.forms.view.view_form;
import tgpr.forms.model.User;

import java.lang.reflect.Member;

public class formController extends Controller<view_form> {

    private final Form form;

    public formController(Form form) {
        this.form = form;
    }

    public view_form getView() {return new view_form(this,form);}



}
