package tgpr.forms.model;

import tgpr.framework.ErrorList;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;
import tgpr.framework.Error;
import tgpr.framework.ErrorList;

public abstract class UserValidator {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static Error isValidMail(String mail) {
        if (mail == null || mail.isBlank())
            return new Error("mail required", User.Fields.Email);
        if (!EMAIL_PATTERN.matcher(mail).matches())
            return new Error("invalid mail", User.Fields.Email);
        return Error.NOERROR;
    }

    public static Error isValidAvailablemail(String mail) {
        var error = isValidMail(mail);
        if (error != Error.NOERROR)
            return error;
        if (User.getByEmail(mail) != null)
            return new Error("not available", User.Fields.Email);
        return Error.NOERROR;
    }

    public static Error isValidPassword(String password) {
        if (password == null || password.isBlank()) {
            return new Error("password required", User.Fields.Password);
        }

        // Nouvelle expression régulière pour accepter les lettres, chiffres et certains caractères spéciaux
        if (!Pattern.matches("[a-zA-Z0-9!@#$%^&*(),.?\":{}|<>]{3,}", password)) {
            return new Error("invalid password (must contain at least 3 characters including letters, digits, or special characters)", User.Fields.Password);
        }

        return Error.NOERROR;
    }


    public static List<Error> validate(User user) {
        var errors = new ErrorList();

        // field validations
        errors.add(isValidMail(user.getName()));


        // cross-fields validations
        if (user.getEmail() != null && !user.getEmail().isBlank() && user.getFullName().equals(user.getFullName()))
            errors.add("profile must be different from pseudo", User.Fields.FullName);

        return errors;
    }

}
