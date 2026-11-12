package ru.itis.karimov.service.impl.admin;

import ru.itis.karimov.dao.ComplaintDao;
import ru.itis.karimov.entity.ComplaintEntity;
import ru.itis.karimov.entity.enums.ComplaintStatus;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.ComplaintAdminService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ComplaintAdminServiceImpl implements ComplaintAdminService {

    private final ComplaintDao complaintDao;

    public ComplaintAdminServiceImpl(ComplaintDao complaintDao) {
        this.complaintDao = complaintDao;
    }

    @Override
    public List<ComplaintEntity> findAll() {
        return complaintDao.findAll();
    }

    @Override
    public List<ComplaintEntity> findByStatus(ComplaintStatus status) {
        return complaintDao.findByStatus(status);
    }

    @Override
    public void updateStatus(Long id, ComplaintStatus status) {
        if(id == null) {
            throw new ValidationException(Map.of("id", "Не указан id жалобы"));
        }

        if(status == null) {
            throw new ValidationException(Map.of("status", "Не указан статус жалобы"));
        }

        Optional<ComplaintEntity> optional = complaintDao.findById(id);
        if(optional.isEmpty()) {
            throw new NotFoundException("Не удалось найти жалобу");
        }

        ComplaintEntity entity = optional.get();

        if(entity.getStatus().equals(status)) {
            throw new ValidationException(Map.of("status","Нельзя поменять на такой же статус"));
        }

        complaintDao.updateStatus(id, status);
    }
}
