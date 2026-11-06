package ru.itis.karimov.service;

import ru.itis.karimov.dto.RegistrationForm;
import ru.itis.karimov.entity.UserEntity;

public interface UserService {
    UserEntity register(RegistrationForm form);
    UserEntity login(String login, String password);
    UserEntity updateDormitory(Long userId, String dormitoryId, String universityId);
    UserEntity getActiveUser(Long userId);
}
