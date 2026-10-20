package ru.itis.karimov.dao.impl;

import ru.itis.karimov.dao.RoomMemberDao;
import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.dao.mapper.impl.RoomMemberRowMapper;
import ru.itis.karimov.entity.RoomMemberEntity;
import ru.itis.karimov.entity.enums.RoomMemberStatus;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomMemberDaoImpl implements RoomMemberDao {

    private static final String SQL_SAVE = "insert into room_members (room_id, user_id) values (?,?) returning status";
    private static final String SQL_FIND_BY_ROOM_ID = "select * from room_members where room_id = ?";
    private static final String SQL_FIND_BY_USER_ID = "select * from room_members where user_id = ?";
    private static final String SQL_FIND = "select * from room_members where room_id = ? and user_id = ?";
    private static final String SQL_DELETE = "delete from room_members where room_id = ? and user_id = ?";
    private static final String SQL_UPDATE_STATUS = "update room_members set status = ? where room_id = ? and user_id = ?";

    private final DataSource dataSource;
    private final RowMapper<RoomMemberEntity> roomMemberEntityRowMapper = new RoomMemberRowMapper();

    public RoomMemberDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public RoomMemberEntity save(RoomMemberEntity member) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            preparedStatement.setLong(1, member.getRoomId());
            preparedStatement.setLong(2, member.getUserId());
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                member.setStatus(RoomMemberStatus.valueOf(resultSet.getString("status")));
                return member;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось сохранить пользователя в комнату: " + member.getUserId(), e);
        }
    }

    @Override
    public List<RoomMemberEntity> findByRoomId(Long roomId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ROOM_ID)) {
            preparedStatement.setLong(1, roomId);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<RoomMemberEntity> list = new ArrayList<>();
                while(resultSet.next()) {
                    list.add(roomMemberEntityRowMapper.mapRow(resultSet));
                }
                return list;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти жителей комнаты по id комнаты: " + roomId, e);
        }
    }

    @Override
    public List<RoomMemberEntity> findByUserId(Long userId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_USER_ID)) {
            preparedStatement.setLong(1, userId);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<RoomMemberEntity> list = new ArrayList<>();
                while(resultSet.next()) {
                    list.add(roomMemberEntityRowMapper.mapRow(resultSet));
                }
                return list;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти комнаты пользователя: " + userId, e);
        }
    }

    @Override
    public Optional<RoomMemberEntity> find(Long roomId, Long userId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND)) {
            preparedStatement.setLong(1 ,roomId);
            preparedStatement.setLong(2, userId);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return Optional.of(roomMemberEntityRowMapper.mapRow(resultSet));
                }
                return Optional.empty();
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти заявку пользователя: " + userId + " в комнату " + roomId, e);
        }
    }

    @Override
    public void updateStatus(Long roomId, Long userId, RoomMemberStatus status) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_STATUS)) {
            preparedStatement.setString(1, status.name());
            preparedStatement.setLong(2, roomId);
            preparedStatement.setLong(3, userId);
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось обновить статус у пользователя: " + userId + " в комнате " + roomId, e);
        }
    }

    @Override
    public void delete(Long roomId, Long userId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE)) {
            preparedStatement.setLong(1, roomId);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось отозвать заявку у пользователя: " + userId, e);
        }
    }
}
