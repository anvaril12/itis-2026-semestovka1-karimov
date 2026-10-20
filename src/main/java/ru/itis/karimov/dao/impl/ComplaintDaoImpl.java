package ru.itis.karimov.dao.impl;

import ru.itis.karimov.dao.ComplaintDao;
import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.dao.mapper.impl.ComplaintRowMapper;
import ru.itis.karimov.entity.ComplaintEntity;
import ru.itis.karimov.entity.enums.ComplaintStatus;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComplaintDaoImpl implements ComplaintDao {
    private static final String SQL_SAVE = "insert into complaints (sender_id, receiver_id, text) " +
            "values (?, ?, ?) returning id, status, created_at";
    private static final String SQL_FIND_BY_ID = "select * from complaints where id = ?";
    private static final String SQL_FIND_ALL = "select * from complaints order by created_at desc";
    private static final String SQL_FIND_BY_STATUS = "select * from complaints where status = ? " +
            "order by created_at desc";
    private static final String SQL_UPDATE_STATUS = "update complaints set status = ? where id = ?";

    private final DataSource dataSource;
    private final RowMapper<ComplaintEntity> complaintEntityRowMapper = new ComplaintRowMapper();

    public ComplaintDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ComplaintEntity save(ComplaintEntity complaint) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            preparedStatement.setLong(1, complaint.getSenderId());
            preparedStatement.setLong(2, complaint.getReceiverId());
            preparedStatement.setString(3, complaint.getText());
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                complaint.setId(resultSet.getLong("id"));
                complaint.setStatus(ComplaintStatus.valueOf(resultSet.getString("status")));
                complaint.setCreatedAt(resultSet.getObject("created_at", LocalDateTime.class));
                return complaint;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось добавить жалобу: " + complaint.getSenderId(), e);
        }
    }

    @Override
    public Optional<ComplaintEntity> findById(Long id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return Optional.of(complaintEntityRowMapper.mapRow(resultSet));
                }
                return Optional.empty();
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти жалобу по id: " + id, e);
        }
    }

    @Override
    public List<ComplaintEntity> findAll() {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL)) {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<ComplaintEntity> list = new ArrayList<>();
                while(resultSet.next()) {
                    list.add(complaintEntityRowMapper.mapRow(resultSet));
                }
                return list;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти все жалобы", e);
        }
    }

    @Override
    public List<ComplaintEntity> findByStatus(ComplaintStatus status) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_STATUS)) {
            preparedStatement.setString(1, status.name());
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<ComplaintEntity> list = new ArrayList<>();
                while(resultSet.next()) {
                    list.add(complaintEntityRowMapper.mapRow(resultSet));
                }
                return list;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти все жалобы по статусу: " + status, e);
        }
    }

    @Override
    public void updateStatus(Long id, ComplaintStatus status) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_STATUS)) {
            preparedStatement.setString(1, status.name());
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось обновить статус по id: " + id, e);
        }
    }
}
