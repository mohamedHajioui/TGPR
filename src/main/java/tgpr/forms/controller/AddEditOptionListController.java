
package tgpr.forms.controller;

import tgpr.forms.model.*;
import tgpr.forms.view.AddEditOptionListView;
import tgpr.framework.Controller;
import tgpr.framework.ErrorList;

import java.util.ArrayList;
import java.util.List;

public class AddEditOptionListController extends Controller<AddEditOptionListView> {
    private AddEditOptionListView view;
    private OptionList optionList;
    private User owner;
    private List<OptionValue> options;

    public AddEditOptionListController(User owner, OptionList optionList, List<OptionValue> options) {
        this.owner = owner;
        this.optionList = optionList;
        this.options = (options != null) ? options : new ArrayList<>();
        view = new AddEditOptionListView(this, owner, optionList);
    }

    @Override
    public AddEditOptionListView getView() {
        return view;
    }

    public OptionList getOptionList() {
        return optionList;
    }

    public ErrorList validate(String optionListName) {
        var errors = new ErrorList();

        var nameError = OptionListValidator.validateOptionListName(optionListName, owner, optionList);
        if (nameError != null) {
            errors.add(nameError, Form.Fields.Name);
        }
        return errors;
    }

    public void addOptionList(String name) {
        var errors = validate(name);
        if (errors.isEmpty() && canCreateOptionList()) {
            optionList = new OptionList(name);
            optionList.setOwnerId(owner.getId());
            optionList.save();
            for (OptionValue optionValue : options) {
                optionValue.setOptionListId(optionList.getId());
                optionValue.save();
            }
            view.close();
        }
    }

    public void updateOptionList(String newName) {
        optionList.setName(newName);
        optionList.save();
    }

    public boolean addOption(OptionValue option) {
        if (options == null) {
            options = new ArrayList<>();
        }
        boolean exists = options.stream()
                .anyMatch(existingOption -> existingOption.getLabel().equals(option.getLabel()));
        if (!exists) {
            option.setIdx(options.size() + 1);
            options.add(option);
            option.setOptionListId(optionList.getId());
            option.save();
            return true;
        }
        return false;
    }

    public List<OptionValue> getOptions() {
        return options;
    }

    public boolean canCreateOptionList() {
        return options != null && !options.isEmpty();
    }
}
