package ru.itis.karimov.service;

import ru.itis.karimov.dto.RequestView;

import java.util.List;

public interface RoommateRequestService {
    void send(Long senderId, Long receiverId);
    void accept(Long requestId, Long userId);
    void reject(Long requestId, Long userId);
    List<RequestView> getIncoming(Long userId);
    List<RequestView> getOutgoing(Long userId);
}
