package tgpr.forms.controller;

import tgpr.forms.model.Instance;
import tgpr.forms.model.Question;
import tgpr.forms.model.User;
import tgpr.forms.view.EditInstanceView;
import tgpr.framework.Controller;

import java.time.LocalDateTime;
import java.util.List;

public class EditInstanceController extends Controller<EditInstanceView> {

    private EditInstanceView view;

    public EditInstanceController() {
        this.view = new EditInstanceView(this);
    }

    @Override
    public EditInstanceView getView() {
        return view;
    }
}


