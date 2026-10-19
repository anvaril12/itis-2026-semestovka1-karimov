package ru.itis.karimov.dao;

import ru.itis.karimov.entity.AnswerOptionEntity;

import java.util.List;

public interface AnswerOptionDao extends CrudDao<AnswerOptionEntity, Integer> {
    List<AnswerOptionEntity> findByQuestionId(Integer questionId);
}
