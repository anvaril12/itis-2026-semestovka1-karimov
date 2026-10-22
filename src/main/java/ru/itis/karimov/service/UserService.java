package ru.itis.karimov.service;

import ru.itis.karimov.dto.RegistrationForm;
import ru.itis.karimov.entity.UserEntity;

public interface UserService {
    UserEntity register(RegistrationForm form);
}
