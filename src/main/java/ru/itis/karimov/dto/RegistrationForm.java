package ru.itis.karimov.dto;

public record RegistrationForm(
        String login,
        String firstName,
        String lastName,
        String password,
        String passwordConfirm,
        String email,
        String gender,
        String contacts,
        String course,
        String birthYear
) {
}
