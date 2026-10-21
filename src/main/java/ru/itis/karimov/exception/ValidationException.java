package ru.itis.karimov.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class ValidationException extends RuntimeException {
    private final Map<String, String> error;

    public ValidationException(Map<String, String> error) {
        super("Ошибка валидации");
        this.error = error;
    }
}
