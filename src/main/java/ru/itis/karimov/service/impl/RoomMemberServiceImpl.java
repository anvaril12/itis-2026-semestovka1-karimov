package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.RoomDao;
import ru.itis.karimov.dao.RoomMemberDao;
import ru.itis.karimov.dao.UserAnswerDao;
import ru.itis.karimov.dao.UserDao;
import ru.itis.karimov.dto.RoomMemberView;
import ru.itis.karimov.entity.RoomEntity;
import ru.itis.karimov.entity.RoomMemberEntity;
import ru.itis.karimov.entity.UserAnswerEntity;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.entity.enums.RoomMemberStatus;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.CompatibilityService;
import ru.itis.karimov.service.RoomMemberService;

import java.util.*;

public class RoomMemberServiceImpl implements RoomMemberService {

    private final RoomDao roomDao;
    private final RoomMemberDao roomMemberDao;
    private final UserDao userDao;
    private final UserAnswerDao userAnswerDao;
    private final CompatibilityService compatibilityService;

    public RoomMemberServiceImpl(RoomDao roomDao, RoomMemberDao roomMemberDao,
                                 UserDao userDao, UserAnswerDao userAnswerDao,
                                 CompatibilityService compatibilityService) {
        this.roomDao = roomDao;
        this.roomMemberDao = roomMemberDao;
        this.userDao = userDao;
        this.userAnswerDao = userAnswerDao;
        this.compatibilityService = compatibilityService;
    }

    @Override
    public void join(Long roomId, Long userId) {
        Optional<RoomEntity> roomMemberEntity = roomDao.findById(roomId);
        if(roomMemberEntity.isEmpty()) {
            throw new ValidationException(Map.of("join", "Комната не найдена"));
        }
        RoomEntity roomEntity = roomMemberEntity.get();

        if(roomEntity.getOwnerId().equals(userId)) {
            throw new ValidationException(Map.of("join", "Это ваша комната"));
        }

        Optional<RoomMemberEntity> userEntity = roomMemberDao.find(roomId, userId);
        if(userEntity.isPresent()) {
            throw new ValidationException(Map.of("join", "Вы уже подали заявку"));
        }

        if(countAccepted(roomId) >= roomEntity.getCapacity() - 1) {
            throw new ValidationException(Map.of("join", "В комнате нет свободных мест"));
        }
        roomMemberDao.save(new RoomMemberEntity(roomId, userId, RoomMemberStatus.PENDING));
    }

    @Override
    public List<RoomMemberView> getMembers(Long roomId) {
        Optional<RoomEntity> optionalRoomEntity = roomDao.findById(roomId);
        if(optionalRoomEntity.isEmpty()) {
            throw new NotFoundException("Комната не найдена");
        }
        RoomEntity roomEntity = optionalRoomEntity.get();

        Optional<UserEntity> optionalUserEntity = userDao.findById(roomEntity.getOwnerId());
        if(optionalUserEntity.isEmpty()) {
            throw new NotFoundException("Не удалось найти владельца комнаты");
        }

        List<RoomMemberEntity> roomMemberEntityList = roomMemberDao.findByRoomId(roomId);
        List<UserEntity> userEntities = new ArrayList<>();
        List<UserEntity> residents = new ArrayList<>();
        residents.add(optionalUserEntity.get());
        for(RoomMemberEntity r : roomMemberEntityList) {
            Optional<UserEntity> optionalUser = userDao.findById(r.getUserId());
            if(optionalUser.isEmpty()) {
                throw new NotFoundException("Пользователь не найден");
            }
            userEntities.add(optionalUser.get());
            if(r.getStatus() == RoomMemberStatus.ACCEPTED) {
                residents.add(optionalUser.get());
            }
        }

        List<RoomMemberView> result = new ArrayList<>();
        for(int i = 0;i < roomMemberEntityList.size(); i++) {
            RoomMemberEntity roomMember = roomMemberEntityList.get(i);
            UserEntity user = userEntities.get(i);
            Map<String, Integer> compatibility = Map.of();
            if(roomMember.getStatus() == RoomMemberStatus.PENDING) {
                compatibility = compatibilityWithResidents(user.getId(), residents);
            }
            result.add(new RoomMemberView(roomMember, user, compatibility));
        }
        return result;
    }

    @Override
    public void changeStatus(Long roomId, Long memberId, Long ownerId, RoomMemberStatus status) {
        Optional<RoomEntity> optionalRoomEntity = roomDao.findById(roomId);
        if(optionalRoomEntity.isEmpty()) {
            throw new ValidationException(Map.of("member", "Комната не найдена"));
        }
        RoomEntity roomEntity = optionalRoomEntity.get();

        if(!roomEntity.getOwnerId().equals(ownerId)) {
            throw new ValidationException(Map.of("member", "Это не ваша комната"));
        }

        Optional<RoomMemberEntity> optionalRoomMemberEntity = roomMemberDao.find(roomId, memberId);
        if(optionalRoomMemberEntity.isEmpty()) {
            throw new ValidationException(Map.of("member", "Заявка не найдена"));
        }
        RoomMemberEntity roomMemberEntity = optionalRoomMemberEntity.get();

        if(!roomMemberEntity.getStatus().equals(RoomMemberStatus.PENDING)) {
            throw new ValidationException(Map.of("member", "Заявка уже обработана"));
        }

        if(status == RoomMemberStatus.ACCEPTED && countAccepted(roomId) >= roomEntity.getCapacity() - 1) {
            throw new ValidationException(Map.of("member", "В комнате нет мест"));
        }
        roomMemberDao.updateStatus(roomId, memberId, status);
    }

    private int countAccepted(Long roomId) {
        int countAccepted = 0;
        for(RoomMemberEntity roomMember : roomMemberDao.findByRoomId(roomId)) {
            if(roomMember.getStatus().equals(RoomMemberStatus.ACCEPTED)) {
                countAccepted++;
            }
        }
        return countAccepted;
    }

    private Map<String, Integer> compatibilityWithResidents(Long candidateId, List<UserEntity> residents) {
        List<UserAnswerEntity> candidateAnswers = userAnswerDao.findByUserId(candidateId);
        Map<String, Integer> result = new LinkedHashMap<>();
        for (UserEntity resident : residents) {
            Integer percent = compatibilityService.calculate(candidateAnswers, userAnswerDao.findByUserId(resident.getId()));
            result.put(resident.getFirstName() + " " + resident.getLastName(), percent);
        }
        return result;
    }
}
