package com.course.aca.model.mapper;

import com.course.aca.model.entity.SubCategoryEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 * Spring JDBC Mapper class which maps data from DataBase to {@link SubCategoryEntity} Entity.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class SubCategoryMapper implements RowMapper<SubCategoryEntity>{

    @Override
    public SubCategoryEntity mapRow(ResultSet resultSet, int i) throws SQLException {
        SubCategoryEntity subCategoryEntity = new SubCategoryEntity();

        subCategoryEntity.setId(resultSet.getInt("id"));
        subCategoryEntity.setCategoryId(resultSet.getInt("categoryId"));
        subCategoryEntity.setName(resultSet.getString("name"));

        return subCategoryEntity;
    }
}
