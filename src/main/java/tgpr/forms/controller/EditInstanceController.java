package tgpr.forms.controller;

// Controller/EditInstanceController.java
public class EditInstanceController {
    private FormInstance instance;
    private EditInstanceView view;

    public EditInstanceController(FormInstance instance, EditInstanceView view) {
        this.instance = instance;
        this.view = view;
    }

    public void start() {
        view.display(); // Affiche la vue
    }
}
