package com.course.aca.model.mapper;

import com.course.aca.model.entity.FileEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Spring JDBC Mapper class which maps data from DataBase to {@link FileEntity} Entity.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class FileMapper implements RowMapper<FileEntity> {
    @Override
    public FileEntity mapRow(ResultSet resultSet, int i) throws SQLException {
        FileEntity fileEntity = new FileEntity();

        fileEntity.setId(resultSet.getInt("id"));
        fileEntity.setName(resultSet.getString("name"));
        fileEntity.setResourceName(resultSet.getString("resourceName"));
        fileEntity.setResourceType(resultSet.getString("type"));
        fileEntity.setPublic(resultSet.getBoolean("isPublic"));
        fileEntity.setSectionId(resultSet.getInt("sectionId"));

        return fileEntity;
    }
}
