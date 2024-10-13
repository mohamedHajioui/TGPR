package tgpr.forms.controller;

import tgpr.framework.Controller;
import tgpr.framework.ErrorList;
import tgpr.forms.view.view_form;
import tgpr.forms.model.User;

import java.lang.reflect.Member;

public class formController extends Controller {

    private final User user;

    public formController(User user) {
        this.user = user;
    }

    public view_form getView() {return new view_form(this);}



}
