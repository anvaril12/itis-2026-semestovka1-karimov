package ru.itis.karimov.entity;

import lombok.*;
import ru.itis.karimov.entity.enums.RoommateRequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoommateRequestEntity {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private RoommateRequestStatus status;
    private LocalDateTime createdAt;

}
