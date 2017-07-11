package com.course.aca.model.mapper;

import com.course.aca.model.entity.CourseCommonInfoEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Spring JDBC Mapper class which maps data from DataBase to {@link CourseCommonInfoEntity} Entity.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class CourseCommonInfoMapper implements RowMapper<CourseCommonInfoEntity> {
    @Override
    public CourseCommonInfoEntity mapRow(ResultSet resultSet, int i) throws SQLException {
        CourseCommonInfoEntity courseCommonInfoEntity = new CourseCommonInfoEntity();
        courseCommonInfoEntity.setId(resultSet.getInt("id"));
        courseCommonInfoEntity.setName(resultSet.getString("name"));
        courseCommonInfoEntity.setStatus(resultSet.getString("status"));

        Blob blob = resultSet.getBlob("description");
        byte[] bytes;
        if (blob != null)
            bytes = blob.getBytes(1, (int) blob.length());
        else
            bytes = new byte[]{};

        courseCommonInfoEntity.setDescription(new String(bytes));

        courseCommonInfoEntity.setRating(resultSet.getDouble("rating"));
        courseCommonInfoEntity.setReviewCount(resultSet.getInt("reviewCount"));
        courseCommonInfoEntity.setPrice(resultSet.getInt("price"));
        courseCommonInfoEntity.setTeacherId(resultSet.getInt("teacherId"));
        courseCommonInfoEntity.setSubCategoryId(resultSet.getInt("subcategoryId"));
        courseCommonInfoEntity.setTotalHours(resultSet.getDouble("totalHours"));
        courseCommonInfoEntity.setTotalMaterials(resultSet.getInt("totalMaterials"));
        courseCommonInfoEntity.setTeacherName(resultSet.getString("teacherName"));

        return courseCommonInfoEntity;
    }
}
