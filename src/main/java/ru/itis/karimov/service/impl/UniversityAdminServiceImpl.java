package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.UniversityDao;
import ru.itis.karimov.entity.UniversityEntity;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.UniversityAdminService;

import java.util.List;
import java.util.Map;

public class UniversityAdminServiceImpl implements UniversityAdminService {

    public UniversityAdminServiceImpl(UniversityDao universityDao) {
        this.universityDao = universityDao;
    }

    private final UniversityDao universityDao;

    @Override
    public List<UniversityEntity> findAll() {
        return universityDao.findAll();
    }

    @Override
    public UniversityEntity save(UniversityEntity university) {

        if(university.getName() == null || university.getCity() == null) {
            throw new ValidationException(Map.of("university", "Название и город не должны быть пустыми"));
        }

        if(university.getCity().isBlank() || university.getName().isBlank()) {
            throw new ValidationException(Map.of("university", "Название и город не должны состоять из пустых символов"));
        }

        if(university.getName().trim().length() > 100) {
            throw new ValidationException(Map.of("university", "Длина названия университета не должна быть больше 100 символов"));
        }

        if(university.getCity().trim().length() > 30) {
            throw new ValidationException(Map.of("university", "Длина названия города не должна быть больше 30 символов"));
        }

        String city = university.getCity().trim();
        String name = university.getName().trim();

        university.setCity(city);
        university.setName(name);

        return universityDao.save(university);
    }
}
