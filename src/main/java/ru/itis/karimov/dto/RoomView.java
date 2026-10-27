package ru.itis.karimov.dto;

import ru.itis.karimov.entity.RoomEntity;
import ru.itis.karimov.entity.UserEntity;

public record RoomView(
        RoomEntity roomEntity,
        UserEntity owner
) {
}
