package ru.itis.karimov.dao.mapper.impl;

import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.entity.UserAnswerEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAnswerRowMapper implements RowMapper<UserAnswerEntity> {
    @Override
    public UserAnswerEntity mapRow(ResultSet resultSet) throws SQLException {
        return UserAnswerEntity.builder()
                .userId(resultSet.getLong("user_id"))
                .questionId(resultSet.getInt("question_id"))
                .value(resultSet.getInt("value"))
                .importance(resultSet.getInt("importance"))
                .isDealBreaker(resultSet.getBoolean("is_dealbreaker"))
                .build();
    }
}
