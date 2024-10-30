package tgpr.forms.model;

import static tgpr.forms.model.OptionList.getByNameAndUser;
public class OptionListValidator {

    public static String isValidOptionListName(String name, User owner, OptionList optionList) {
        if (name == null || name.isBlank()) {
            return "name required";
        }
        if (name.length() < 3) {
            return "min 3 characters required";
        }
        if (!name.equals(optionList.getName()) && !isUniqueForOwner(name, owner)) {
            return " optionList name must be unique for this owner";
        }
        return null;
    }

    private static boolean isUniqueForOwner(String name, User owner) {
        var existingOptionList = getByNameAndUser(name, owner);
        return existingOptionList == null;
    }
}
