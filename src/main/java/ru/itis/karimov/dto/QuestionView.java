package ru.itis.karimov.dto;

import ru.itis.karimov.entity.AnswerOptionEntity;
import ru.itis.karimov.entity.QuestionEntity;
import ru.itis.karimov.entity.UserAnswerEntity;

import java.util.List;

public record QuestionView(
        QuestionEntity question,
        List<AnswerOptionEntity> options,
        UserAnswerEntity userAnswer
) {
}
