package ru.itis.karimov.dto;

import ru.itis.karimov.entity.RoomEntity;
import ru.itis.karimov.entity.RoomMemberEntity;

public record RoomApplicationView(
        RoomMemberEntity roomMemberEntity,
        RoomEntity roomEntity
) {
}
