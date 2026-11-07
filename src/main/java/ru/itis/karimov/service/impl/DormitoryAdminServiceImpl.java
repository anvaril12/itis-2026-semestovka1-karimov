package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.*;
import ru.itis.karimov.entity.DormitoryEntity;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.DormitoryAdminService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DormitoryAdminServiceImpl implements DormitoryAdminService {

    private final DormitoryDao dormitoryDao;
    private final RoomDao roomDao;
    private final UserDao userDao;
    private final UniversityDao universityDao;
    private final DormReviewDao dormReviewDao;

    public DormitoryAdminServiceImpl(DormitoryDao dormitoryDao, RoomDao roomDao,
                                     UserDao userDao, UniversityDao universityDao,
                                     DormReviewDao dormReviewDao) {
        this.dormitoryDao = dormitoryDao;
        this.roomDao = roomDao;
        this.userDao = userDao;
        this.universityDao = universityDao;
        this.dormReviewDao = dormReviewDao;
    }

    @Override
    public List<DormitoryEntity> findAll() {
        return dormitoryDao.findAll();
    }

    @Override
    public DormitoryEntity findById(Integer id) {
        Optional<DormitoryEntity> optional = dormitoryDao.findById(id);
        if(optional.isEmpty()) {
            throw new NotFoundException("Не удалось найти общежитие");
        }
        return optional.get();
    }

    @Override
    public DormitoryEntity save(DormitoryEntity dormitoryEntity) {
        validateDormitories(dormitoryEntity);

        if(universityDao.findById(dormitoryEntity.getUniversityId()).isEmpty()) {
            throw new ValidationException(Map.of("university", "Выбранный ВУЗ не найден"));
        }

        dormitoryEntity.setName(dormitoryEntity.getName().trim());
        dormitoryEntity.setAddress(dormitoryEntity.getAddress().trim());

        return dormitoryDao.save(dormitoryEntity);
    }

    @Override
    public void update(DormitoryEntity dormitoryEntity) {
        findById(dormitoryEntity.getId());
        validateDormitories(dormitoryEntity);

        if(universityDao.findById(dormitoryEntity.getUniversityId()).isEmpty()) {
            throw new ValidationException(Map.of("university", "Выбранный ВУЗ не найден"));
        }

        dormitoryEntity.setName(dormitoryEntity.getName().trim());
        dormitoryEntity.setAddress(dormitoryEntity.getAddress().trim());

        dormitoryDao.update(dormitoryEntity);
    }

    @Override
    public void delete(Integer id) {
        findById(id);

        if(!userDao.findByDormitoryId(id).isEmpty() || !roomDao.findByDormitoryId(id).isEmpty()) {
            throw new ValidationException(Map.of("dormitory", "Не удалось удалить общежитие: с ним связаны данные"));
        }

        if(!dormReviewDao.findByDormitoryId(id).isEmpty()) {
            throw new ValidationException(Map.of("dormitory", "Нельзя удалить общежитие с отзывами"));
        }

        dormitoryDao.delete(id);
    }

    private void validateDormitories(DormitoryEntity dormitoryEntity) {
        if(dormitoryEntity.getAddress() == null || dormitoryEntity.getName() == null) {
            throw new ValidationException(Map.of("dormitory", "Введите адрес и название"));
        }

        if(dormitoryEntity.getAddress().isBlank() || dormitoryEntity.getName().isBlank()) {
            throw new ValidationException(Map.of("dormitory", "Адрес и имя общежития не должны быть пустыми"));
        }

        if(dormitoryEntity.getName().trim().length() > 100) {
            throw new ValidationException(Map.of("dormitory", "Длина названия общежития не должна превышать 100 символов"));
        }

        if(dormitoryEntity.getAddress().trim().length() > 100) {
            throw new ValidationException(Map.of("dormitory", "Длина адреса общежития не должна превышать 100 символов"));
        }
    }
}
