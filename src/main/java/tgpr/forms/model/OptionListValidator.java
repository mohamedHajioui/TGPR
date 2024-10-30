package tgpr.forms.model;

import java.util.ArrayList;
import java.util.List;

import static tgpr.forms.model.OptionList.getByNameAndUser;
public class OptionListValidator {

    public static String validateOptionListName(String name, User owner, OptionList optionList) {
        List<String> errors = new ArrayList<>();

        if (name == null || name.isBlank()) {
            errors.add("Name required");
        }
        if (name.length() < 3) {
            errors.add("min 3 characters required");
        }
        if (!name.equals(optionList.getName()) && !isUniqueForOwner(name, owner)) {
            errors.add("Name must be unique for this owner");
        }
        return null;
    }

    private static boolean isUniqueForOwner(String name, User owner) {
        var existingOptionList = getByNameAndUser(name, owner);
        return existingOptionList == null;
    }
}
