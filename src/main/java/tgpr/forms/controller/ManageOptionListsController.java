package tgpr.forms.controller;

import tgpr.forms.model.OptionList;
import tgpr.forms.view.ManageOptionListsView;
import tgpr.framework.Controller;

import java.util.List;

public class ManageOptionListsController extends Controller<ManageOptionListsView> {
    private List<OptionList> optionLists;
    @Override
    public ManageOptionListsView getView() {
        return new ManageOptionListsView(this);
    }
    public List<OptionList> getOptionLists() {
        return OptionList.getAll();
    }
    public void navigateToOptinList(OptionList optionList){
        navigateTo(new TestController());

    }
}
