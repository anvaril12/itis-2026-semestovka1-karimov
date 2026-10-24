package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.UserAnswerDao;
import ru.itis.karimov.dao.UserDao;
import ru.itis.karimov.entity.UserAnswerEntity;
import ru.itis.karimov.service.CompatibilityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompatibilityServiceImpl implements CompatibilityService {
    private final UserDao userDao;
    private final UserAnswerDao userAnswerDao;

    public CompatibilityServiceImpl(UserDao userDao, UserAnswerDao userAnswerDao) {
        this.userDao = userDao;
        this.userAnswerDao = userAnswerDao;
    }

    @Override
    public Integer calculate(List<UserAnswerEntity> mine, List<UserAnswerEntity> other) {
        Map<Integer, UserAnswerEntity> otherByQuestion = new HashMap<>();

        for(UserAnswerEntity u : other) {
            otherByQuestion.put(u.getQuestionId(), u);
        }

        double sum = 0;
        int totalWeight = 0;
        for(UserAnswerEntity u : mine) {

            UserAnswerEntity his = otherByQuestion.get(u.getQuestionId());
            if(his == null) {
                continue;
            }

            boolean sameAnswer = u.getValue().equals(his.getValue());
            if((u.isDealBreaker() || his.isDealBreaker()) && !sameAnswer) {
                return null;
            }

            double similarity = 1 - Math.abs(u.getValue() - his.getValue()) / 4.0;
            int weight = u.getImportance() + his.getImportance();

            sum += similarity * weight;
            totalWeight += weight;
        }
        if (totalWeight == 0) {
            return null;
        }
        return (int) Math.round(sum / totalWeight * 100);
    }
}
