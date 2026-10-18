package ru.itis.karimov.dao.mapper.impl;

import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.entity.enums.Gender;
import ru.itis.karimov.entity.enums.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class UserRowMapper implements RowMapper<UserEntity> {


    @Override
    public UserEntity mapRow(ResultSet resultSet) throws SQLException {
        return UserEntity.builder()
                .id(resultSet.getLong("id"))
                .login(resultSet.getString("login"))
                .firstName(resultSet.getString("firstname"))
                .lastName(resultSet.getString("lastname"))
                .email(resultSet.getString("email"))
                .hashPassword(resultSet.getString("hash_password"))
                .gender(Gender.valueOf(resultSet.getString("gender")))
                .contacts(resultSet.getString("contacts"))
                .role(Role.valueOf(resultSet.getString("role")))
                .course(resultSet.getInt("course"))
                .birthYear(resultSet.getInt("birth_year"))
                .createdAt(resultSet.getObject("created_at", LocalDateTime.class))
                .isBlocked(resultSet.getBoolean("is_blocked"))
                .isConfirmed(resultSet.getBoolean("is_confirmed"))
                .dormitoryId(resultSet.getObject("dormitory_id", Integer.class))
                .build();
    }
}
