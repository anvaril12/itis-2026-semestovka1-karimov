package ru.itis.karimov.dao;

import ru.itis.karimov.entity.RoomMemberEntity;
import ru.itis.karimov.entity.enums.RoomMemberStatus;

import java.util.List;
import java.util.Optional;

public interface RoomMemberDao {
    RoomMemberEntity save(RoomMemberEntity member);
    List<RoomMemberEntity> findByRoomId(Long roomId);
    List<RoomMemberEntity> findByUserId(Long userId);
    Optional<RoomMemberEntity> find(Long roomId, Long userId);
    void updateStatus(Long roomId, Long userId, RoomMemberStatus status);
    void delete(Long roomId, Long userId);
}
