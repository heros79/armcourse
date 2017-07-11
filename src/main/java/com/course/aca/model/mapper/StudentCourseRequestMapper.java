package com.course.aca.model.mapper;

import com.course.aca.model.entity.StudentCourseRequestEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Spring JDBC Mapper class which maps data from DataBase to {@link StudentCourseRequestEntity} Entity.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class StudentCourseRequestMapper implements RowMapper<StudentCourseRequestEntity> {
    @Override
    public StudentCourseRequestEntity mapRow(ResultSet resultSet, int i) throws SQLException {
        StudentCourseRequestEntity requestEntity = new StudentCourseRequestEntity();

        requestEntity.setId(resultSet.getInt("id"));
        requestEntity.setStudentId(resultSet.getInt("studentId"));
        requestEntity.setStudentFullName(resultSet.getString("studentName"));
        requestEntity.setCourseId(resultSet.getInt("courseId"));
        requestEntity.setCourseName(resultSet.getString("courseName"));

        return requestEntity;
    }
}
