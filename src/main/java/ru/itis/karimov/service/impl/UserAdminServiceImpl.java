package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.UserDao;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.entity.enums.Role;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.UserAdminService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserAdminServiceImpl implements UserAdminService {

    private final UserDao userDao;

    public UserAdminServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<UserEntity> findAll() {
        return userDao.findAll();
    }

    @Override
    public List<UserEntity> findByBlocked(boolean blocked) {
        return userDao.findByBlocked(blocked);
    }

    @Override
    public void updateBlocked(Long userId, boolean blocked) {
        if(userId == null) {
            throw new NotFoundException("Не удалось найти пользователя");
        }

        Optional<UserEntity> optional = userDao.findById(userId);
        if(optional.isEmpty()) {
            throw new NotFoundException("Не удалось найти пользователя по id:" + userId);
        }
        UserEntity user = optional.get();

        if(user.getRole().equals(Role.ADMIN)) {
            throw new ValidationException(Map.of("users", "Нельзя менять статус у админа"));
        }

        userDao.updateBlocked(userId, blocked);
    }
}
