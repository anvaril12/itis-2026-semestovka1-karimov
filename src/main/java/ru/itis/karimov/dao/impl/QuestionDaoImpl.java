package ru.itis.karimov.dao.impl;

import ru.itis.karimov.dao.QuestionDao;
import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.dao.mapper.impl.QuestionRowMapper;
import ru.itis.karimov.entity.QuestionEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuestionDaoImpl implements QuestionDao {

    private static final String SQL_FIND_ALL_ACTIVE = "select * from questions where is_active = true order by id";
    private static final String SQL_FIND_BY_ID = "select * from questions where id = ?";
    private static final String SQL_SAVE = "insert into questions (text, is_active) values (?, ?) returning id, created_at";
    private static final String SQL_FIND_ALL = "select * from questions order by id";
    private static final String SQL_UPDATE = "update questions set text = ?, is_active = ? where id = ?";
    private static final String SQL_DELETE_BY_ID = "delete from questions where id = ?";

    private final DataSource dataSource;
    private final RowMapper<QuestionEntity> questionEntityRowMapper = new QuestionRowMapper();

    public QuestionDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<QuestionEntity> findAllActive() {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_ACTIVE)) {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<QuestionEntity> list = new ArrayList<>();
                while(resultSet.next()) {
                    list.add(questionEntityRowMapper.mapRow(resultSet));
                }
                return list;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось показать активные вопросы:", e);
        }
    }

    @Override
    public Optional<QuestionEntity> findById(Integer id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return Optional.of(questionEntityRowMapper.mapRow(resultSet));
                }
                return Optional.empty();
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти вопрос по id: " + id, e);
        }
    }

    @Override
    public QuestionEntity save(QuestionEntity entity) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            preparedStatement.setString(1,entity.getText());
            preparedStatement.setBoolean(2, entity.isActive());
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                entity.setId(resultSet.getInt("id"));
                entity.setCreatedAt(resultSet.getObject("created_at", LocalDateTime.class));
                return entity;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось сохранить вопрос:", e);
        }
    }

    @Override
    public List<QuestionEntity> findAll() {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL)) {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<QuestionEntity> list = new ArrayList<>();
                while(resultSet.next()) {
                    list.add(questionEntityRowMapper.mapRow(resultSet));
                }
                return list;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти все вопросы:", e);
        }
    }

    @Override
    public void update(QuestionEntity entity) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            preparedStatement.setString(1, entity.getText());
            preparedStatement.setBoolean(2, entity.isActive());
            preparedStatement.setInt(3, entity.getId());
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось обновить вопрос:", e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_BY_ID)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }catch(SQLException e) {
            throw new RuntimeException("Не удалось удалить вопрос по id: " + id, e);
        }
    }
}
