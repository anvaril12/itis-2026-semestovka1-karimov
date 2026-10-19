package ru.itis.karimov.dao.impl;

import ru.itis.karimov.dao.RoomDao;
import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.dao.mapper.impl.RoomRowMapper;
import ru.itis.karimov.entity.RoomEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDaoImpl implements RoomDao {

    private static final String SQL_FIND_BY_DORMITORY_ID = "select * from rooms where dormitory_id = ? order by created_at desc";
    private static final String SQL_FIND_BY_OWNER_ID = "select * from rooms where owner_id = ? order by created_at desc";
    private static final String SQL_FIND_BY_ID = "select * from rooms where id = ?";
    private static final String SQL_SAVE = "insert into rooms (dormitory_id, owner_id, capacity," +
            " room_number, description) " +
            "values (?, ?, ?, ?, ?) returning id, created_at";
    private static final String SQL_UPDATE = "update rooms set capacity = ?, room_number = ?, description = ?" +
            " where id = ?";
    private static final String SQL_FIND_ALL = "select * from rooms order by id";
    private static final String SQL_DELETE_BY_ID = "delete from rooms where id = ?";

    private final DataSource dataSource;
    private final RowMapper<RoomEntity> roomEntityRowMapper = new RoomRowMapper();

    public RoomDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<RoomEntity> findByDormitoryId(Integer dormitoryId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_DORMITORY_ID)) {
            preparedStatement.setInt(1, dormitoryId);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<RoomEntity> list = new ArrayList<>();
                while(resultSet.next()) {
                    list.add(roomEntityRowMapper.mapRow(resultSet));
                }
                return list;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти комнату по id общежития: " + dormitoryId, e);
        }
    }

    @Override
    public List<RoomEntity> findByOwnerId(Long ownerId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_OWNER_ID)) {
            preparedStatement.setLong(1, ownerId);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<RoomEntity> list = new ArrayList<>();
                while(resultSet.next()) {
                    list.add(roomEntityRowMapper.mapRow(resultSet));
                }
                return list;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти комнату по id владельца: " + ownerId, e);
        }
    }

    @Override
    public Optional<RoomEntity> findById(Long id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return Optional.of(roomEntityRowMapper.mapRow(resultSet));
                }
                return Optional.empty();
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти комнату по id: " + id, e);
        }
    }

    @Override
    public RoomEntity save(RoomEntity entity) {
       try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            preparedStatement.setInt(1, entity.getDormitoryId());
            preparedStatement.setLong(2, entity.getOwnerId());
            preparedStatement.setInt(3, entity.getCapacity());
            preparedStatement.setString(4, entity.getRoomNumber());
            preparedStatement.setString(5, entity.getDescription());
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                entity.setId(resultSet.getLong("id"));
                entity.setCreatedAt(resultSet.getObject("created_at", LocalDateTime.class));
                return entity;
            }
       } catch(SQLException e) {
           throw new RuntimeException("Не удалось сохранить комнату пользователя: " + entity.getOwnerId(), e);
       }
    }

    @Override
    public List<RoomEntity> findAll() {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL)) {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<RoomEntity> list = new ArrayList<>();
                while(resultSet.next()) {
                    list.add(roomEntityRowMapper.mapRow(resultSet));
                }
                return list;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти все комнаты", e);
        }
    }

    @Override
    public void update(RoomEntity entity) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            preparedStatement.setInt(1, entity.getCapacity());
            preparedStatement.setString(2, entity.getRoomNumber());
            preparedStatement.setString(3, entity.getDescription());
            preparedStatement.setLong(4, entity.getId());
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось обновить комнату: " + entity.getId(), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_BY_ID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось удалить комнату по id: " + id, e);
        }
    }
}
