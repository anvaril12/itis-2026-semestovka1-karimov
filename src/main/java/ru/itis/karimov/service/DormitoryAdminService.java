package ru.itis.karimov.service;

import ru.itis.karimov.entity.DormitoryEntity;

import java.util.List;

public interface DormitoryAdminService {
    List<DormitoryEntity> findAll();
    DormitoryEntity findById(Integer id);
    DormitoryEntity save(DormitoryEntity dormitoryEntity);
    void update(DormitoryEntity dormitoryEntity);
    void delete(Integer id);
}
