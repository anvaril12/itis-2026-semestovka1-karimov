package ru.itis.karimov.dao.impl;

import ru.itis.karimov.dao.DormReviewDao;
import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.dao.mapper.impl.DormReviewRowMapper;
import ru.itis.karimov.entity.DormReviewEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DormReviewDaoImpl implements DormReviewDao {

    private static final String SQL_DELETE_BY_ID = "delete from dorm_reviews where id = ?";
    private static final String SQL_FIND_BY_DORMITORY_ID = "select * from dorm_reviews where dormitory_id = ? " +
            "order by created_at desc";
    private static final String SQL_FIND_BY_USER_AND_DORMITORY_ID = "select * from dorm_reviews where " +
            "user_id = ? and dormitory_id = ?";
    private static final String SQL_FIND_BY_ID = "select * from dorm_reviews where id = ?";
    private static final String SQL_FIND_ALL = "select * from dorm_reviews order by created_at desc";
    private static final String SQL_SAVE = "insert into dorm_reviews (user_id, dormitory_id, text, rating)" +
            " values (?, ?, ?, ?) returning id, created_at";
    private static final String SQL_UPDATE = "update dorm_reviews set text = ?, rating = ? where id = ?";

    private final DataSource dataSource;
    private final RowMapper<DormReviewEntity> dormReviewEntityRowMapper = new DormReviewRowMapper();

    public DormReviewDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<DormReviewEntity> findByDormitoryId(Integer dormitoryId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_DORMITORY_ID)) {
            preparedStatement.setInt(1, dormitoryId);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<DormReviewEntity> list = new ArrayList<>();
                while(resultSet.next()) {
                    list.add(dormReviewEntityRowMapper.mapRow(resultSet));
                }
                return list;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти отзывы по id общежития: " + dormitoryId, e);
        }
    }

    @Override
    public Optional<DormReviewEntity> findByUserIdAndDormitoryId(Long userId, Integer dormitoryId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_USER_AND_DORMITORY_ID)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setInt(2, dormitoryId);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return Optional.of(dormReviewEntityRowMapper.mapRow(resultSet));
                }
                return Optional.empty();
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти отзыв по id пользователя: " + userId +
                    " и id общежития " + dormitoryId, e);
        }
    }

    @Override
    public Optional<DormReviewEntity> findById(Long id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return Optional.of(dormReviewEntityRowMapper.mapRow(resultSet));
                }
                return Optional.empty();
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти отзыв по id: " + id, e);
        }
    }

    @Override
    public DormReviewEntity save(DormReviewEntity entity) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            preparedStatement.setLong(1, entity.getUserId());
            preparedStatement.setInt(2, entity.getDormitoryId());
            preparedStatement.setString(3, entity.getText());
            preparedStatement.setInt(4, entity.getRating());
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                entity.setId(resultSet.getLong("id"));
                entity.setCreatedAt(resultSet.getObject("created_at", LocalDateTime.class));
                return entity;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось сохранить отзыв пользователя: " + entity.getUserId(), e);
        }
    }

    @Override
    public List<DormReviewEntity> findAll() {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL)) {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<DormReviewEntity> list = new ArrayList<>();
                while(resultSet.next()) {
                    list.add(dormReviewEntityRowMapper.mapRow(resultSet));
                }
                return list;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти все отзывы", e);
        }
    }

    @Override
    public void update(DormReviewEntity entity) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            preparedStatement.setString(1, entity.getText());
            preparedStatement.setInt(2, entity.getRating());
            preparedStatement.setLong(3, entity.getId());
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось обновить отзыв: " + entity.getId(), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_BY_ID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось удалить отзыв по id: " + id, e);
        }
    }
}
