
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
    List<OptionValue> optionsToDelete = new ArrayList<>();
    List<OptionValue> originalOptionList;
    private List<OptionValue> tempOptions = new ArrayList<>();


    public AddEditOptionListController(User owner, OptionList optionList, List<OptionValue> options) {
        this.owner = owner;
        this.optionList = optionList;
        this.options = (options != null) ? options : new ArrayList<>();
        view = new AddEditOptionListView(this, owner, optionList);
    }

    public AddEditOptionListController() {
        this.optionList = new OptionList();
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

    public ErrorList validate(String optionListName) {
        var errors = new ErrorList();

        var nameError = OptionListValidator.validateOptionListName(optionListName, owner, optionList);
        if (nameError != null) {
            errors.add(nameError, Form.Fields.Name);
        }
        return errors;
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

    public OptionList createOptionList(String name, String label) {
        optionList = new OptionList();
        optionList.setName(name);
        optionList = optionList.save();
        OptionValue firstOptionValue = new OptionValue();
        firstOptionValue.setIdx(1);
        firstOptionValue.setLabel(label);
        optionList.addValue(firstOptionValue);
        optionList.save();

        return optionList;
    }

    public void addToDeleteList(OptionValue option) {
        optionsToDelete.add(option);
    }

    public void save(OptionList optionList) {
        optionList.save();
/*        for (OptionValue option : optionsToDelete) {
            option.delete();
        }
        optionsToDelete.clear();
        int index = 1;
        for (OptionValue option : options) {
            option.setIdx(index++);
            option.save();
        }
        optionList.save();
        view.reloadData();
        view.close(); //à mettre ou pas???
*/
    }
    public void initializeOptions() {
        tempOptions = new ArrayList<>(optionList.getOptionValues());
        view.reload(tempOptions);
    }

    public void addOptionValue(String label) {
        if (optionList == null) {
            view.createOptionList();
        }
//        List<OptionValue> currentOptions = optionList.getOptionValues();
//        int newIdx = currentOptions.isEmpty() ? 1 : currentOptions.getLast().getIdx() + 1;
        int newIdx = tempOptions.isEmpty() ? 1 : tempOptions.getLast().getIdx() + 1;
        OptionValue newOptionValue = new OptionValue(optionList, newIdx, label);
        tempOptions.add(newOptionValue);
        //optionList.addValue(newOptionValue);
        //newOptionValue.save();
        view.reload(tempOptions);
    }
    public void saveAllOptionValues() {
        //optionList.clearValuesInDatabase();
        for (OptionValue option : tempOptions) {
            optionList.addValue(option);
        }
        tempOptions.clear();
        initializeOptions();
    }

    public void deleteOptionList(OptionList optionList) {
        if (owner.isAdmin() && !optionList.isUsed()) {
            return;
        }
        if (askConfirmation("Are you sure you want to delete this option list?", "Delete")) {
            return;
        }
        for (OptionValue value : optionList.getOptionValues()) {
            value.delete();
        }
        optionList.delete();

        view.reloadData(); // refresh de l'interface
    }

    public void deleteOptionValue(OptionValue value) {
        tempOptions.remove(value);
        reindexTempOptions();
        view.reload(tempOptions);
    }

    public void reindexTempOptions() {
        for (int i = 0; i < tempOptions.size(); i++) {
            tempOptions.get(i).setIdx(i + 1);
        }
    }
    public void reorder() {
        view.updateButtonDisplay(false);
        optionList.reorderValues(optionList.getOptionValues());
    }

    public void alphabetically() {
        if (optionList != null) {
            optionList.getOptionValues().sort(Comparator.comparing(OptionValue::getLabel));
            optionList.reorderValues(optionList.getOptionValues());
            reindexInMemory(optionList.getOptionValues());
            view.reloadData();
        }
    }

    public void reindexInMemory(List<OptionValue> values) {
        int i = 1;
        for (var value : values) {
            value.setIdx(i++);
        }
    }
    public void confirmOrder() {
        if (options != null && !options.isEmpty()){

        }
        view.updateButtonDisplay(true);
    }
    public void duplicate() {
        optionList.duplicate(owner);
        //navigateTo(manageOptionLists);
        //Une fois la copie créée, on revient à la vue de gestion des listes d'options
        // (voir manage_option_lists).
    }
    public void cancelOrder() {
        if (optionList != null && optionList.getOptionValues() != null) {
            optionList.setValues(originalOptionList);
            reindexInMemory(optionList.getOptionValues());
            view.reloadData();
        }
        view.updateButtonDisplay(true);
    }

    /*public void deleteOptionValue(OptionValue option) {
        options.remove(option);
        reindexInMemory(options);
        view.reloadData();
    }
     */

    public void saveOptionList() {
        for (OptionValue option : options) {
            option.save();
        }
        optionList.save();
        view.reloadData();
    }
/*
    public void closeWindow() {
        if (changesPending()) {
            boolean saveChanges = view.confirmSaveChanges();
            if (saveChanges) {
                saveOptionList();
            }
        }
        view.close();
    }


 */
    private boolean changesPending() {
        return true;
    }

    public void handleToggleSystem(boolean isSystem) {
        if (optionList != null) {
            if (isSystem) {
                optionList.setOwnerId(null);
            } else {
                optionList.setOwnerId(owner.getId());
            }
            optionList.save();

            view.reloadData();
        }
    }

}
