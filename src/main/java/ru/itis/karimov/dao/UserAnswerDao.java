package ru.itis.karimov.dao;

import ru.itis.karimov.entity.UserAnswerEntity;

import java.util.List;

public interface UserAnswerDao {
    List<UserAnswerEntity> findByUserId(Long userId);
    void save(UserAnswerEntity answer);
}
