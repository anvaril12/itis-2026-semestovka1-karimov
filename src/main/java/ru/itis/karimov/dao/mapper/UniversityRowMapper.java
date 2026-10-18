package ru.itis.karimov.dao.mapper;

import ru.itis.karimov.entity.UniversityEntity;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UniversityRowMapper implements RowMapper<UniversityEntity>{

    @Override
    public UniversityEntity mapRow(ResultSet resultSet) throws SQLException {
        return UniversityEntity.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .city(resultSet.getString("city"))
                .build();
    }
}
