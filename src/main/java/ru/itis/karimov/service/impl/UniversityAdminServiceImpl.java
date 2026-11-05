package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.UniversityDao;
import ru.itis.karimov.entity.UniversityEntity;
import ru.itis.karimov.service.UniversityAdminService;

import java.util.List;

public class UniversityAdminServiceImpl implements UniversityAdminService {

    public UniversityAdminServiceImpl(UniversityDao universityDao) {
        this.universityDao = universityDao;
    }

    private final UniversityDao universityDao;

    @Override
    public List<UniversityEntity> findAll() {
        return universityDao.findAll();
    }
}
