package tgpr.forms.controller;

import tgpr.forms.model.OptionList;
import tgpr.forms.model.User;
import tgpr.forms.view.ManageOptionListsView;
import tgpr.framework.Controller;

import java.util.List;

public class ManageOptionListsController extends Controller<ManageOptionListsView> {
    private List<OptionList> optionLists;
    private final User currentUser;

    public ManageOptionListsController(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public ManageOptionListsView getView() {
        return new ManageOptionListsView(this);
    }

    public List<OptionList> getOptionLists() {
        return OptionList.getAll();
    }

    public void navigateToOptionList(OptionList optionList){
        navigateTo(new AddEditOptionListController(currentUser, optionList, optionList.getOptionValues()));

    }
}
