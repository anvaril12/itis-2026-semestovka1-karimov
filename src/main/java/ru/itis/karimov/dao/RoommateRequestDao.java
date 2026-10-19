package ru.itis.karimov.dao;

import ru.itis.karimov.entity.RoommateRequestEntity;
import ru.itis.karimov.entity.enums.RoommateRequestStatus;

import java.util.List;
import java.util.Optional;

public interface RoommateRequestDao {
    RoommateRequestEntity save(RoommateRequestEntity request);
    List<RoommateRequestEntity> findBySenderId(Long senderId);
    List<RoommateRequestEntity> findByReceiverId(Long receiverId);
    Optional<RoommateRequestEntity> findById(Long id);
    void updateStatus(Long id, RoommateRequestStatus status);
    void deleteById(Long id);
    Optional<RoommateRequestEntity> findBetween(Long userId1, Long userId2);
}
