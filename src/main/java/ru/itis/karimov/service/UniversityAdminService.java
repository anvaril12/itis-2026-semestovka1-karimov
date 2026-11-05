package ru.itis.karimov.service;

import ru.itis.karimov.entity.UniversityEntity;

import java.util.List;

public interface UniversityAdminService {
    List<UniversityEntity> findAll();
    UniversityEntity save(UniversityEntity university);
}
