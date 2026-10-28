package ru.itis.karimov.service;

import ru.itis.karimov.dto.RoomApplicationView;
import ru.itis.karimov.dto.RoomMemberView;
import ru.itis.karimov.entity.enums.RoomMemberStatus;

import java.util.List;

public interface RoomMemberService {
    void join(Long roomId, Long userId);
    List<RoomMemberView> getMembers(Long roomId);
    void changeStatus(Long roomId, Long memberId, Long ownerId, RoomMemberStatus status);
    List<RoomApplicationView> getMyApplications(Long userId);
}
