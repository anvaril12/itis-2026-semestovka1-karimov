package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.UserAnswerDao;
import ru.itis.karimov.dao.UserDao;
import ru.itis.karimov.dto.MatchView;
import ru.itis.karimov.entity.UserAnswerEntity;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.service.CompatibilityService;

import java.util.*;

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

    @Override
    public List<MatchView> findMatches(UserEntity me) {
        List<MatchView> result = new ArrayList<>();

        if(me.getDormitoryId() == null) {
            return result;
        }

        List<UserAnswerEntity> answerEntities = userAnswerDao.findByUserId(me.getId());
        if(answerEntities.isEmpty()) {
            return result;
        }
        for(UserEntity candidate : userDao.findByDormitoryId(me.getDormitoryId())) {
            if(candidate.getId().equals(me.getId())) {
                continue;
            }
            if(candidate.isBlocked()) {
                continue;
            }
            if(!candidate.getGender().equals(me.getGender())) {
                continue;
            }
            Integer percent = calculate(answerEntities, userAnswerDao.findByUserId(candidate.getId()));
            if(percent == null) {
                continue;
            }
            result.add(new MatchView(candidate, percent));
        }
        result.sort(Comparator.comparingInt(MatchView::percent).reversed());
        return result;
    }
}
