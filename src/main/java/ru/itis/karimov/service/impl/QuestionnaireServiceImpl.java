package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.AnswerOptionDao;
import ru.itis.karimov.dao.QuestionDao;
import ru.itis.karimov.dao.UserAnswerDao;
import ru.itis.karimov.dto.QuestionView;
import ru.itis.karimov.entity.AnswerOptionEntity;
import ru.itis.karimov.entity.QuestionEntity;
import ru.itis.karimov.entity.UserAnswerEntity;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.QuestionnaireService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionnaireServiceImpl implements QuestionnaireService {

    private final QuestionDao questionDao;
    private final AnswerOptionDao answerOptionDao;
    private final UserAnswerDao userAnswerDao;

    public QuestionnaireServiceImpl(QuestionDao questionDao, AnswerOptionDao answerOptionDao, UserAnswerDao userAnswerDao) {
        this.questionDao = questionDao;
        this.answerOptionDao = answerOptionDao;
        this.userAnswerDao = userAnswerDao;
    }

    @Override
    public List<QuestionView> getQuestionnaire(Long id) {
        Map<Integer, UserAnswerEntity> answer = new HashMap<>();
        for(UserAnswerEntity a : userAnswerDao.findByUserId(id)) {
            answer.put(a.getQuestionId(), a);
        }

        List<QuestionView> list = new ArrayList<>();
        for(QuestionEntity q : questionDao.findAllActive()) {
            List<AnswerOptionEntity> options = answerOptionDao.findByQuestionId(q.getId());
            UserAnswerEntity userAnswerEntity = answer.get(q.getId());
            list.add(new QuestionView(q, options, userAnswerEntity));
        }
        return list;
    }

    @Override
    public void saveAnswers(Long id, Map<String, String[]> params) {
        Map<String, String> errors = new HashMap<>();
        List<UserAnswerEntity> answers = new ArrayList<>();

        for(QuestionEntity q : questionDao.findAllActive()) {
            String valueParam = first(params, "value_" + q.getId());
            String importanceParam = first(params, "importance_" + q.getId());
            Boolean is_dealbreaker = params.containsKey("dealbreaker_" + q.getId());

            Integer value = null;
            try {
                value = Integer.parseInt(valueParam);
            } catch(NumberFormatException e) {

            }

            boolean optionExists = false;
            if(value != null) {
                for(AnswerOptionEntity option : answerOptionDao.findByQuestionId(q.getId())) {
                    if(option.getValue().equals(value)) {
                        optionExists = true;
                        break;
                    }
                }
            }

            if(!optionExists) {
                errors.put("value_" + q.getId(), "Выберите вариант ответа");
                continue;
            }

            Integer importance = null;
            try {
                importance = Integer.parseInt(importanceParam);
            } catch(NumberFormatException e) {

            }

            if (importance == null || importance < 0 || importance > 10) {
                errors.put("importance_" + q.getId(), "Важность - значение от 0 до 10");
                continue;
            }

            UserAnswerEntity userAnswerEntity  = UserAnswerEntity.builder()
                    .userId(id)
                    .questionId(q.getId())
                    .value(value)
                    .importance(importance)
                    .isDealBreaker(is_dealbreaker)
                    .build();
            answers.add(userAnswerEntity);
        }
        if(!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        for(UserAnswerEntity u : answers) {
            userAnswerDao.save(u);
        }
    }

    private String first(Map<String, String[]> params, String name) {
        String[] values = params.get(name);
        return values == null ? null : values[0];
    }
}
