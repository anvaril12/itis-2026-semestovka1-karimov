package ru.itis.karimov.service;

import ru.itis.karimov.dto.RoomForm;
import ru.itis.karimov.dto.RoomView;

public interface RoomService {
    Long create(RoomForm roomForm, Long ownerId);
    RoomView getById(Long roomId);
}
