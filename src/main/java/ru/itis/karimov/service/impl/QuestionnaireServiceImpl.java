package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.AnswerOptionDao;
import ru.itis.karimov.dao.QuestionDao;
import ru.itis.karimov.dao.UserAnswerDao;
import ru.itis.karimov.dto.QuestionView;
import ru.itis.karimov.entity.AnswerOptionEntity;
import ru.itis.karimov.entity.QuestionEntity;
import ru.itis.karimov.entity.UserAnswerEntity;
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
}
