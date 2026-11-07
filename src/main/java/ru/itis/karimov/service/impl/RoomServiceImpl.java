package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.RoomDao;
import ru.itis.karimov.dao.RoomMemberDao;
import ru.itis.karimov.dao.UserDao;
import ru.itis.karimov.dto.RoomForm;
import ru.itis.karimov.dto.RoomView;
import ru.itis.karimov.entity.RoomEntity;
import ru.itis.karimov.entity.RoomMemberEntity;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.entity.enums.RoomMemberStatus;
import ru.itis.karimov.exception.ForbiddenException;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.RoomService;
import ru.itis.karimov.validation.RoomValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RoomServiceImpl implements RoomService {

    private final RoomValidator roomValidator = new RoomValidator();
    private final RoomDao roomDao;
    private final UserDao userDao;
    private final RoomMemberDao roomMemberDao;

    public RoomServiceImpl(RoomDao roomDao, UserDao userDao, RoomMemberDao roomMemberDao) {
        this.roomDao = roomDao;
        this.userDao = userDao;
        this.roomMemberDao = roomMemberDao;
    }

    @Override
    public Long create(RoomForm roomForm, Long ownerId) {
        Map<String, String> errors = roomValidator.validate(roomForm);

        if(!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        Optional<UserEntity> userEntity = userDao.findById(ownerId);
        if(userEntity.isEmpty()) {
            throw new ValidationException(Map.of("room", "Пользователь не найден"));
        }

        UserEntity user = userEntity.get();
        if(user.getDormitoryId() == null) {
            throw new ValidationException(Map.of("room", "Сначала выберите общежитие"));
        }

        if(!roomDao.findByOwnerId(ownerId).isEmpty()) {
            throw new ValidationException(Map.of("room", "У вас уже есть комната"));
        }

        RoomEntity roomEntity = RoomEntity.builder()
                .ownerId(ownerId)
                .dormitoryId(user.getDormitoryId())
                .capacity(Integer.parseInt(roomForm.capacity()))
                .description(roomForm.description())
                .roomNumber(roomForm.roomNumber())
                .build();
        return roomDao.save(roomEntity).getId();
    }

    @Override
    public RoomView getById(Long roomId) {
        Optional<RoomEntity> roomView = roomDao.findById(roomId);
        if(roomView.isEmpty()) {
            throw new NotFoundException("Комната не найдена");
        }
        RoomEntity room = roomView.get();

        Optional<UserEntity> userEntity = userDao.findById(room.getOwnerId());
        if(userEntity.isEmpty()) {
            throw new NotFoundException("Владелец комнаты не найден");
        }
        UserEntity user = userEntity.get();

        return new RoomView(room, user);
    }

    @Override
    public RoomEntity getOwnerRoom(Long roomId, Long userId) {
        Optional<RoomEntity> roomEntity = roomDao.findById(roomId);
        if(roomEntity.isEmpty()) {
            throw new NotFoundException("Не удалось найти комнату");
        }
        RoomEntity room = roomEntity.get();
        if(!room.getOwnerId().equals(userId)) {
            throw new ForbiddenException("Комната не ваша");
        }
        return room;
    }

    @Override
    public void update(Long roomId, RoomForm roomForm, Long userId) {
        Map<String, String> errors = roomValidator.validate(roomForm);

        if(!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        RoomEntity room = getOwnerRoom(roomId, userId);
        int capacity = Integer.parseInt(roomForm.capacity());
        int resident = 1;

        for (RoomMemberEntity member : roomMemberDao.findByRoomId(roomId)) {
            if (member.getStatus() == RoomMemberStatus.ACCEPTED) {
                resident++;
            }
        }

        if(capacity < resident) {
            throw new ValidationException(Map.of("capacity", "В комнате уже " + resident +
                    " жильца. Вместимость не может быть меньше этого числа"));
        }
        room.setCapacity(capacity);

        room.setDescription(roomForm.description());
        room.setRoomNumber(roomForm.roomNumber());
        roomDao.update(room);
    }

    @Override
    public void delete(Long roomId, Long userId) {
        getOwnerRoom(roomId, userId);
        roomDao.deleteById(roomId);
    }

    @Override
    public List<RoomView> getRoomsInMyDormitory(Long userId) {

        Optional<UserEntity> optionalUserEntity = userDao.findById(userId);
        if(optionalUserEntity.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }

        UserEntity user = optionalUserEntity.get();
        if(user.getDormitoryId() == null) {
            return List.of();
        }

        List<RoomEntity> rooms = roomDao.findByDormitoryId(user.getDormitoryId());

        List<RoomView> roomViews = new ArrayList<>();
        for(RoomEntity room : rooms) {
            Optional<UserEntity> optionalUserEntity1 = userDao.findById(room.getOwnerId());
            if(optionalUserEntity1.isEmpty()) {
                throw new NotFoundException("Владелец комнаты не найден");
            }
            roomViews.add(new RoomView(room, optionalUserEntity1.get()));
        }
        return roomViews;
    }
}
