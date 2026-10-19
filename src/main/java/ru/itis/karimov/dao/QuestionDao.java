package ru.itis.karimov.dao;

import ru.itis.karimov.entity.QuestionEntity;

import java.util.List;

public interface QuestionDao extends CrudDao<QuestionEntity, Integer>{
    List<QuestionEntity> findAllActive();
}
