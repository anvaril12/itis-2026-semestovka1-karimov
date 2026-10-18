package ru.itis.karimov.dao.mapper.impl;

import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.entity.ConfirmationCodeEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ConfirmationCodeRowMapper implements RowMapper<ConfirmationCodeEntity> {

    @Override
    public ConfirmationCodeEntity mapRow(ResultSet resultSet) throws SQLException {
        return ConfirmationCodeEntity.builder()
                .id(resultSet.getLong("id"))
                .userId(resultSet.getLong("user_id"))
                .code(resultSet.getString("code"))
                .sendAt(resultSet.getObject("send_at", LocalDateTime.class))
                .build();
    }
}
