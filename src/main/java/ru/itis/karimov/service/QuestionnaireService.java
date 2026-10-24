package ru.itis.karimov.service;

import ru.itis.karimov.dto.QuestionView;
import java.util.List;
import java.util.Map;

public interface QuestionnaireService {
    List<QuestionView> getQuestionnaire(Long id);
    void saveAnswers(Long id, Map<String, String[]> params);
}
