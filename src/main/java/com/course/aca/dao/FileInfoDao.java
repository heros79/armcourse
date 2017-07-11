package com.course.aca.dao;

import com.course.aca.model.entity.FileEntity;
import com.course.aca.model.entity.FileItemEntity;
import com.course.aca.model.entity.SectionEntity;
import com.course.aca.model.mapper.FileItemMapper;
import com.course.aca.model.mapper.SectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
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
public class FileInfoDao {

    /**
     * Instance of Spring JDBC framework's @{@link JdbcTemplate} class.
     *
     * It's initialized in public constructor.
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * The Mapper bean that is used in DB queries.
     */
    private final SectionMapper sectionMapper;

    /**
     * Initializes autowired beans in this class.
     *
     * @param jdbcTemplate
     */
    @Autowired
    public FileInfoDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.sectionMapper = new SectionMapper();
    }

    /**
     * Transactional method which adds new File's information into DataBase.
     *
     * @param fileEntity Entity, that contains data which will be added to DataBase.
     */
    @Transactional
    public void addFile(FileEntity fileEntity) {

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        final String courseItemQuery = "INSERT INTO course_item (isPublic, sectionId, name) VALUES (?, ?, ?)";
        final String resourceQuery = "INSERT INTO resource (courseItemId, resourceTypeId, resourceName) VALUES " +
                " (?, (SELECT rf_resource_type.id FROM rf_resource_type WHERE rf_resource_type.type = ?), ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(courseItemQuery,
                    Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setBoolean(1, fileEntity.isPublic());
            preparedStatement.setInt(2, fileEntity.getSectionId());
            preparedStatement.setString(3, fileEntity.getName());

            return preparedStatement;
        }, keyHolder);

        jdbcTemplate.update(resourceQuery,
                keyHolder.getKey().intValue(),
                fileEntity.getResourceType(),
                fileEntity.getResourceName());
    }

    /**
     * Gets all Sections' information from DataBase that belong to specified course.
     *
     * @param courseId Identifies which course's sections are needed.
     *
     * @return {@link List<SectionEntity>} The needed sections' list.
     */
    public List<SectionEntity> getSections(int courseId) {
        String query = " SELECT * FROM section WHERE courseId = ?";
        return jdbcTemplate.query(query, sectionMapper, courseId);
    }

    /**
     * Gets all Items from DataBase that belong to specified section.
     *
     * @param sectionId Identifies which section's items are needed.
     *
     * @return {@link List<FileItemEntity>} The needed files' entities list.
     */
    public List<FileItemEntity> getSectionItems(int sectionId) {
        String query = " Select * FROM course_item WHERE sectionId = ? ";

        return jdbcTemplate.query(query, new FileItemMapper(), sectionId);

    }
}
