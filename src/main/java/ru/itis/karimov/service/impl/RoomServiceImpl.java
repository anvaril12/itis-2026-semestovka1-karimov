package ru.itis.karimov.service.impl;

import ru.itis.karimov.dao.RoomDao;
import ru.itis.karimov.dao.UserDao;
import ru.itis.karimov.dto.RoomForm;
import ru.itis.karimov.dto.RoomView;
import ru.itis.karimov.entity.RoomEntity;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.exception.ForbiddenException;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.RoomService;
import ru.itis.karimov.validation.RoomValidator;
import java.util.Map;
import java.util.Optional;

public class RoomServiceImpl implements RoomService {

    private final RoomValidator roomValidator = new RoomValidator();
    private final RoomDao roomDao;
    private final UserDao userDao;

    public RoomServiceImpl(RoomDao roomDao, UserDao userDao) {
        this.roomDao = roomDao;
        this.userDao = userDao;
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
        RoomEntity roomEntity = getOwnerRoom(roomId, userId);
        roomEntity.setCapacity(Integer.parseInt(roomForm.capacity()));
        roomEntity.setDescription(roomForm.description());
        roomEntity.setRoomNumber(roomForm.roomNumber());
        roomDao.update(roomEntity);
    }

    @Override
    public void delete(Long roomId, Long userId) {
        getOwnerRoom(roomId, userId);
        roomDao.deleteById(roomId);
    }
}
