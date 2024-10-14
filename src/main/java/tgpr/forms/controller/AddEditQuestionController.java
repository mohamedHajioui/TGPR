package tgpr.forms.controller;

import com.googlecode.lanterna.screen.Screen;
import tgpr.forms.model.Question;
import tgpr.forms.view.AddEditQuestionView;
import tgpr.framework.Controller;

import java.io.IOException;

public class AddEditQuestionController extends Controller<AddEditQuestionView> {
    @Override
    public AddEditQuestionView getView() {
        return new AddEditQuestionView(this);
    }

}
