package ru.itis.karimov.dao;

import ru.itis.karimov.entity.AnswerOptionEntity;
import ru.itis.karimov.entity.QuestionEntity;
import ru.itis.karimov.entity.UniversityEntity;

import java.util.List;

public interface QuestionDao extends CrudDao<QuestionEntity, Integer>{
    List<QuestionEntity> findAllActive();
    QuestionEntity saveWithOptions(QuestionEntity question, List<AnswerOptionEntity> options);
}
