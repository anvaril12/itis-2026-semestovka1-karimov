package ru.itis.karimov.dao.impl;

import ru.itis.karimov.dao.UserAnswerDao;
import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.dao.mapper.impl.UserAnswerRowMapper;
import ru.itis.karimov.entity.UserAnswerEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserAnswerDaoImpl implements UserAnswerDao {

    private static final String SQL_FIND_BY_USER_ID = "select * from user_answers where user_id = ? order by question_id";
    private static final String SQL_SAVE = "insert into user_answers (user_id, question_id, value, importance, is_dealbreaker)\n" +
            "values (?, ?, ?, ?, ?)\n" +
            "on conflict (user_id, question_id)\n" +
            "do update set value = excluded.value,\n" +
            "              importance = excluded.importance,\n" +
            "              is_dealbreaker = excluded.is_dealbreaker";

    private final DataSource dataSource;
    private final RowMapper<UserAnswerEntity> userAnswerEntityRowMapper = new UserAnswerRowMapper();

    public UserAnswerDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<UserAnswerEntity> findByUserId(Long userId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_USER_ID)) {
            preparedStatement.setLong(1, userId);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<UserAnswerEntity> list = new ArrayList<>();
                while(resultSet.next()) {
                    list.add(userAnswerEntityRowMapper.mapRow(resultSet));
                }
                return list;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти ответы пользователя по его id: " + userId, e);
        }
    }

    @Override
    public void save(UserAnswerEntity answer) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            preparedStatement.setLong(1, answer.getUserId());
            preparedStatement.setInt(2, answer.getQuestionId());
            preparedStatement.setInt(3, answer.getValue());
            preparedStatement.setInt(4, answer.getImportance());
            preparedStatement.setBoolean(5, answer.isDealBreaker());
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось добавить/обновить ответы пользователя:" + answer.getUserId(), e);
        }
    }
}
