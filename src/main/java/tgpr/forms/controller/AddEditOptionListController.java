
package tgpr.forms.controller;

import tgpr.forms.model.OptionList;
import tgpr.forms.model.OptionListValidator;
import tgpr.forms.model.User;
import tgpr.forms.model.OptionValue;
import tgpr.forms.view.AddEditOptionListView;
import tgpr.framework.Controller;

import java.util.ArrayList;
import java.util.List;

public class AddEditOptionListController extends Controller<AddEditOptionListView> {
    private AddEditOptionListView view;
    private OptionList optionList;
    private User owner;
    private final List<OptionValue> options;

    public AddEditOptionListController() {
        this.options = new ArrayList<>();
    }
    public void initialize(User owner, OptionList optionList) {
        this.owner = owner;
        this.optionList = optionList;
        view = new AddEditOptionListView(this, owner, optionList);
    }

    public AddEditOptionListController(User owner, OptionList optionList, List<OptionValue> options) {
        this.owner = owner;
        this.optionList = optionList;
        this.options = options;
        view = new AddEditOptionListView(this, owner, optionList);
    }

    @Override
    public AddEditOptionListView getView() {
        return new AddEditOptionListView(this, owner, optionList);
    }

    public OptionList getOptionList() {
        return optionList;
    }

    public void addOptionList(String name, User owner) {
        var errors = OptionListValidator.validateOptionListName(name, owner, optionList);
        if (errors.isEmpty()) {
            optionList = new OptionList(name);
            optionList.save();
            view.close();
        }
    }

    public void validate(String optionListName) {
    }
    public void saveOptionList(OptionList optionList) {

    }

    public void updateOptionList(OptionList optionList) {
        optionList.save();
    }

    public boolean addOption(OptionValue option) {
        boolean exists = options.stream()
                .anyMatch(existingOption -> existingOption.getLabel().equals(option.getLabel()));
        if (!exists) {
            options.add(option);
            return true;
        }
        return false;
    }

    public List<OptionValue> getOptions() {
        return options;
    }

    public boolean canCreateOptionList() {
        return !options.isEmpty();
    }
}
