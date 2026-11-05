package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.DormReviewDao;
import ru.itis.karimov.dao.DormitoryDao;
import ru.itis.karimov.entity.DormReviewEntity;
import ru.itis.karimov.exception.ForbiddenException;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.DormReviewService;

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

    private void validateDormReview(DormReviewEntity dormReviewEntity) {
        if(dormReviewEntity.getText() == null || dormReviewEntity.getRating() == null) {
            throw new ValidationException(Map.of("review", "Введите текст и рейтинг"));
        }

        if(dormReviewEntity.getText().isBlank()) {
            throw new ValidationException(Map.of("text", "Текст отзыва не должен быть пустым"));
        }

        if(dormReviewEntity.getText().trim().length() > 1000) {
            throw new ValidationException(Map.of("text", "Текст отзыва должен быть не больше 1000 символов"));
        }

        if(dormReviewEntity.getRating() > 5 || dormReviewEntity.getRating() < 1) {
            throw new ValidationException(Map.of("rating", "Рейтинг должен быть от 1 до 5"));
        }
    }
}
