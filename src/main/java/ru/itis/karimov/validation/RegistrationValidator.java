package ru.itis.karimov.validation;

import ru.itis.karimov.dto.RegistrationForm;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class RegistrationValidator {

    public Map<String, String> validate(RegistrationForm form) {
        Map<String, String> errors = new HashMap<>();

        String login = form.login();
        if(isBlank(login)) {
            errors.put("login", "Введите логин");
        } else if (login.length() < 3) {
            errors.put("login", "Длина логина не должна быть меньше 3 символов");
        } else if(login.length() > 12) {
            errors.put("login", "Длина логина не должна быть больше 12 символов");
        } else if (!login.matches("[a-zA-Z0-9_]+")) {
            errors.put("login", "Только латиница, цифры и _");
        }

        String firstName = form.firstName();
        if (isBlank(firstName)) {
            errors.put("firstName", "Введите имя");
        } else if (firstName.length() > 50) {
            errors.put("firstName", "Длина имени не должна быть больше 50 символов");
        }

        String lastName = form.lastName();
        if(isBlank(lastName)) {
            errors.put("lastName", "Введите фамилию");
        } else if(lastName.length() > 50) {
            errors.put("lastName", "Длина фамилии не должна быть больше 50 символов");
        }

        String email = form.email();
        if(isBlank(email)) {
            errors.put("email", "Введите email");
        } else if(email.length() > 50) {
            errors.put("email", "Длина email должна быть не больше 50 символов");
        } else if(!email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            errors.put("email", "Некорректный email");
        }

        String password = form.password();
        if(isBlank(password)) {
            errors.put("password", "Введите пароль");
        } else if(password.length() < 8) {
            errors.put("password", "Пароль не должен быть меньше 8 символов");
        } else if(password.length() > 72) {
            errors.put("password", "Пароль не должен быть больше 72 символов");
        }

        String passwordConfirm = form.passwordConfirm();
        if(isBlank(passwordConfirm)) {
            errors.put("passwordConfirm", "Введите повторный пароль");
        } else if(!passwordConfirm.equals(password)) {
            errors.put("passwordConfirm", "Пароли не совпадают");
        }

        String contacts = form.contacts();
        if(isBlank(contacts)) {
            errors.put("contacts", "Введите номер телефона");
        } else if(!contacts.matches("89\\d{9}|\\+79\\d{9}")) {
            errors.put("contacts", "Некорректный формат номера телефона");
        }

        String course = form.course();
        if(isBlank(course)) {
            errors.put("course", "Введите номер курса");
        } else {
            try {
                int c = Integer.parseInt(course);
                if (c < 1 || c > 6) {
                    errors.put("course", "Курс должен быть от 1 до 6");
                }
            } catch (NumberFormatException e) {
                errors.put("course", "Курс должен быть числом");
            }
        }

        String gender = form.gender();
        if (isBlank(gender)) {
            errors.put("gender", "Выберите пол");
        } else if (!gender.equals("MALE") && !gender.equals("FEMALE")) {
            errors.put("gender", "Некорректное значение пола");
        }

        String birthYear = form.birthYear();
        if(isBlank(birthYear)) {
            errors.put("birthYear", "Введите год рождения");
        } else {
            try {
                int year = Integer.parseInt(birthYear);
                if(year < 1990 || year > LocalDate.now().getYear() - 15) {
                    errors.put("birthYear", "Некорректный год рождения");
                }
            } catch (NumberFormatException e) {
                errors.put("birthYear", "Год рождения должен быть числом");
            }
        }
        return errors;
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
