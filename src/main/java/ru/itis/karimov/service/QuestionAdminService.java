package ru.itis.karimov.service;

import ru.itis.karimov.entity.QuestionEntity;

import java.util.List;

public interface QuestionAdminService {
    List<QuestionEntity> findAll();
    QuestionEntity findById(Integer id);
    QuestionEntity create(String text, String[] optionValues, String[] optionLabels);
    void update(Integer id, String text);
    void delete(Integer id);
    void activate(Integer id);
}
