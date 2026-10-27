package ru.itis.karimov.dto;

import ru.itis.karimov.entity.RoommateRequestEntity;
import ru.itis.karimov.entity.UserEntity;

public record RequestView (
        RoommateRequestEntity roommateRequestEntity,
        UserEntity otherUser
) {
}
