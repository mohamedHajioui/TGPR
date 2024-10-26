package tgpr.forms.controller;

import tgpr.forms.model.Form;
import tgpr.forms.view.AnalyseView;
import tgpr.framework.Controller;

public class AnalyseController extends Controller<AnalyseView> {
    private final AnalyseView view;


    public AnalyseController(Form currentForm) {
        this.view = new AnalyseView(this, currentForm);
    }

    public AnalyseView getView() {
        return view;
    }
}
