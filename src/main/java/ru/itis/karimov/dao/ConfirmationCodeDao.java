package ru.itis.karimov.dao;

import ru.itis.karimov.entity.ConfirmationCodeEntity;

import java.util.Optional;

public interface ConfirmationCodeDao {
    ConfirmationCodeEntity save(ConfirmationCodeEntity code);
    Optional<ConfirmationCodeEntity> findByCode(String code);
    void deleteByUserId(Long userId);
}
