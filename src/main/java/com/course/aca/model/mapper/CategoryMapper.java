package com.course.aca.model.mapper;

import com.course.aca.model.entity.CategoryEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Spring JDBC Mapper class which maps data from DataBase to {@link CategoryEntity} Entity.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class CategoryMapper implements RowMapper<CategoryEntity> {

    @Override
    public CategoryEntity mapRow(ResultSet resultSet, int i) throws SQLException {

        CategoryEntity categoryEntity = new CategoryEntity();

        categoryEntity.setId(resultSet.getInt("id"));
        categoryEntity.setName(resultSet.getString("name"));

        return categoryEntity;
    }
}
