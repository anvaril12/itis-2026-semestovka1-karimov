package ru.itis.karimov.dao.impl;

import ru.itis.karimov.dao.DormitoryDao;
import ru.itis.karimov.dao.mapper.DormitoryRowMapper;
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
}
