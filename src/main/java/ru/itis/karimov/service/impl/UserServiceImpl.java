package ru.itis.karimov.service.impl;

import org.mindrot.jbcrypt.BCrypt;
import ru.itis.karimov.dao.DormitoryDao;
import ru.itis.karimov.dao.UserDao;
import ru.itis.karimov.dto.RegistrationForm;
import ru.itis.karimov.entity.DormitoryEntity;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.entity.enums.Gender;
import ru.itis.karimov.entity.enums.Role;
import ru.itis.karimov.exception.AuthenticationException;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.UserService;
import ru.itis.karimov.validation.RegistrationValidator;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final RegistrationValidator validator = new RegistrationValidator();
    private final UserDao userDao;
    private final DormitoryDao dormitoryDao;

    public UserServiceImpl(UserDao userDao, DormitoryDao dormitoryDao) {
        this.userDao = userDao;
        this.dormitoryDao = dormitoryDao;
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

        if(password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new AuthenticationException("Неверный логин или пароль");
        }

        Optional<UserEntity> optionalUser = userDao.findByLogin(login);

        if(optionalUser.isEmpty()) {
            throw new AuthenticationException(message);
        }

        UserEntity userEntity = optionalUser.get();

        if(!BCrypt.checkpw(password, userEntity.getHashPassword())) {
            throw new AuthenticationException(message);
        }

        if(userEntity.isBlocked()) {
            throw new AuthenticationException("Ошибка входа в аккаунт: он заблокирован!");
        }
        return userEntity;
    }

    @Override
    public UserEntity updateDormitory(Long userId, String dormitoryId, String universityId) {
        int uniId;
        int dormId;
        try {
            uniId = Integer.parseInt(universityId);
            dormId = Integer.parseInt(dormitoryId);
        } catch(NumberFormatException e) {
            throw new ValidationException(Map.of("dormitory", "Выберите общежитие"));
        }

        Optional<DormitoryEntity> dormitoryEntity = dormitoryDao.findById(dormId);
        if(dormitoryEntity.isEmpty()) {
            throw new ValidationException(Map.of("dormitory", "Выберите общежитие"));
        }

        DormitoryEntity dormitory = dormitoryEntity.get();

        if(uniId != dormitory.getUniversityId()) {
            throw new ValidationException(Map.of("dormitory", "Общежитие не относится к выбранному университету"));
        }

        Optional<UserEntity> userEntity = userDao.findById(userId);
        if(userEntity.isEmpty()) {
            throw new IllegalStateException("Пользователь не найден");
        }

        UserEntity user = userEntity.get();
        user.setDormitoryId(dormId);
        userDao.update(user);
        return user;
     }

    @Override
    public UserEntity getActiveUser(Long userId) {
        Optional<UserEntity> optional = userDao.findById(userId);
        if(optional.isEmpty()) {
            throw new NotFoundException("Не удалось найти пользователя по id: " + userId);
        }

        UserEntity user = optional.get();
        if(user.isBlocked()) {
            throw new AuthenticationException("Аккаунт заблокирован!");
        }

        return user;
     }
}
