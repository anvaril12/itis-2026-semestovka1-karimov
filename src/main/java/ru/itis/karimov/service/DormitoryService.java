package ru.itis.karimov.service;

import ru.itis.karimov.entity.DormitoryEntity;
import ru.itis.karimov.entity.UniversityEntity;

import java.util.List;
import java.util.Optional;

public interface DormitoryService {
    List<UniversityEntity> getAllUniversities();
    List<DormitoryEntity> getDormitoriesByUniversityId(Integer universityId);
    Optional<DormitoryEntity> getDormitoryById(Integer id);
}
