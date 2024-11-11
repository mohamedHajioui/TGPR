
package tgpr.forms.controller;

import tgpr.forms.model.*;
import tgpr.forms.view.AddEditOptionListView;
import tgpr.framework.Controller;
import tgpr.framework.ErrorList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static tgpr.forms.model.OptionList.getAll;

public class AddEditOptionListController extends Controller<AddEditOptionListView> {
    private AddEditOptionListView view;
    private OptionList optionList;
    private User owner;
    private OptionValue optionValue;
    private List<OptionValue> options;
    List<OptionValue> optionsToDelete = new ArrayList<>();
    List<OptionValue> originalOptionList;
    private List<OptionValue> tempOptions = new ArrayList<>();
    private boolean isModified = false;


    public AddEditOptionListController(User owner, OptionList optionList, List<OptionValue> options) {
        this.owner = owner;
        this.optionList = optionList;
        this.options = (options != null) ? options : (optionList != null ? optionList.getOptionValues() : new ArrayList<>());
        this.tempOptions = new ArrayList<>(this.options);
        this.view = new AddEditOptionListView(this, owner, optionList);

        if (this.optionList != null) {
            view.initialize();
        }
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

    public static int getNextAvailableId() {
        List<OptionList> optionLists = getAll();
        if (optionLists.isEmpty()) {
            return 1;
        }
        int nextId = 1;
        for (OptionList optionList : optionLists) {
            if (optionList.getId() != nextId) {
                return nextId;
            }
            nextId++;
        }
        return nextId;
    }

    public OptionList createNewOptionList(String name) {  // méthode pour le bouton new List dans Manage Option List
        OptionList newOptionList = new OptionList(name);
        int nextId = getNextAvailableId();
        newOptionList.setId(nextId);
        newOptionList.setOwnerId(owner.getId());
        this.optionList = newOptionList;
        view.reloadData();
        return newOptionList;
    }

    public void createAndSaveOptionList(String name) {
        OptionList newOptionList = new OptionList(name);
        newOptionList.setOwnerId(owner.getId());
        newOptionList.save();
        this.optionList = newOptionList;
        saveOptionValue();
    }

    public void addOptionValue(String label) {
        OptionValue newOptionValue = new OptionValue(optionList, options.size() + 1, label);
        if (optionList.hasValue(newOptionValue)) {
            showError("Option already exists in the list");
            return; // je préfère désactiver le bouton add dans ce cas !!!
        }
        newOptionValue.save();
        options.add(newOptionValue);
        view.reloadData();
        isModified = true;
    }

    public void saveOptionValue() {
        for (OptionValue optionValue : options) {
            optionValue.setOptionListId(optionList.getId());
            optionValue.save();
        }
    }
    public void addOptionInMemory(String label) {
        if (isOptionDuplicate(label)) {
            showError("Option already exists in the list");
            return;
        }
        int newIndex = tempOptions.size() + 1;
        OptionValue newOptionValue = new OptionValue(optionList, newIndex, label);
        tempOptions.add(newOptionValue);
        isModified = true;
        view.reloadData();
        view.updateCreateButtonState();
    }
    public List<OptionValue> getTempOptions() {
        return tempOptions;
    }
    public boolean isOptionDuplicate(String label) {
        return options.stream().anyMatch(option -> option.getLabel().equals(label));
    }
        public boolean enableCreateButton() {
        return !optionList.getOptionValues().isEmpty();
    }

    public void save() {
        for (OptionValue optionValue : optionsToDelete) {
            optionValue.delete();
        }
        optionsToDelete.clear();

        reindexOptions();

        optionList.deleteAllValues();

        for (OptionValue option : tempOptions) {
            option.setOptionListId(optionList.getId());
            option.save();
        }

        saveOptionList();
        isModified = false;
    }
    private void saveOptionList() {
        optionList.save();
    }
    public void deleteOptionList(OptionList optionList) {   // A TESTER !!!
        if (!askConfirmation("Are you sure you want to delete this option list?", "Delete")) {
            return;
        }
        for (OptionValue value : optionList.getOptionValues()) {
            value.delete();
        }
        optionList.delete();
        view.close();
    }
    /*
    public void deleteOptionList(OptionList optionList) {
        if (!canDeleteOptionList(optionList)) {
            view.showError("You cannot delete this option list because it's in use or you lack permissions.");
            return;
        }
        boolean confirmDelete = askConfirmation("Are you sure you want to delete this option list?", "Delete");
        if (confirmDelete) {
            optionList.deleteAllValues; // Supprime toutes les valeurs associées
            optionList.delete(); // Supprime la liste
            view.close(); // Ferme la vue après suppression
        }
    }
     */

    public boolean canDeleteOptionList(OptionList optionList) {
        return owner.isAdmin() && !optionList.isUsed() && !optionList.isSystem();
    }

    public void reindexOptions() {
        for (int i = 0; i < tempOptions.size(); i++) {
            OptionValue option = tempOptions.get(i);
            option.setIdx(i + 1);
        }
        view.reloadData();
    }

    public void reorder() {
        view.updateButtonDisplay(false);
        isModified = true;
    }
    public void alphabetically() {
        options.sort(Comparator.comparing(OptionValue::getLabel));
        reindexOptions();
        view.reloadData();
    }

    public void reindexInMemory(List<OptionValue> values) {
        int i = 1;
        for (var value : values) {
            value.setIdx(i++);
        }
    }
    public void confirmOrder() {
        optionList.reorderValues(options);
        isModified = false;
        view.updateButtonDisplay(true);
    }
    public void duplicate() {
        optionList.duplicate(owner);
        //navigateTo(manageOptionLists);
        //Une fois la copie créée, on revient à la vue de gestion des listes d'options
        // (voir manage_option_lists).
        /*
        OptionList duplicateList = optionList.duplicate(owner);
        navigateTo(ManageOptionListsView.class);
         */
    }
    public void cancelOrder() {
        options = new ArrayList<>(originalOptionList);
        view.reloadData();
        view.updateButtonDisplay(true);
    }

    public void handleToggleSystem(boolean isSystem) {
        if (optionList != null) {
            optionList.setOwnerId(isSystem ? null : owner.getId());
            optionList.save();
            view.reloadData();
        }
    }

    public void onOptionListModified() {
        isModified = true;
    }

    public void closeAll() {
        System.out.println("isModified: " + isModified);
        if (isModified) {
            boolean confirmDiscard = askConfirmation("Are you sure you want to cancel?", "Cancel");
            if (confirmDiscard) {
                discardChanges();
                view.close();
            }
        } else {
            view.close();
        }
    }

    private void discardChanges() {
//        tempOptions.clear();
//        tempOptions.addAll(options);
        tempOptions = new ArrayList<>(options);
        reindexOptions();
        isModified = false;
        view.reloadData();
    }

    public void addToDeleteList(OptionValue option) {
        optionsToDelete.add(option);
        isModified = true;
    }

    public List<OptionValue> loadOptions(OptionList optionList) {
        return optionList.getOptionValues();
    }
}
