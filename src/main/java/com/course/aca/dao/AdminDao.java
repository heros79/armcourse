package com.course.aca.dao;

import com.course.aca.model.entity.AdminCommentEntity;
import com.course.aca.model.entity.CourseEntity;
import com.course.aca.model.mapper.AdminCommentMapper;
import com.course.aca.model.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
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
public class AdminDao {

    /**
     * Instance of Spring JDBC framework's @{@link JdbcTemplate} class.
     *
     * It's initialized in public constructor.
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * The Mapper bean that is used in DB queries.
     */
    private final CourseMapper courseMapper;

    /**
     * Initializes autowired beans in this class.
     *
     * @param jdbcTemplate
     */
    @Autowired
    public AdminDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.courseMapper = new CourseMapper();
    }

    /**
     * Deletes course's comment from DataBase.
     *
     * @param commentId Comment's id, which will be deleted.
     *
     * @return Affected row's number from DataBase.
     */
    public int deleteCourseComment(int commentId) {

        final String query = "DELETE FROM admin_comment WHERE admin_comment.id = ?";

        return jdbcTemplate.update(query, commentId);
    }

    /**
     * Gets Disapproved Courses' data from DataBase.
     *
     * @param pageId Used in offset.
     *
     * @return {@link List<CourseEntity>} Required data from DataBase.
     */
    public List<CourseEntity> getDisapprovedCourses(int pageId) {
        String query = "SELECT course.id, course.name, course.rating, course.reviewCount,course.price, " +
                "concat(user.firstName,' ',user.lastName) as teacherName " +
                "FROM course, user WHERE  course.status = 2 and course.teacherId = user.id LIMIT 5 OFFSET ? ";
        int offset =  (pageId-1)*5;
        List<CourseEntity> list = jdbcTemplate.query(query,courseMapper, offset);
        return list;
    }

    /**
     * Gets Pending Courses' data from DataBase.
     *
     * @param pageId Used in offset.
     *
     * @return {@link List<CourseEntity>} Required data from DataBase.
     */
    public List<CourseEntity> getPendingCourses(int pageId) {
        String query = "SELECT course.id, course.name, course.rating, course.reviewCount,course.price, " +
                " concat(user.firstName,' ',user.lastName) as teacherName " +
                " FROM course, user WHERE  course.status = 3 and course.teacherId = user.id LIMIT 5 OFFSET ? ";
        int offset =  (pageId-1)*5;
        List<CourseEntity> list = jdbcTemplate.query(query,courseMapper, offset);
        return list;
    }

    /**
     * Updates Course's status in DataBase.
     *
     * @param courseId Identifies which course's status must be updated.
     * @param status The new status's value.
     *
     * @return Affected row's number from DataBase.
     */
    public int updateCourseStatus(int courseId, int status) {
        final String query = "UPDATE course SET status = ? WHERE id = ?;";
        return jdbcTemplate.update(query, status, courseId);
    }

    /**
     * Sets Course's comment in DataBase.
     *
     * @param courseId Identifies which course's comment must be set.
     * @param comment The comment that will be set.
     *
     * @return Affected row's number from DataBase.
     */
    public int setCourseComment(int courseId, String comment) {
        final String query = "INSERT INTO admin_comment(courseId, comment) VALUES( ?, ?);";
        int row = jdbcTemplate.update(query, courseId, comment);

        return row;
    }

    /**
     * Gets Admin's comments from DataBase.
     *
     * @param courseId Used in identifying which course's comments are needed.
     *
     * @return {@link List<AdminCommentEntity>} Required data from DataBase.
     */
    public List<AdminCommentEntity> getCourseComments(int courseId) {
        final String query = "SELECT admin_comment.id, admin_comment.courseId, admin_comment.comment " +
                " FROM admin_comment WHERE courseId = ? ";

        List<AdminCommentEntity> comments = jdbcTemplate.query(query, new AdminCommentMapper(), courseId);
        return comments;
    }

    /**
     * Add's new Category's name into DataBase.
     *
     * @param category The new Category name.
     */
    public void createCategory(String category) {
        final String query = " INSERT INTO category (name) VALUES (?) ";
        jdbcTemplate.update(query, category);
    }

    /**
     * Add's new subcategory to DataBase.
     *
     * @param categoryId Identifies to which category the new subcategory will belong.
     * @param subCategoryName Subcategory name that will be newly added.
     */
    public void createSubcategory(int categoryId, String subCategoryName) {
        final String query = "INSERT INTO subCategory (name, categoryId) VALUES (?, ?) ";
        jdbcTemplate.update(query, subCategoryName, categoryId);
    }
}
