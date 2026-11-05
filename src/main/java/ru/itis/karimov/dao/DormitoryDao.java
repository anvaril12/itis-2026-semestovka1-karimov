package ru.itis.karimov.dao;

import ru.itis.karimov.entity.DormitoryEntity;
import java.util.List;
import java.util.Optional;

public interface DormitoryDao {
    Optional<DormitoryEntity> findById(Integer id);
    List<DormitoryEntity> findByUniversityId(Integer universityId);
    List<DormitoryEntity> findAll();
    DormitoryEntity save(DormitoryEntity dormitoryEntity);
    void update(DormitoryEntity dormitoryEntity);
    void delete(Integer id);
}
