package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.RoomDao;
import ru.itis.karimov.dao.RoomMemberDao;
import ru.itis.karimov.entity.RoomEntity;
import ru.itis.karimov.entity.RoomMemberEntity;
import ru.itis.karimov.entity.enums.RoomMemberStatus;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.RoomMemberService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RoomMemberServiceImpl implements RoomMemberService {
    private final RoomDao roomDao;
    private final RoomMemberDao roomMemberDao;

    public RoomMemberServiceImpl(RoomDao roomDao, RoomMemberDao roomMemberDao) {
        this.roomDao = roomDao;
        this.roomMemberDao = roomMemberDao;
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

        List<RoomMemberEntity> roomMemberEntityList = roomMemberDao.findByRoomId(roomId);
        int acceptedCount = 0;
        for(RoomMemberEntity roomMember : roomMemberEntityList) {
            if(roomMember.getStatus() == RoomMemberStatus.ACCEPTED) {
                acceptedCount++;
            }
        }
        if(acceptedCount >= roomEntity.getCapacity() - 1) {
            throw new ValidationException(Map.of("join", "В комнате нет свободных мест"));
        }
        roomMemberDao.save(new RoomMemberEntity(roomId, userId, RoomMemberStatus.PENDING));
    }
}
