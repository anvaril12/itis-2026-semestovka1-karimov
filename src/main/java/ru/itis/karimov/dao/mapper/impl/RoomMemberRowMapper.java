package ru.itis.karimov.dao.mapper.impl;

import ru.itis.karimov.dao.mapper.RowMapper;
import ru.itis.karimov.entity.RoomMemberEntity;
import ru.itis.karimov.entity.enums.RoomMemberStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomMemberRowMapper implements RowMapper<RoomMemberEntity> {
    @Override
    public RoomMemberEntity mapRow(ResultSet resultSet) throws SQLException {
        return RoomMemberEntity.builder()
                .roomId(resultSet.getLong("room_id"))
                .userId(resultSet.getLong("user_id"))
                .status(RoomMemberStatus.valueOf(resultSet.getString("status")))
                .build();
    }
}
