package ru.itis.karimov.service;

import ru.itis.karimov.entity.ConfirmationCodeEntity;

public interface ConfirmationService {
    ConfirmationCodeEntity getOrCreateCode(Long userId);
    boolean confirm(String code);
}
