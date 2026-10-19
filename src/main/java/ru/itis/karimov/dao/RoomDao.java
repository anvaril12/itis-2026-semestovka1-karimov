package ru.itis.karimov.dao;

import ru.itis.karimov.entity.RoomEntity;

import java.util.List;

public interface RoomDao extends CrudDao<RoomEntity, Long> {
    List<RoomEntity> findByDormitoryId(Integer dormitoryId);
    List<RoomEntity> findByOwnerId(Long ownerId);
}
