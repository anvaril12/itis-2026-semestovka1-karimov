package ru.itis.karimov.entity;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RoomEntity {

    private Long id;
    private Integer dormitoryId;
    private Long ownerId;
    private Integer capacity;
    private String roomNumber;
    private String description;
    private LocalDateTime createdAt;

}
