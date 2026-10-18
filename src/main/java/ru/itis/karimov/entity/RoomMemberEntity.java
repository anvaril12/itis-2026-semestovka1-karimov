package ru.itis.karimov.entity;

import lombok.*;
import ru.itis.karimov.entity.enums.RoomMemberStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomMemberEntity {

    private Long roomId;
    private Long userId;
    private RoomMemberStatus status;

}
