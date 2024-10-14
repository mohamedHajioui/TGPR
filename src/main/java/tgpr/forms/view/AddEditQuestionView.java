package tgpr.forms.view;

import com.googlecode.lanterna.gui2.*;
import tgpr.forms.controller.AddEditQuestionController;
import tgpr.forms.model.Question;

import java.util.List;


public class AddEditQuestionView extends BasicWindow {
    private final TextBox txtTitle;
    private final TextBox txtDescription;
    //private final ComboBox<Question> cbType ;
    private final AddEditQuestionController controller;
    public AddEditQuestionView(AddEditQuestionController controller) {
        this.controller = controller;
        setTitle("Add/Edit Question");
        setHints(List.of(Hint.EXPANDED));
        Panel root = new Panel();
        Panel content = new Panel().addTo(root).setLayoutManager(new LinearLayout(Direction.VERTICAL));
        content.setLayoutManager(new GridLayout(10));
        content.addComponent(new Label("Title:"));
        txtTitle = new TextBox();
        Panel description = new Panel();
        description.setLayoutManager(new GridLayout(4));
        description.addComponent(new Label("Description:"));
        txtDescription = new TextBox();

    }


}