package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.DormitoryDao;
import ru.itis.karimov.dao.UniversityDao;
import ru.itis.karimov.entity.DormitoryEntity;
import ru.itis.karimov.entity.UniversityEntity;
import ru.itis.karimov.service.DormitoryService;

import java.util.List;

public class DormitoryServiceImpl implements DormitoryService {
    private final DormitoryDao dormitoryDao;
    private final UniversityDao universityDao;

    public DormitoryServiceImpl(DormitoryDao dormitoryDao, UniversityDao universityDao) {
        this.dormitoryDao = dormitoryDao;
        this.universityDao = universityDao;
    }

    @Override
    public List<UniversityEntity> getAllUniversities() {
        return universityDao.findAll();
    }

    @Override
    public List<DormitoryEntity> getDormitoriesByUniversityId(Integer universityId) {
        return dormitoryDao.findByUniversityId(universityId);
    }
}
