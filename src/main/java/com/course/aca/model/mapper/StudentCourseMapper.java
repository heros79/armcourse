package com.course.aca.model.mapper;

import com.course.aca.model.entity.StudentCourseEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Spring JDBC Mapper class which maps data from DataBase to {@link StudentCourseEntity} Entity.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class StudentCourseMapper implements RowMapper<StudentCourseEntity> {
    @Override
    public StudentCourseEntity mapRow(ResultSet resultSet, int i) throws SQLException {
        StudentCourseEntity studentCourseEntity = new StudentCourseEntity();

        studentCourseEntity.setId(resultSet.getInt("id"));
        studentCourseEntity.setCourseId(resultSet.getInt("courseId"));
        studentCourseEntity.setStudentId(resultSet.getInt("studentId"));
        studentCourseEntity.setRating(resultSet.getInt("rating"));
        studentCourseEntity.setReview(resultSet.getString("review"));

        return studentCourseEntity;
    }
}
