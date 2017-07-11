package com.course.aca.model.mapper;

import com.course.aca.model.entity.AdminCommentEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Spring JDBC Mapper class which maps data from DataBase to {@link AdminCommentEntity} Entity.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class AdminCommentMapper implements RowMapper<AdminCommentEntity>{
    @Override
    public AdminCommentEntity mapRow(ResultSet resultSet, int i) throws SQLException {
        AdminCommentEntity adminCommentEntity = new AdminCommentEntity();

        adminCommentEntity.setId(resultSet.getInt("id"));
        adminCommentEntity.setCourseId(resultSet.getInt("courseId"));
        adminCommentEntity.setComment(resultSet.getString("comment"));

        return adminCommentEntity;
    }
}
