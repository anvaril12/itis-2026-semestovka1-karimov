package ru.itis.karimov.dao;

import ru.itis.karimov.entity.ConfirmationCodeEntity;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ConfirmationCodeDao {
    ConfirmationCodeEntity save(ConfirmationCodeEntity code);
    Optional<ConfirmationCodeEntity> findByCode(String code);
    void deleteByUserId(Long userId);
    Optional<ConfirmationCodeEntity> findByUserId(Long userId);
    boolean consumeAndConfirm(String code, LocalDateTime validSince);
}
