package ru.itis.karimov.dao.mapper.impl;

import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.entity.DormReviewEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class DormReviewRowMapper implements RowMapper<DormReviewEntity> {
    @Override
    public DormReviewEntity mapRow(ResultSet resultSet) throws SQLException {
        return DormReviewEntity.builder()
                .id(resultSet.getLong("id"))
                .userId(resultSet.getLong("user_id"))
                .dormitoryId(resultSet.getInt("dormitory_id"))
                .text(resultSet.getString("text"))
                .rating(resultSet.getInt("rating"))
                .createdAt(resultSet.getObject("created_at", LocalDateTime.class))
                .build();
    }
}
