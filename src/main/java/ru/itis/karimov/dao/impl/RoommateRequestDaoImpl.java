package ru.itis.karimov.dao.impl;

import ru.itis.karimov.dao.RoommateRequestDao;
import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.dao.mapper.impl.RoommateRequestRowMapper;
import ru.itis.karimov.entity.RoommateRequestEntity;
import ru.itis.karimov.entity.enums.RoommateRequestStatus;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoommateRequestDaoImpl implements RoommateRequestDao {

    private static final String SQL_SAVE = "insert into roommate_requests (sender_id, receiver_id) values (?, ?) returning id, status, created_at";
    private static final String SQL_FIND_BY_SENDER_ID = "select * from roommate_requests where sender_id = ? order by created_at desc";
    private static final String SQL_FIND_BY_RECEIVER_ID = "select * from roommate_requests where receiver_id = ? order by created_at desc";
    private static final String SQL_FIND_BY_ID = "select * from roommate_requests where id = ?";
    private static final String SQL_DELETE_BY_ID = "delete from roommate_requests where id = ?";
    private static final String SQL_UPDATE_STATUS = "update roommate_requests set status = ? where id = ?";
    private static final String SQL_FIND_BETWEEN = "select * from roommate_requests " +
            "where (sender_id = ? and receiver_id = ?) or (sender_id = ? and receiver_id = ?)";

    private final DataSource dataSource;
    private final RowMapper<RoommateRequestEntity> roommateRequestEntityRowMapper = new RoommateRequestRowMapper();

    public RoommateRequestDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public RoommateRequestEntity save(RoommateRequestEntity request) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            preparedStatement.setLong(1, request.getSenderId());
            preparedStatement.setLong(2, request.getReceiverId());
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                request.setId(resultSet.getLong("id"));
                request.setStatus(RoommateRequestStatus.valueOf(resultSet.getString("status")));
                request.setCreatedAt(resultSet.getObject("created_at", LocalDateTime.class));
                return request;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось сохранить заявку: "+ request.getSenderId(), e);
        }
    }

    @Override
    public List<RoommateRequestEntity> findBySenderId(Long senderId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_SENDER_ID)) {
            preparedStatement.setLong(1, senderId);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<RoommateRequestEntity> list = new ArrayList<>();
                while(resultSet.next()) {
                    list.add(roommateRequestEntityRowMapper.mapRow(resultSet));
                }
                return list;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти заявку по id отправителя: " + senderId, e);
        }
    }

    @Override
    public List<RoommateRequestEntity> findByReceiverId(Long receiverId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_RECEIVER_ID)) {
            preparedStatement.setLong(1, receiverId);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<RoommateRequestEntity> list = new ArrayList<>();
                while(resultSet.next()) {
                    list.add(roommateRequestEntityRowMapper.mapRow(resultSet));
                }
                return list;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти заявку по id получателя: " + receiverId, e);
        }
    }

    @Override
    public Optional<RoommateRequestEntity> findById(Long id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return Optional.of(roommateRequestEntityRowMapper.mapRow(resultSet));
                }
                return Optional.empty();
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти заявку по id: " + id, e);
        }
    }

    @Override
    public void updateStatus(Long id, RoommateRequestStatus status) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_STATUS)) {
            preparedStatement.setString(1, status.name());
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();
        }catch(SQLException e) {
            throw new RuntimeException("Не удалось обновить статус заявки: " + id, e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_BY_ID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось удалить заявку по id: " + id, e);
        }
    }

    @Override
    public Optional<RoommateRequestEntity> findBetween(Long userId1, Long userId2) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BETWEEN)) {
            preparedStatement.setLong(1, userId1);
            preparedStatement.setLong(2, userId2);
            preparedStatement.setLong(3, userId2);
            preparedStatement.setLong(4, userId1);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(roommateRequestEntityRowMapper.mapRow(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти заявку между пользователями: " + userId1 + " и " + userId2, e);
        }
    }
}
