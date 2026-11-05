package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.ComplaintDao;
import ru.itis.karimov.dao.UserDao;
import ru.itis.karimov.entity.ComplaintEntity;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.ComplaintService;

import java.util.Map;

public class ComplaintServiceImpl implements ComplaintService {
    private final ComplaintDao complaintDao;
    private final UserDao userDao;

    public ComplaintServiceImpl(ComplaintDao complaintDao, UserDao userDao) {
        this.complaintDao = complaintDao;
        this.userDao = userDao;
    }

    @Override
    public ComplaintEntity save(ComplaintEntity complaint) {
        if (complaint.getReceiverId() == null || userDao.findById(complaint.getReceiverId()).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (complaint.getSenderId().equals(complaint.getReceiverId())) {
            throw new ValidationException(Map.of("complaint", "Нельзя пожаловаться на себя"));
        }
        if (complaint.getText() == null || complaint.getText().isBlank()) {
            throw new ValidationException(Map.of("text", "Укажите причину жалобы"));
        }
        if (complaint.getText().trim().length() > 1000) {
            throw new ValidationException(Map.of("text", "Причина жалобы должна быть не больше 1000 символов"));
        }

        complaint.setText(complaint.getText().trim());
        return complaintDao.save(complaint);
    }
}
