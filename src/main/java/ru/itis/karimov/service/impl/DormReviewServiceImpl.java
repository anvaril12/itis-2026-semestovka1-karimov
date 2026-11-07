package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.DormReviewDao;
import ru.itis.karimov.dao.DormitoryDao;
import ru.itis.karimov.entity.DormReviewEntity;
import ru.itis.karimov.exception.ForbiddenException;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.DormReviewService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DormReviewServiceImpl implements DormReviewService {

    private final DormReviewDao dormReviewDao;
    private final DormitoryDao dormitoryDao;

    public DormReviewServiceImpl(DormReviewDao dormReviewDao, DormitoryDao dormitoryDao) {
        this.dormReviewDao = dormReviewDao;
        this.dormitoryDao = dormitoryDao;
    }

    @Override
    public List<DormReviewEntity> findAll() {
        return dormReviewDao.findAll();
    }

    @Override
    public List<DormReviewEntity> findByDormitoryId(Integer dormitoryId) {
        return dormReviewDao.findByDormitoryId(dormitoryId);
    }

    @Override
    public DormReviewEntity save(DormReviewEntity dormReviewEntity) {
        validateDormReview(dormReviewEntity);

        if(dormitoryDao.findById(dormReviewEntity.getDormitoryId()).isEmpty()) {
            throw new ValidationException(Map.of("review", "Общежитие не найдено"));
        }

        if(dormReviewDao.findByUserIdAndDormitoryId(dormReviewEntity.getUserId(), dormReviewEntity.getDormitoryId()).isPresent()) {
            throw new ValidationException(Map.of("review", "Вы уже оставляли отзыв на это общежитие"));
        }

        dormReviewEntity.setText(dormReviewEntity.getText().trim());

        return dormReviewDao.save(dormReviewEntity);

    }

    @Override
    public void update(Long userId, DormReviewEntity dormReviewEntity) {
        Optional<DormReviewEntity> optional = dormReviewDao.findById(dormReviewEntity.getId());
        if(optional.isEmpty()) {
            throw new NotFoundException("Не удалось найти отзыв");
        }

        DormReviewEntity review = optional.get();

        if(!review.getUserId().equals(userId)) {
            throw new ForbiddenException("Нельзя редактировать чужой отзыв");
        }

        validateDormReview(dormReviewEntity);

        dormReviewEntity.setText(dormReviewEntity.getText().trim());
        dormReviewDao.update(dormReviewEntity);

    }

    @Override
    public void delete(Long userId, Long id) {
        Optional<DormReviewEntity> optional = dormReviewDao.findById(id);
        if(optional.isEmpty()) {
            throw new NotFoundException("Не удалось найти отзыв");
        }

        DormReviewEntity review = optional.get();

        if(!review.getUserId().equals(userId)) {
            throw new ForbiddenException("Нельзя удалить чужой отзыв");
        }

        dormReviewDao.deleteById(id);
    }

    @Override
    public void deleteByAdmin(Long id) {
        if (dormReviewDao.findById(id).isEmpty()) {
            throw new NotFoundException("Не удалось найти отзыв");
        }
        dormReviewDao.deleteById(id);
    }

    private void validateDormReview(DormReviewEntity dormReviewEntity) {
        Map<String, String> errors = new HashMap<>();
        String text = dormReviewEntity.getText();
        if (text == null || text.isBlank()) {
            errors.put("text", "Текст отзыва не должен быть пустым");
        } else if (text.trim().length() > 1000) {
            errors.put("text", "Текст отзыва должен быть не больше 1000 символов");
        }

        Integer rating = dormReviewEntity.getRating();
        if (rating == null || rating < 1 || rating > 5) {
            errors.put("rating", "Выберите оценку от 1 до 5");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
