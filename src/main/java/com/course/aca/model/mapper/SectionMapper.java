package com.course.aca.model.mapper;

import com.course.aca.model.entity.SectionEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Spring JDBC Mapper class which maps data from DataBase to {@link SectionEntity} Entity.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class SectionMapper implements RowMapper<SectionEntity> {
    @Override
    public SectionEntity mapRow(ResultSet resultSet, int i) throws SQLException {
        SectionEntity sectionEntity = new SectionEntity();

        sectionEntity.setId(resultSet.getInt("id"));
        sectionEntity.setCourseId(resultSet.getInt("courseId"));
        sectionEntity.setName(resultSet.getString("name"));

        return sectionEntity;
    }
}
