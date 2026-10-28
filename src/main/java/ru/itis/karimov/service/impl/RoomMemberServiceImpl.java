package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.RoomDao;
import ru.itis.karimov.dao.RoomMemberDao;
import ru.itis.karimov.dao.UserDao;
import ru.itis.karimov.dto.RoomMemberView;
import ru.itis.karimov.entity.RoomEntity;
import ru.itis.karimov.entity.RoomMemberEntity;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.entity.enums.RoomMemberStatus;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.RoomMemberService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RoomMemberServiceImpl implements RoomMemberService {

    private final RoomDao roomDao;
    private final RoomMemberDao roomMemberDao;
    private final UserDao userDao;

    public RoomMemberServiceImpl(RoomDao roomDao, RoomMemberDao roomMemberDao, UserDao userDao) {
        this.roomDao = roomDao;
        this.roomMemberDao = roomMemberDao;
        this.userDao = userDao;
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
        List<RoomMemberEntity> list = roomMemberDao.findByRoomId(roomId);
        List<RoomMemberView> result = new ArrayList<>();
        for(RoomMemberEntity roomMember : list) {
            Optional<UserEntity> optionalUser = userDao.findById(roomMember.getUserId());
            if (optionalUser.isEmpty()) {
                throw new NotFoundException("Пользователь не найден");
            }
            result.add(new RoomMemberView(roomMember, optionalUser.get()));
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
}
