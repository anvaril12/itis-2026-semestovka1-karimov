package ru.itis.karimov.dao.impl;

import ru.itis.karimov.dao.AnswerOptionDao;
import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.dao.mapper.impl.AnswerOptionRowMapper;
import ru.itis.karimov.entity.AnswerOptionEntity;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnswerOptionDaoImpl implements AnswerOptionDao {

    private static final String SQL_DELETE_BY_ID = "delete from answer_options where id = ?";
    private static final String SQL_UPDATE = "update answer_options set value = ?, label = ? where id = ?";
    private static final String SQL_FIND_ALL = "select * from answer_options order by question_id, value";
    private static final String SQL_SAVE = "insert into answer_options (question_id, value, label) values (?, ?, ?) returning id";
    private static final String SQL_FIND_BY_ID = "select * from answer_options where id = ?";
    private static final String SQL_FIND_BY_QUESTION_ID = "select * from answer_options where question_id = ? order by value";

    private final DataSource dataSource;
    private final RowMapper<AnswerOptionEntity> answerOptionEntityRowMapper = new AnswerOptionRowMapper();

    public AnswerOptionDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<AnswerOptionEntity> findByQuestionId(Integer questionId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_QUESTION_ID)) {
            preparedStatement.setInt(1, questionId);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<AnswerOptionEntity> list = new ArrayList<>();
                while(resultSet.next()) {
                    list.add(answerOptionEntityRowMapper.mapRow(resultSet));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти варианты ответа по id вопроса: " + questionId, e);
        }
    }

    @Override
    public Optional<AnswerOptionEntity> findById(Integer id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return Optional.of(answerOptionEntityRowMapper.mapRow(resultSet));
                }
                return Optional.empty();
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти вариант ответа по id: " + id, e);
        }
    }

    @Override
    public AnswerOptionEntity save(AnswerOptionEntity entity) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            preparedStatement.setInt(1, entity.getQuestionId());
            preparedStatement.setInt(2,entity.getValue());
            preparedStatement.setString(3, entity.getLabel());
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                entity.setId(resultSet.getInt("id"));
                return entity;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось сохранить вариант ответа для вопроса с id: " + entity.getQuestionId(), e);
        }
    }

    @Override
    public List<AnswerOptionEntity> findAll() {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL)) {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<AnswerOptionEntity> list = new ArrayList<>();
                while(resultSet.next()) {
                    list.add(answerOptionEntityRowMapper.mapRow(resultSet));
                }
                return list;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти все варианты ответа", e);
        }
    }

    @Override
    public void update(AnswerOptionEntity entity) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            preparedStatement.setInt(1, entity.getValue());
            preparedStatement.setString(2, entity.getLabel());
            preparedStatement.setInt(3, entity.getId());
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось обновить вариант ответа с id: " + entity.getId(), e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_BY_ID)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }catch(SQLException e) {
            throw new RuntimeException("Не удалось удалить вариант ответа по id: " + id, e);
        }
    }
}
