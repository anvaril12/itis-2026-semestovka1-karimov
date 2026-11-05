package ru.itis.karimov.service;

import ru.itis.karimov.entity.ComplaintEntity;
import ru.itis.karimov.entity.enums.ComplaintStatus;

import java.util.List;

public interface ComplaintAdminService {
    List<ComplaintEntity> findAll();
    List<ComplaintEntity> findByStatus(ComplaintStatus status);
    void updateStatus(Long id, ComplaintStatus status);
}
