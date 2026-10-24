package ru.itis.karimov.service;

import ru.itis.karimov.entity.UserAnswerEntity;

import java.util.List;

public interface CompatibilityService {
    Integer calculate(List<UserAnswerEntity> mine, List<UserAnswerEntity> other);
}
