package ru.itis.karimov.dao;

import ru.itis.karimov.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserDao extends CrudDao<UserEntity, Long> {
    Optional<UserEntity> findByLogin(String login);
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findByDormitoryId(Integer id);
    void updateBlocked(Long userId, boolean blocked);
    List<UserEntity> findByBlocked(boolean blocked);
}
