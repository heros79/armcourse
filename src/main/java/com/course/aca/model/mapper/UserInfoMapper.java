package com.course.aca.model.mapper;

import com.course.aca.model.entity.UserInfoEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Spring JDBC Mapper class which maps data from DataBase to {@link UserInfoEntity} Entity.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class UserInfoMapper implements RowMapper<UserInfoEntity> {
    @Override
    public UserInfoEntity mapRow(ResultSet resultSet, int i) throws SQLException {
        UserInfoEntity entity = new UserInfoEntity();

        entity.setFirstName(resultSet.getString("firstName"));
        entity.setLastName(resultSet.getString("lastName"));
        entity.setRoleId(resultSet.getInt("roleId"));

        return entity;
    }
}
