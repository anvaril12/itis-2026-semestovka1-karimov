package ru.itis.karimov.dao.mapper.impl;

import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.entity.ComplaintEntity;
import ru.itis.karimov.entity.enums.ComplaintStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ComplaintRowMapper implements RowMapper<ComplaintEntity> {
    @Override
    public ComplaintEntity mapRow(ResultSet resultSet) throws SQLException {
        return ComplaintEntity.builder()
                .id(resultSet.getLong("id"))
                .senderId(resultSet.getLong("sender_id"))
                .receiverId(resultSet.getLong("receiver_id"))
                .text(resultSet.getString("text"))
                .status(ComplaintStatus.valueOf(resultSet.getString("status")))
                .createdAt(resultSet.getObject("created_at", LocalDateTime.class))
                .build();
    }
}
