package ru.itis.karimov.dao.mapper.impl;

import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.entity.RoommateRequestEntity;
import ru.itis.karimov.entity.enums.RoommateRequestStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class RoommateRequestRowMapper implements RowMapper<RoommateRequestEntity> {
    @Override
    public RoommateRequestEntity mapRow(ResultSet resultSet) throws SQLException {
        return RoommateRequestEntity.builder()
                .id(resultSet.getLong("id"))
                .senderId(resultSet.getLong("sender_id"))
                .receiverId(resultSet.getLong("receiver_id"))
                .status(RoommateRequestStatus.valueOf(resultSet.getString("status")))
                .createdAt(resultSet.getObject("created_at", LocalDateTime.class))
                .build();
    }
}
