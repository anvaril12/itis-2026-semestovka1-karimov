package ru.itis.karimov.service;

import ru.itis.karimov.entity.DormitoryEntity;
import ru.itis.karimov.entity.UniversityEntity;

import java.util.List;

public interface DormitoryService {
    List<UniversityEntity> getAllUniversities();
    List<DormitoryEntity> getDormitoriesByUniversityId(Integer universityId);
}
