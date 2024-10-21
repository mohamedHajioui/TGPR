package tgpr.forms.model;

public class FormValidator {

    public static String isValidAvailableTitle(String title) {
        if (title == null || title.isBlank()) {
            return "title required";
        }
        if (title.length() < 3) {
            return "min 3 characters required";
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