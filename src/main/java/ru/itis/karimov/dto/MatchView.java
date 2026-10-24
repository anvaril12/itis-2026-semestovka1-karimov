package ru.itis.karimov.dto;

import ru.itis.karimov.entity.UserEntity;

public record MatchView(
        UserEntity user,
        int percent
) {
}
