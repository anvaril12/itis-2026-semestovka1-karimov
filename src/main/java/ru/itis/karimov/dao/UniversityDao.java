package ru.itis.karimov.dao;

import ru.itis.karimov.entity.UniversityEntity;
import java.util.List;
import java.util.Optional;

public interface UniversityDao {
    List<UniversityEntity> findAll();
    Optional<UniversityEntity> findById(Integer id);
}
