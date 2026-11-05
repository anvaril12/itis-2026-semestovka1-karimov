package ru.itis.karimov.dao.impl;

import ru.itis.karimov.dao.UniversityDao;
import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.dao.mapper.impl.UniversityRowMapper;
import ru.itis.karimov.entity.UniversityEntity;
import ru.itis.karimov.exception.DuplicateException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UniversityDaoImpl implements UniversityDao {

    private static final String SQL_FIND_ALL = "select id, name, city from universities order by name";
    private static final String SQL_FIND_BY_ID = "select id, name, city from universities where id = ?";
    private static final String SQL_SAVE = "insert into universities(name, city) values(?, ?) returning id";

    private final DataSource dataSource;
    private final RowMapper<UniversityEntity> mapper = new UniversityRowMapper();

    public UniversityDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<UniversityEntity> findAll() {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL)) {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {

                List<UniversityEntity> result = new ArrayList<>();
                while(resultSet.next()) {
                    result.add(mapper.mapRow(resultSet));
                }
                return result;
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось получить список университетов", e);
        }
    }

    @Override
    public Optional<UniversityEntity> findById(Integer id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return Optional.of(mapper.mapRow(resultSet));
                }
                return Optional.empty();
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось найти университет по id: " + id, e);
        }
    }

    @Override
    public UniversityEntity save(UniversityEntity university) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            preparedStatement.setString(1, university.getName());
            preparedStatement.setString(2, university.getCity());
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                university.setId(resultSet.getInt("id"));
                return university;
            }
        } catch(SQLException e) {
            if("23505".equals(e.getSQLState())) {
                throw new DuplicateException("Такой ВУЗ в этом городе уже есть");
            }
            throw new RuntimeException("Не удалось сохранить университет", e);
        }
    }
}
