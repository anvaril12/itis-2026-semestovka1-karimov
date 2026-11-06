package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.ConfirmationCodeDao;
import ru.itis.karimov.dao.UserDao;
import ru.itis.karimov.entity.ConfirmationCodeEntity;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.exception.AuthenticationException;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.service.ConfirmationService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

public class ConfirmationServiceImpl implements ConfirmationService {

    private static final int CODE_LIFETIME_MINUTES = 10;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UserDao userDao;
    private final ConfirmationCodeDao confirmationCodeDao;

    public ConfirmationServiceImpl(UserDao userDao, ConfirmationCodeDao confirmationCodeDao) {
        this.userDao = userDao;
        this.confirmationCodeDao = confirmationCodeDao;
    }

    @Override
    public synchronized ConfirmationCodeEntity getOrCreateCode(Long userId) {
        if (userId == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        UserEntity user = userDao.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден по id: " + userId));

        if (user.isBlocked()) {
            throw new AuthenticationException("Аккаунт заблокирован!");
        }
        if (user.isConfirmed()) {
            throw new IllegalStateException("Аккаунт уже подтверждён");
        }

        Optional<ConfirmationCodeEntity> existingCode = confirmationCodeDao.findByUserId(userId);
        if (existingCode.isPresent()) {
            ConfirmationCodeEntity code = existingCode.get();
            if (code.getSendAt().plusMinutes(CODE_LIFETIME_MINUTES).isAfter(LocalDateTime.now())) {
                return code;
            }
            confirmationCodeDao.deleteByUserId(userId);
        }

        ConfirmationCodeEntity code = ConfirmationCodeEntity.builder()
                .userId(userId)
                .code(generateCode())
                .build();
        return confirmationCodeDao.save(code);
    }

    @Override
    public boolean confirm(String code) {
        if (code == null) {
            return false;
        }
        String normalizedCode = code.trim();
        if (!normalizedCode.matches("[0-9]{6}")) {
            return false;
        }
        return confirmationCodeDao.consumeAndConfirm(normalizedCode,
                LocalDateTime.now().minusMinutes(CODE_LIFETIME_MINUTES));
    }

    private String generateCode() {
        String code;
        do {
            code = String.format(Locale.ROOT, "%06d", RANDOM.nextInt(1_000_000));
        } while (confirmationCodeDao.findByCode(code).isPresent());
        return code;
    }
}
