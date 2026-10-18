package ru.itis.karimov.dao.mapper.impl;

import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.entity.AnswerOptionEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnswerOptionRowMapper implements RowMapper<AnswerOptionEntity> {
    @Override
    public AnswerOptionEntity mapRow(ResultSet resultSet) throws SQLException {
        return AnswerOptionEntity.builder()
                .id(resultSet.getInt("id"))
                .questionId(resultSet.getInt("question_id"))
                .value(resultSet.getInt("value"))
                .label(resultSet.getString("label"))
                .build();
    }
}
