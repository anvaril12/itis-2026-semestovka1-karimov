package ru.itis.karimov.service;

import ru.itis.karimov.dto.RoomForm;
import ru.itis.karimov.dto.RoomView;
import ru.itis.karimov.entity.RoomEntity;

import java.util.List;

public interface RoomService {
    Long create(RoomForm roomForm, Long ownerId);
    RoomView getById(Long roomId);
    RoomEntity getOwnerRoom(Long roomId, Long userId);
    void update(Long roomId, RoomForm roomForm, Long userId);
    void delete(Long roomId, Long userId);
    List<RoomView> getRoomsInMyDormitory(Long userId);
}
