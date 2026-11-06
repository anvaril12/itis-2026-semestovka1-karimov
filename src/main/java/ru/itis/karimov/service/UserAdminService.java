package ru.itis.karimov.service;

import ru.itis.karimov.entity.UserEntity;
import java.util.List;

public interface UserAdminService {
    List<UserEntity> findAll();
    List<UserEntity> findByBlocked(boolean blocked);
    void updateBlocked(Long userId, boolean blocked);
}
