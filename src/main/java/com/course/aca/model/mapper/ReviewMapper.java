package com.course.aca.model.mapper;

import com.course.aca.model.entity.ReviewEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Spring JDBC Mapper class which maps data from DataBase to {@link ReviewEntity} Entity.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class ReviewMapper implements RowMapper<ReviewEntity>{
    @Override
    public ReviewEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        ReviewEntity entity = new ReviewEntity();

        entity.setRating(rs.getInt("rating"));
        entity.setReview(rs.getString("review"));

        return entity;
    }
}
