package ru.itis.karimov.dao.impl;

import ru.itis.karimov.dao.UserDao;
import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.dao.mapper.impl.UserRowMapper;
import ru.itis.karimov.entity.UserEntity;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    private static final String SQL_FIND_BY_LOGIN = "select * from users where login = ?";
    private static final String SQL_FIND_BY_EMAIL = "select * from users where email = ?";
    private static final String SQL_FIND_BY_ID = "select * from users where id = ?";
    private static final String SQL_SAVE = "insert into users (login, firstname, lastname," +
            " email, hash_password, gender, contacts, role, course, birth_year) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning id, created_at";
    private static final String SQL_FIND_ALL = "select * from users order by id";
    private static final String SQL_DELETE_BY_ID = "delete from users where id = ?";
    private static final String SQL_UPDATE = "update users set login = ?, firstname = ?, " +
            "lastname = ?, email = ?, hash_password = ?, gender = ?, contacts = ?, " +
            "role = ?, course = ?, birth_year = ?, is_blocked = ?, is_confirmed = ?, " +
            "dormitory_id = ? where id = ?";
    private static final String SQL_FIND_BY_DORMITORY_ID = "select * from users where dormitory_id = ?";
    private static final String SQL_UPDATE_BLOCKED = "update users set is_blocked = ? where id = ?";
    private static final String SQL_FIND_BY_BLOCKED = "select * from users where is_blocked = ? order by id";

    private final DataSource dataSource;
    private final RowMapper<UserEntity> entityRowMapper = new UserRowMapper();

    public UserDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<UserEntity> findByLogin(String login) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_LOGIN)) {
            preparedStatement.setString(1, login);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return Optional.of(entityRowMapper.mapRow(resultSet));
                }
                return Optional.empty();
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти пользователя по логину: " + login, e);
        }
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_EMAIL)) {
            preparedStatement.setString(1, email);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return Optional.of(entityRowMapper.mapRow(resultSet));
                }
                return Optional.empty();
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти пользователя по email: " + email, e);
        }
    }

    @Override
    public List<UserEntity> findByDormitoryId(Integer id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_DORMITORY_ID)) {
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<UserEntity> list = new ArrayList<>();
                while(resultSet.next()) {
                    list.add(entityRowMapper.mapRow(resultSet));
                }
                return list;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти пользователей по id общежития: " + id, e);
        }
    }

    @Override
    public void updateBlocked(Long userId, boolean blocked) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_BLOCKED)) {
            preparedStatement.setBoolean(1, blocked);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось изменить блокировку пользователя с id: " + userId, e);
        }
    }

    @Override
    public List<UserEntity> findByBlocked(boolean blocked) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_BLOCKED)) {
            preparedStatement.setBoolean(1, blocked);

            List<UserEntity> list = new ArrayList<>();
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    list.add(entityRowMapper.mapRow(resultSet));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти пользователей", e);
        }
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return Optional.of(entityRowMapper.mapRow(resultSet));
                }
                return Optional.empty();
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти пользователя по id: " + id, e);
        }
    }

    @Override
    public UserEntity save(UserEntity entity) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            preparedStatement.setString(1,entity.getLogin());
            preparedStatement.setString(2,entity.getFirstName());
            preparedStatement.setString(3, entity.getLastName());
            preparedStatement.setString(4,entity.getEmail());
            preparedStatement.setString(5, entity.getHashPassword());
            preparedStatement.setString(6,entity.getGender().name());
            preparedStatement.setString(7, entity.getContacts());
            preparedStatement.setString(8, entity.getRole().name());
            preparedStatement.setInt(9, entity.getCourse());
            preparedStatement.setInt(10, entity.getBirthYear());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                entity.setId(resultSet.getLong("id"));
                entity.setCreatedAt(resultSet.getObject("created_at", LocalDateTime.class));
                return entity;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось сохранить пользователя: " + entity.getLogin(), e);
        }
    }


    @Override
    public List<UserEntity> findAll() {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL)) {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<UserEntity> list = new ArrayList<>();
                while(resultSet.next()) {
                    list.add(entityRowMapper.mapRow(resultSet));
                }
                return list;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти всех пользователей", e);
        }
    }

    @Override
    public void update(UserEntity user) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getHashPassword());
            preparedStatement.setString(6, user.getGender().name());
            preparedStatement.setString(7, user.getContacts());
            preparedStatement.setString(8, user.getRole().name());
            preparedStatement.setInt(9, user.getCourse());
            preparedStatement.setInt(10, user.getBirthYear());
            preparedStatement.setBoolean(11, user.isBlocked());
            preparedStatement.setBoolean(12, user.isConfirmed());
            preparedStatement.setObject(13, user.getDormitoryId(), Types.INTEGER);
            preparedStatement.setLong(14, user.getId());

            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось обновить пользователя с id: " + user.getId(), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_BY_ID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось удалить пользователя по id: " + id, e);
        }
    }
}
