package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.QuestionDao;
import ru.itis.karimov.entity.AnswerOptionEntity;
import ru.itis.karimov.entity.QuestionEntity;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.QuestionAdminService;

import java.util.*;

public class QuestionAdminServiceImpl implements QuestionAdminService {

    private final QuestionDao questionDao;

    public QuestionAdminServiceImpl(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public List<QuestionEntity> findAll() {
        return questionDao.findAll();
    }

    @Override
    public QuestionEntity findById(Integer id) {
        Optional<QuestionEntity> question = questionDao.findById(id);
        if(question.isEmpty()) {
            throw new NotFoundException("Не удалось найти вопрос по id: " + id);
        }
        return question.get();
    }

    @Override
    public QuestionEntity create(String text, String[] optionValues,String[] optionLabels) {
        if (optionValues == null || optionLabels == null) {
            throw new ValidationException(Map.of("options","Добавьте минимум 2 варианта ответа"));
        }

        if(optionLabels.length != optionValues.length) {
            throw new ValidationException(Map.of("options", "Некорректные данные вариантов ответа"));
        }

        List<AnswerOptionEntity> list = new ArrayList<>();
        HashSet<Integer> hashSet = new HashSet<>();

        for(int i = 0; i < optionValues.length; i++) {
            String value = optionValues[i];
            String label = optionLabels[i];

            if(value.isBlank() && label.isBlank()) {
                continue;
            }

            if(value.isBlank() || label.isBlank()) {
                throw new ValidationException(Map.of("options", "Укажите и значение и текст варианта"));
            }

            int valueInt;
            try {
                valueInt = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new ValidationException(Map.of("options", "Значение варианта должно быть числом"));
            }

            if(valueInt < 1 || valueInt > 5) {
                throw new ValidationException(Map.of("options", "Значение варианта должно быть от 1 до 5"));
            }

            if(!hashSet.add(valueInt)) {
                throw new ValidationException(Map.of("options", "Значение вариантов не должно повторяться"));
            }

            if(label.trim().length() > 100) {
                throw new ValidationException(Map.of("options", "Текст варианта не должен превышать 100 символов"));
            }
            AnswerOptionEntity answerOptionEntity = AnswerOptionEntity.builder()
                    .value(valueInt)
                    .label(label.trim())
                    .build();
            list.add(answerOptionEntity);
        }

        if(text == null) {
            throw new ValidationException(Map.of("text", "Введите текст вопроса"));
        }

        if(text.isBlank()) {
            throw new ValidationException(Map.of("text", "Текст вопроса не должен состоять только из пробелов"));
        }

        if(text.trim().length() > 255) {
            throw new ValidationException(Map.of("text", "Текст вопроса не должен быть больше 255 символов"));
        }

        if(hashSet.size() < 2) {
            throw new ValidationException(Map.of("options", "Должно быть минимум 2 варианта ответа"));
        }

        String newText = text.trim();
        QuestionEntity questionEntity = QuestionEntity.builder()
                .text(newText)
                .isActive(false)
                .build();

        return questionDao.saveWithOptions(questionEntity, list);
    }

    @Override
    public void update(Integer id, String text) {
        QuestionEntity questionEntity = findById(id);

        if(text == null) {
            throw new ValidationException(Map.of("text", "Введите текст вопроса"));
        }

        if(text.isBlank()) {
            throw new ValidationException(Map.of("text", "Текст вопроса не должен состоять только из пробелов"));
        }

        if(text.trim().length() > 255) {
            throw new ValidationException(Map.of("text", "Текст вопроса не должен быть больше 255 символов"));
        }

        String newText = text.trim();
        questionEntity.setText(newText);
        questionDao.update(questionEntity);
    }

    @Override
    public void delete(Integer id) {
        QuestionEntity questionEntity = findById(id);
        questionEntity.setActive(false);
        questionDao.update(questionEntity);
    }
}
