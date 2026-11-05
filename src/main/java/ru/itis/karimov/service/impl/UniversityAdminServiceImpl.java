package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.DormitoryDao;
import ru.itis.karimov.dao.UniversityDao;
import ru.itis.karimov.entity.DormitoryEntity;
import ru.itis.karimov.entity.UniversityEntity;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.UniversityAdminService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UniversityAdminServiceImpl implements UniversityAdminService {

    public UniversityAdminServiceImpl(UniversityDao universityDao, DormitoryDao dormitoryDao) {
        this.universityDao = universityDao;
        this.dormitoryDao = dormitoryDao;
    }

    private final UniversityDao universityDao;
    private final DormitoryDao dormitoryDao;

    @Override
    public List<UniversityEntity> findAll() {
        return universityDao.findAll();
    }

    @Override
    public UniversityEntity save(UniversityEntity university) {
        validateUniversity(university);
        university.setCity(university.getCity().trim());
        university.setName(university.getName().trim());

        return universityDao.save(university);
    }

    @Override
    public void update(UniversityEntity university) {
        validateUniversity(university);
        university.setName(university.getName().trim());
        university.setCity(university.getCity().trim());

        universityDao.update(university);
    }

    @Override
    public UniversityEntity findById(Integer id) {
        Optional<UniversityEntity> university = universityDao.findById(id);
        if(university.isEmpty()) {
            throw new NotFoundException("Не удалось найти университет по id: " + id);
        }
        return university.get();
    }

    @Override
    public void delete(Integer id) {
        Optional<UniversityEntity> optional = universityDao.findById(id);
        if(optional.isEmpty()) {
            throw new NotFoundException("Не удалось найти ВУЗ");
        }

        List<DormitoryEntity> dormitories = dormitoryDao.findByUniversityId(id);

        if (!dormitories.isEmpty()) {
            throw new ValidationException(Map.of("university", "Нельзя удалить вуз: у него есть общежития"));
        }
        universityDao.delete(optional.get());

    }

    private void validateUniversity(UniversityEntity university) {

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
    }
}
