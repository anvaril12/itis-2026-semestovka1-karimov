package ru.itis.karimov.dao.impl;

import ru.itis.karimov.dao.DormitoryDao;
import ru.itis.karimov.dao.mapper.impl.DormitoryRowMapper;
import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.entity.DormitoryEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DormitoryDaoImpl implements DormitoryDao {

    private static final String SQL_FIND_BY_ID = "select id, university_id, name, address from dormitories where id = ?";
    private static final String SQL_FIND_BY_UNIVERSITY_ID = "select id, university_id, name, address from dormitories where university_id = ? order by name";
    private static final String SQL_FIND_ALL = "select * from dormitories order by name";
    private static final String SQL_DELETE = "delete from dormitories where id = ?";
    private static final String SQL_UPDATE = "update dormitories set name = ?, address = ?, university_id = ? where id = ?";
    private static final String SQL_SAVE = "insert into dormitories(university_id, name, address) values(?, ?, ?) returning id";

    private final DataSource dataSource;
    private final RowMapper<DormitoryEntity> entityDormitoryRowMapper = new DormitoryRowMapper();

    public DormitoryDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<DormitoryEntity> findById(Integer id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID)) {

            preparedStatement.setInt(1, id);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return Optional.of(entityDormitoryRowMapper.mapRow(resultSet));
                }
                return Optional.empty();
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти общежитие с id: " + id, e);
        }
    }

    @Override
    public List<DormitoryEntity> findByUniversityId(Integer universityId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_UNIVERSITY_ID)) {

            preparedStatement.setInt(1, universityId);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<DormitoryEntity> list = new ArrayList<>();
                while(resultSet.next()) {
                    list.add(entityDormitoryRowMapper.mapRow(resultSet));
                }
                return list;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти общежитие с id университета: "+ universityId, e);
        }
    }

    @Override
    public List<DormitoryEntity> findAll() {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL)) {

            List<DormitoryEntity> list = new ArrayList<>();
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    list.add(entityDormitoryRowMapper.mapRow(resultSet));
                }
                return list;
             }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти все общежития", e);
        }
    }

    @Override
    public DormitoryEntity save(DormitoryEntity dormitoryEntity) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            preparedStatement.setInt(1, dormitoryEntity.getUniversityId());
            preparedStatement.setString(2, dormitoryEntity.getName());
            preparedStatement.setString(3, dormitoryEntity.getAddress());
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                dormitoryEntity.setId(resultSet.getInt("id"));
                return dormitoryEntity;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось добавить общежитие", e);
        }
    }

    @Override
    public void update(DormitoryEntity dormitoryEntity) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            preparedStatement.setString(1, dormitoryEntity.getName());
            preparedStatement.setString(2, dormitoryEntity.getAddress());
            preparedStatement.setInt(3, dormitoryEntity.getUniversityId());
            preparedStatement.setInt(4, dormitoryEntity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось обновить общежитие по id: " + dormitoryEntity.getId(), e);
        }
    }

    @Override
    public void delete(Integer id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось удалить общежитие по id: " + id, e);
        }
    }
}
