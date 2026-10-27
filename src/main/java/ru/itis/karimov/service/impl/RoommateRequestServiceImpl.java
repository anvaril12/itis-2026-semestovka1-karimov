package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.RoommateRequestDao;
import ru.itis.karimov.dao.UserDao;
import ru.itis.karimov.entity.RoommateRequestEntity;
import ru.itis.karimov.entity.enums.RoommateRequestStatus;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.RoommateRequestService;

import java.util.Map;

public class RoommateRequestServiceImpl implements RoommateRequestService {

    private final UserDao userDao;
    private final RoommateRequestDao roommateRequestDao;

    public RoommateRequestServiceImpl(UserDao userDao, RoommateRequestDao roommateRequestDao) {
        this.userDao = userDao;
        this.roommateRequestDao = roommateRequestDao;
    }

    @Override
    public void send(Long senderId, Long receiverId) {
        if(senderId.equals(receiverId)) {
            throw new ValidationException(Map.of("request", "Нельзя отправить заявку самому себе"));
        }

        if(userDao.findById(receiverId).isEmpty()) {
            throw new ValidationException(Map.of("request", "Не удалось найти получателя"));
        }

        if(roommateRequestDao.findBetween(senderId, receiverId).isPresent()) {
            throw new ValidationException(Map.of("request", "Заявка уже существует"));
        }

        RoommateRequestEntity roommateRequestEntity = RoommateRequestEntity.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .status(RoommateRequestStatus.PENDING)
                .build();
        roommateRequestDao.save(roommateRequestEntity);
    }
}
