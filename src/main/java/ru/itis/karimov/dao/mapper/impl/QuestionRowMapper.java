package ru.itis.karimov.dao.mapper.impl;

import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.entity.QuestionEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class QuestionRowMapper implements RowMapper<QuestionEntity> {
    @Override
    public QuestionEntity mapRow(ResultSet resultSet) throws SQLException {
        return QuestionEntity.builder()
                .id(resultSet.getInt("id"))
                .text(resultSet.getString("text"))
                .isActive(resultSet.getBoolean("is_active"))
                .createdAt(resultSet.getObject("created_at", LocalDateTime.class))
                .build();
    }
}
