package ru.itis.karimov.entity;

import lombok.*;
import ru.itis.karimov.entity.enums.ComplaintStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComplaintEntity {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private String text;
    private ComplaintStatus status;
    private LocalDateTime createdAt;

}
