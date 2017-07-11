package com.course.aca.model.mapper;

import com.course.aca.model.entity.CourseEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Spring JDBC Mapper class which maps data from DataBase to {@link CourseEntity} Entity.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class CourseMapper implements RowMapper<CourseEntity>{

    @Override
    public CourseEntity mapRow(ResultSet resultSet, int i) throws SQLException {
        CourseEntity courseEntity = new CourseEntity();

        courseEntity.setId(resultSet.getInt("id"));
        courseEntity.setName(resultSet.getString("name"));
        courseEntity.setPrice(resultSet.getInt("price"));
        courseEntity.setRating(resultSet.getDouble("rating"));
        courseEntity.setReviewCount(resultSet.getInt("reviewCount"));
        courseEntity.setTeacherName(resultSet.getString("teacherName"));

        return courseEntity;
    }
}
