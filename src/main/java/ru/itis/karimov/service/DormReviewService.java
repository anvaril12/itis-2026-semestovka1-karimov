package ru.itis.karimov.service;

import ru.itis.karimov.entity.DormReviewEntity;

import java.util.List;

public interface DormReviewService {
    List<DormReviewEntity> findAll();
    List<DormReviewEntity> findByDormitoryId(Integer dormitoryId);
    DormReviewEntity save(DormReviewEntity dormReviewEntity);
    void update(Long userId, DormReviewEntity dormReviewEntity);
    void delete(Long userId, Long id);
}
