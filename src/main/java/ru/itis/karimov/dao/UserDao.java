package ru.itis.karimov.dao;

import ru.itis.karimov.entity.UserEntity;

import java.util.Optional;

public interface UserDao extends CrudDao<UserEntity, Long> {
    Optional<UserEntity> findByLogin(String login);
    Optional<UserEntity> findByEmail(String email);
}
