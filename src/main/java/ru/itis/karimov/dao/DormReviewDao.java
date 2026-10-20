package ru.itis.karimov.dao;

import ru.itis.karimov.entity.DormReviewEntity;
import java.util.List;
import java.util.Optional;

public interface DormReviewDao extends CrudDao<DormReviewEntity, Long> {
    List<DormReviewEntity> findByDormitoryId(Integer dormitoryId);
    Optional<DormReviewEntity> findByUserIdAndDormitoryId(Long userId, Integer dormitoryId);
}