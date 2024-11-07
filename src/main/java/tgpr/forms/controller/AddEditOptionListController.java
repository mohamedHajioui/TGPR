
package tgpr.forms.controller;

import tgpr.forms.model.*;
import tgpr.forms.view.AddEditOptionListView;
import tgpr.framework.Controller;
import tgpr.framework.ErrorList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AddEditOptionListController extends Controller<AddEditOptionListView> {
    private AddEditOptionListView view;
    private OptionList optionList;
    private User owner;
    private OptionValue optionValue;
    private List<OptionValue> options;
    //private List<OptionValue> optionValues = new ArrayList<>();
    public AddEditOptionListController(User owner, OptionList optionList, List<OptionValue> options) {
        this.owner = owner;
        this.optionList = optionList;
        this.options = (options != null) ? options : new ArrayList<>();
        view = new AddEditOptionListView(this, owner, optionList);
    }

    public AddEditOptionListController() {
//        view = new AddEditOptionListView(this, owner, optionList);
    }

    @Override
    public AddEditOptionListView getView() {
        return view;
    }
    public OptionList getOptionList() {
        return optionList;
    }
    public List<OptionValue> getOptions() {
        return options;
    }

    public void save(String labelOption) {
        var errors = validate(optionList.getName());
        if (errors.isEmpty()) {
            List<OptionValue> existingOptions = optionList.getOptionValues();
            int idx = existingOptions.isEmpty() ? 1 : existingOptions.size() + 1;
            optionValue = new OptionValue(optionList, idx, labelOption);
            optionValue.save();
        } else {
            showErrors(errors);
        }
    }
    public ErrorList validate(String optionListName) {
        var errors = new ErrorList();

        var nameError = OptionListValidator.validateOptionListName(optionListName, owner, optionList);
        if (nameError != null) {
            errors.add(nameError, Form.Fields.Name);
        }
        return errors;
    }

    public boolean canCreateOptionList() {
        return options != null && !options.isEmpty();
    }

    public OptionList createOptionList(String name, OptionValue optionValue) {
        optionList = new OptionList(name);
        optionList.addValue(optionValue);
        optionList.save();
        return optionList;
    }

    public void saveOptionList(OptionList optionList) {
        for (OptionValue newValue : options) {
            OptionValue existingValue = optionList.getValue(newValue.getIdx());
            if (existingValue == null) {
                optionList.addValue(newValue);
            } else {
                existingValue.setLabel(newValue.getLabel());
                existingValue.save();
            }
        }
        optionList.reorderValues(options);
        optionList.save();
        view.reloadData();
    }

    public void addOptionValue(String label) {
        List<OptionValue> currentOptions = optionList.getOptionValues();
        int newIdx = currentOptions.isEmpty() ? 1 : currentOptions.get(currentOptions.size() - 1).getIdx() + 1;
        OptionValue newOptionValue = new OptionValue();
        newOptionValue.setIdx(newIdx);
        newOptionValue.setLabel(label);
        optionList.addValue(newOptionValue);
        view.reloadData();
    }

    public void optionValueDelete(OptionValue optionValue) {
        if (optionValue != null) {
            optionValue.delete();
            options.remove(optionValue);
            optionList.reorderValues(options);
            view.reloadData();
        }
    }

    public void reorder() {
        view.updateButtonDisplay(false);
        optionList.reorderValues(optionList.getOptionValues());
    }

    public void duplicate() {
        optionList.duplicate(owner);
        //navigateTo(manageOptionLists);
        //Une fois la copie créée, on revient à la vue de gestion des listes d'options
        // (voir manage_option_lists).
    }

    public void alphabetically() {
        if (optionList != null) {
            optionList.getOptionValues().sort(Comparator.comparing(OptionValue::getLabel));
            optionList.reorderValues(optionList.getOptionValues());
            view.reloadData();
        }
    }
    public void confirmOrder() {
        if (optionList != null && options != null && !options.isEmpty()) {
            optionList.reorderValues(options);
            optionList.save();
            view.reloadData();
        }
        view.updateButtonDisplay(true);
    }

    public void cancelOrder() {
        List<OptionValue> originalOptions = optionList.getOptionValues();
        if (originalOptions != null && !originalOptions.isEmpty()) {
            options.clear();
            options.addAll(originalOptions);
            optionList.reorderValues(options);
            view.reloadData();
        }
        view.updateButtonDisplay(true);
    }

}
