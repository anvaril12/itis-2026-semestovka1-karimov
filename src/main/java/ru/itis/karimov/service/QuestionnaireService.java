package ru.itis.karimov.service;

import ru.itis.karimov.dto.QuestionView;
import java.util.List;

public interface QuestionnaireService {
    List<QuestionView> getQuestionnaire(Long id);
}
