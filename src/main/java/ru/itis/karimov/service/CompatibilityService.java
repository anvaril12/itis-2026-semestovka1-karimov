package ru.itis.karimov.service;

import ru.itis.karimov.dto.MatchView;
import ru.itis.karimov.entity.UserAnswerEntity;
import ru.itis.karimov.entity.UserEntity;

import java.util.List;

public interface CompatibilityService {
    Integer calculate(List<UserAnswerEntity> mine, List<UserAnswerEntity> other);
    List<MatchView> findMatches(UserEntity me);
}
