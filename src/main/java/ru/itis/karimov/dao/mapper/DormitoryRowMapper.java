package ru.itis.karimov.dao.mapper;

import ru.itis.karimov.entity.DormitoryEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DormitoryRowMapper implements RowMapper<DormitoryEntity> {

    @Override
    public DormitoryEntity mapRow(ResultSet resultSet) throws SQLException {
        return DormitoryEntity.builder()
                .id(resultSet.getInt("id"))
                .universityId(resultSet.getInt("university_id"))
                .name(resultSet.getString("name"))
                .address(resultSet.getString("address"))
                .build();
    }
}
