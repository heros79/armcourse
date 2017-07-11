package com.course.aca.model.mapper;

import com.course.aca.model.entity.PathComponentsEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Spring JDBC Mapper class which maps data from DataBase to {@link PathComponentsEntity} Entity.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class PathComponentsEntityMapper implements RowMapper<PathComponentsEntity> {

    @Override
    public PathComponentsEntity mapRow(ResultSet resultSet, int i) throws SQLException {
        PathComponentsEntity pathComponentsEntity = new PathComponentsEntity();
        pathComponentsEntity.setCategoryName(resultSet.getString(1));
        pathComponentsEntity.setSubcategoryName(resultSet.getString(2));
        pathComponentsEntity.setCourseName(resultSet.getString(3));
        return pathComponentsEntity;//
        /*
        fileEntity.setId(resultSet.getInt("id"));
        fileEntity.setName(resultSet.getString("name"));
        fileEntity.setResourceName(resultSet.getString("resourceName"));
        fileEntity.setResourceType(resultSet.getString("type"));
        fileEntity.setPublic(resultSet.getBoolean("isPublic"));
        fileEntity.setSectionId(resultSet.getInt("sectionId"));

        return fileEntity;
    }*/
       // return null;
    }
}
