package ru.itis.karimov.dto;

import ru.itis.karimov.entity.RoomMemberEntity;
import ru.itis.karimov.entity.UserEntity;

public record RoomMemberView(
        RoomMemberEntity roomMemberEntity,
        UserEntity user
) {
}
