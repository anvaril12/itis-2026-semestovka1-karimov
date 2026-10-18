package ru.itis.karimov.dao.mapper.impl;

import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.entity.RoomEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class RoomRowMapper implements RowMapper<RoomEntity> {
    @Override
    public RoomEntity mapRow(ResultSet resultSet) throws SQLException {
        return RoomEntity.builder()
                .id(resultSet.getLong("id"))
                .dormitoryId(resultSet.getInt("dormitory_id"))
                .ownerId(resultSet.getLong("owner_id"))
                .capacity(resultSet.getInt("capacity"))
                .roomNumber(resultSet.getString("room_number"))
                .description(resultSet.getString("description"))
                .createdAt(resultSet.getObject("created_at", LocalDateTime.class))
                .build();
    }
}
