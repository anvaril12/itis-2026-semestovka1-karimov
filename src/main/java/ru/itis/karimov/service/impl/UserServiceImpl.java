package ru.itis.karimov.service.impl;

import org.mindrot.jbcrypt.BCrypt;
import ru.itis.karimov.dao.UserDao;
import ru.itis.karimov.dto.RegistrationForm;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.entity.enums.Gender;
import ru.itis.karimov.entity.enums.Role;
import ru.itis.karimov.exception.AuthenticationException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.UserService;
import ru.itis.karimov.validation.RegistrationValidator;
import java.util.Map;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final RegistrationValidator validator = new RegistrationValidator();
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserEntity register(RegistrationForm form) {
        Map<String, String> errors = validator.validate(form);

        if(!errors.containsKey("email") && userDao.findByEmail(form.email()).isPresent()) {
            errors.put("email", "Такой email уже занят");
        }
        if(!errors.containsKey("login") && userDao.findByLogin(form.login()).isPresent()) {
            errors.put("login", "Такой логин уже занят");
        }
        if(!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
           UserEntity user = UserEntity.builder()
                   .login(form.login())
                   .firstName(form.firstName())
                   .lastName(form.lastName())
                   .hashPassword(BCrypt.hashpw(form.password(), BCrypt.gensalt()))
                   .email(form.email())
                   .gender(Gender.valueOf(form.gender()))
                   .contacts(form.contacts())
                   .role(Role.USER)
                   .course(Integer.parseInt(form.course()))
                   .birthYear(Integer.parseInt(form.birthYear()))
                   .build();
        return userDao.save(user);
    }

    @Override
    public UserEntity login(String login, String password) {
        String message = "Неверный логин или пароль";

        if(login == null || login.isBlank() || password == null || password.isBlank()) {
            throw new AuthenticationException(message);
        }

        Optional<UserEntity> optionalUser = userDao.findByLogin(login);

        if(optionalUser.isEmpty()) {
            throw new AuthenticationException(message);
        }

        UserEntity userEntity = optionalUser.get();

        if(!BCrypt.checkpw(password, userEntity.getHashPassword())) {
            throw new AuthenticationException(message);
        }
        return userEntity;
    }
}
