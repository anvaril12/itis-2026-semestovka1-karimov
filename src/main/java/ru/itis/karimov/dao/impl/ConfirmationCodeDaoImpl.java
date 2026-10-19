package ru.itis.karimov.dao.impl;

import ru.itis.karimov.dao.ConfirmationCodeDao;
import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.dao.mapper.impl.ConfirmationCodeRowMapper;
import ru.itis.karimov.entity.ConfirmationCodeEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

public class ConfirmationCodeDaoImpl implements ConfirmationCodeDao {
    private static final String SQL_SAVE = "insert into confirmation_codes (user_id, code) " +
            "values(?, ?) returning id, send_at";
    private static final String SQL_FIND_BY_CODE = "select id, user_id, code, send_at from " +
            "confirmation_codes where code = ?";
    private static final String SQL_DELETE_BY_USER_ID = "delete from confirmation_codes where user_id = ?";

    private final DataSource dataSource;
    private final RowMapper<ConfirmationCodeEntity> confirmationCodeEntityRowMapper =
            new ConfirmationCodeRowMapper();

    public ConfirmationCodeDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ConfirmationCodeEntity save(ConfirmationCodeEntity code) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {

            preparedStatement.setLong(1, code.getUserId());
            preparedStatement.setString(2, code.getCode());

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                code.setId(resultSet.getLong("id"));
                code.setSendAt(resultSet.getObject("send_at", LocalDateTime.class));
                return code;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось сохранить код подтверждения для пользователя: " + code.getUserId(), e);
        }
    }

    @Override
    public Optional<ConfirmationCodeEntity> findByCode(String code) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_CODE)) {
            preparedStatement.setString(1, code);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return Optional.of(confirmationCodeEntityRowMapper.mapRow(resultSet));
                }
                return Optional.empty();
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти строку с кодом:", e);
        }
    }

    @Override
    public void deleteByUserId(Long userId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_BY_USER_ID)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось удалить код у пользователя: " + userId, e);
        }
    }
}
