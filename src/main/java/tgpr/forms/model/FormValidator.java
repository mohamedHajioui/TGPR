package tgpr.forms.model;

import static tgpr.forms.model.Form.getByTitleAndUser;

public class FormValidator {

    public static boolean isTitleUniqueForOwner(String title, User owner) {
        var existingForm = getByTitleAndUser(title, owner);
        return existingForm == null;
    }

    public static String isValidAvailableTitle(String title, User owner, Form form) {
        if (title == null || title.isBlank()) {
            return "title required";
        }
        if (title.length() < 3) {
            return "min 3 characters required";
        }
        if(!title.equals(form.getTitle()) && !isTitleUniqueForOwner(title, owner)) {
            return "title must be unique for this owner";
        }
        return null;
    }

    public static String isValidDescription(String description) {
        if (description != null && !description.isBlank() && description.length() < 3) {
            return "min 3 characters required";
        }
        return null;
    }

}