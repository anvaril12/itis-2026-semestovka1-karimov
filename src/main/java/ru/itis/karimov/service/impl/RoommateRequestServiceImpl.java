package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.RoommateRequestDao;
import ru.itis.karimov.dao.UserDao;
import ru.itis.karimov.dto.RequestView;
import ru.itis.karimov.entity.RoommateRequestEntity;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.entity.enums.RoommateRequestStatus;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.RoommateRequestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Override
    public void accept(Long requestId, Long userId) {
        changeStatus(requestId, userId, RoommateRequestStatus.ACCEPTED);
    }

    @Override
    public void reject(Long requestId, Long userId) {
        changeStatus(requestId, userId, RoommateRequestStatus.REJECTED);
    }

    @Override
    public List<RequestView> getIncoming(Long userId) {
        List<RequestView> result = new ArrayList<>();
        for(RoommateRequestEntity r : roommateRequestDao.findByReceiverId(userId)) {
            Optional<UserEntity> sender = userDao.findById(r.getSenderId());
            if(sender.isEmpty()) {
                continue;
            }
            result.add(new RequestView(r, sender.get()));
        }
        return result;
    }

    @Override
    public List<RequestView> getOutgoing(Long userId) {
        List<RequestView> result = new ArrayList<>();
        for(RoommateRequestEntity r : roommateRequestDao.findBySenderId(userId)) {
            Optional<UserEntity> receiver = userDao.findById(r.getReceiverId());
            if(receiver.isEmpty()) {
                continue;
            }
            result.add(new RequestView(r, receiver.get()));
        }
        return result;
    }

    private void changeStatus(Long requestId, Long userId, RoommateRequestStatus newStatus) {
        Optional<RoommateRequestEntity> roommateRequestEntity = roommateRequestDao.findById(requestId);
        if(roommateRequestEntity.isEmpty()) {
            throw new ValidationException(Map.of("request", "Не удалось найти заявку"));
        }
        RoommateRequestEntity roommateRequest = roommateRequestEntity.get();

        if(!roommateRequest.getReceiverId().equals(userId)) {
            throw new ValidationException(Map.of("request", "Это не ваша заявка"));
        }

        if(!roommateRequest.getStatus().equals(RoommateRequestStatus.PENDING)) {
            throw new ValidationException(Map.of("request", "Заявка уже обработана"));
        }
        roommateRequestDao.updateStatus(requestId, newStatus);
    }
}
