package com.course.aca.model.mapper;

import com.course.aca.model.entity.FileItemEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Spring JDBC Mapper class which maps data from DataBase to {@link FileItemEntity} Entity.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class FileItemMapper implements RowMapper<FileItemEntity>{

    @Override
    public FileItemEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        FileItemEntity entity = new FileItemEntity();

        entity.setId(rs.getInt("id"));
        entity.setName(rs.getString("name"));
        entity.setPublic(rs.getBoolean("isPublic"));

        return entity;
    }
}
