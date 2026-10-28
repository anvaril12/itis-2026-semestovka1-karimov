package ru.itis.karimov.dto;

import ru.itis.karimov.entity.RoomMemberEntity;
import ru.itis.karimov.entity.UserEntity;

import java.util.Map;

public record RoomMemberView(
        RoomMemberEntity roomMemberEntity,
        UserEntity user,
        Map<String, Integer> compatibility
) {
}
