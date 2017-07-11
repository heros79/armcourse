package com.course.aca.dao;

import com.course.aca.model.entity.CategoryEntity;
import com.course.aca.model.entity.SubCategoryEntity;
import com.course.aca.model.mapper.CategoryMapper;
import com.course.aca.model.mapper.SubCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object. Contains public methods which allow to connect to DataBase, Create, Read, Update or
 * delete data from there.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
@Repository
public class MainDao {

    /**
     * Instance of Spring JDBC framework's @{@link JdbcTemplate} class.
     *
     * It's initialized in public constructor.
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * The instances of mapper beans that are used in DB queries.
      */
    private final CategoryMapper categoryMapper;
    private final SubCategoryMapper subCategoryMapper;


    /**
     * Initializes autowired beans in this class.
     *
     * @param jdbcTemplate
     */
    @Autowired
    public MainDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.categoryMapper = new CategoryMapper();
        this.subCategoryMapper = new SubCategoryMapper();
    }

    /**
     * Gets categories' data from DB using @{@link CategoryMapper}'s object
     * 
     * @return {@link List< CategoryEntity >} list which contains all category info in DB.
     */
    public List<CategoryEntity> getCategories() {
        List<CategoryEntity> categories;

        String query = "SELECT * FROM category";

        categories = jdbcTemplate.query(query, categoryMapper);

        return categories;
    }

    /**
     * Gets subCategories' data from DB using @{@link SubCategoryMapper}'s object
     *
     * @throws // TODO: 5/24/17  
     * @return @{@link List< SubCategoryEntity >} list which contains all subCategory info in DB
     */
    public List<SubCategoryEntity> getSubCategories() {
        List<SubCategoryEntity> subCategories;

        String query = "SELECT * FROM subCategory";

        subCategories = jdbcTemplate.query(query, subCategoryMapper);

        return subCategories;
    }
}
