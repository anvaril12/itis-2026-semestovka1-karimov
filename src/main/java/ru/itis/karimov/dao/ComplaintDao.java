package ru.itis.karimov.dao;

import ru.itis.karimov.entity.ComplaintEntity;
import ru.itis.karimov.entity.enums.ComplaintStatus;

import java.util.List;
import java.util.Optional;

public interface ComplaintDao {
    ComplaintEntity save(ComplaintEntity complaint);
    Optional<ComplaintEntity> findById(Long id);
    List<ComplaintEntity> findAll();
    List<ComplaintEntity> findByStatus(ComplaintStatus status);
    void updateStatus(Long id, ComplaintStatus status);
}
