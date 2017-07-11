package com.course.aca.dao;

import com.course.aca.exception.IllegalParameterException;
import com.course.aca.model.entity.PathComponentsEntity;
import com.course.aca.model.entity.StudentCourseRequestEntity;
import com.course.aca.model.mapper.PathComponentsEntityMapper;
import com.course.aca.model.mapper.StudentCourseRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Blob;
import java.sql.SQLException;
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
public class TeacherDao {

    /**
     * Instance of Spring JDBC framework's @{@link JdbcTemplate} class.
     *
     * It's initialized in public constructor.
     */
    private JdbcTemplate jdbcTemplate;

    /**
     * Initializes autowired beans in this class.
     *
     * @param jdbcTemplate
     */
    @Autowired
    public TeacherDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Transactional method. Adds new Course's data into DataBase.
     *
     * @param subcategoryId Identifies to which subcategory new Course will be added.
     * @param courseName Specifies the new course's name.
     * @param teacherId Identifies which teacher has been published the new course.
     *
     * @return Newly added course's Id in DataBase.
     */
    @Transactional
    public int createCourse(int subcategoryId, String courseName, int teacherId) {

        final String query = "INSERT INTO course (name, teacherId, subCategoryId) VALUES (?, ?, ?);";
        final String queryForCourseInfo = "INSERT INTO course_info (courseId, totalHours, totalMaterials) " +
                " VALUES ((SELECT course.id FROM course WHERE name = ?), 0, 0)";
        int row = jdbcTemplate.update(query, courseName, teacherId, subcategoryId);
        jdbcTemplate.update(queryForCourseInfo, courseName);

        return row;
    }

    /**
     * Gets all Student-Course purchase requests from DataBase.
     *
     * @param teacherId Identifies which teacher's Student-Course requests are needed.
     *
     * @return {@link StudentCourseRequestEntity} Required Student-Course Request entities list.
     */
    public List<StudentCourseRequestEntity> getStudentCourseRequests(int teacherId) {
        final String query = "SELECT student_course_request.id, course.id as courseId, course.name as courseName," +
                "  user.id as studentId, concat(user.firstName, ' ', user.lastName) AS studentName " +
                " FROM user INNER JOIN student_course_request ON user.id = student_course_request.studentId " +
                " INNER JOIN course ON student_course_request.courseId = course.id WHERE course.teacherId = ?";
        return jdbcTemplate.query(query, new StudentCourseRequestMapper(), teacherId);
    }

    /**
     * Transactional method. Changes Student-Course purchase request's status in DataBase to "Approved".
     *
     * @param id Student-Course purchase Request's id.
     */
    @Transactional
    public void approveRequest(int id) {

        final String queryForInsert = "INSERT INTO student_course (studentId, courseId) " +
                " VALUES ( (SELECT studentId FROM student_course_request WHERE student_course_request.id = ? ) , " +
                " (SELECT courseId FROM student_course_request WHERE student_course_request.id = ?));";
        jdbcTemplate.update(queryForInsert, id, id);

        final String queryForDelete = "DELETE FROM student_course_request WHERE student_course_request.id = ?;";
        jdbcTemplate.update(queryForDelete, id);
    }

    /**
     * Gets specified course's path's components from DataBase.
     *
     * @param courseId Identifies which course's path components is needed.
     *
     * @return {@link PathComponentsEntity} Entity that stores course's path's components.
     */
    public PathComponentsEntity getPathComponents(int courseId) {
        final String query = "SELECT category.name, subCategory.name , course.name FROM category, subCategory, course " +
                " WHERE course.subCategoryId=subCategory.id AND course.id=? AND category.id=subCategory.categoryId ";

        return jdbcTemplate.queryForObject(query, new PathComponentsEntityMapper(), courseId);
    }

    /**
     * Transactional method. Add new section information into DataBase.
     *
     * @param courseId Identifies to which course the new section will belong.
     * @param sectionName Specifies the new Section's name.
     *
     * @return Newly created section's id in DataBase.
     */
    @Transactional
    public int createSection(int courseId, String sectionName) {
        final String query = "INSERT INTO section (name, courseId) VALUES (?, ?)";
        return jdbcTemplate.update(query, sectionName, courseId);
    }

    /**
     * Transactional method. Sets specified course's description's new value in DataBase.
     *
     * @param description The new description, that will be set in DataBase.
     * @param courseId Identifies which course's description will be modified.
     */
    @Transactional
    public void setDescription(String description, int courseId){
        byte[] byteData = description.getBytes();
        Blob blobData;
        try {
            blobData = jdbcTemplate.getDataSource().getConnection().createBlob();
            blobData.setBytes(1, byteData);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final String query = "UPDATE course SET description = ? WHERE course.id = ?";
        jdbcTemplate.update(query, description, courseId);
    }

    /**
     * Transactional method. Sets specified course's price's new value in DataBase.
     *
     * @param courseId Identifies which course's price will be modified.
     * @param price The new price, that will be added to course in DataBase.
     */
    @Transactional
    public void setCoursePrice(int courseId, int price) {
        final String query = "UPDATE course SET price = ? WHERE course.id = ?";
        jdbcTemplate.update(query, price, courseId);
    }

    /**
     * Gets course's id, using one of it's section's id.
     *
     * @param sectionId Identifies which section courseId is needed.
     *
     * @return The required Course id.
     *
     * @throws IllegalParameterException Thrown when no data found in DataBase.
     */
    public int getCourseIdBySection(int sectionId) throws IllegalParameterException {
        final String query = "SELECT courseId FROM section WHERE section.id = ?";
        int courseId;
        try {
            courseId = jdbcTemplate.queryForObject(query, Integer.class, sectionId);
        } catch (DataAccessException e) {
            throw new IllegalParameterException();
        }
        return courseId;
    }

    /**
     * Gets section's name from DataBase, using it's sectionId.
     *
     * @param sectionId Identifies which section's name is needed.
     *
     * @return {@link String} The required section's name
     */
    public String getSectionName(int sectionId) {
        final String query = "SELECT name FROM section WHERE section.id = ?";
        return jdbcTemplate.queryForObject(query, String.class, sectionId);
    }

    /**
     * Transactional method. Updates already existing course's duration in DataBase.
     *
     * @param courseId Identifies which course's duration will be modified.
     * @param duration Specifies to which value the old duration will be changed.
     */
    @Transactional
    public void updateCourseDuration(int courseId, long duration) {
        int materialCount = 0;
        if(duration == 0)
            materialCount = 1;
        final String query = "UPDATE course_info SET course_info.totalMaterials = course_info.totalMaterials + ? , " +
                             " course_info.totalHours = course_info.totalHours + ? WHERE course_info.courseId = ?";
        jdbcTemplate.update(query, materialCount, duration, courseId);
    }

    /**
     * Gets teacher's id, whose course is requested to be purchased.
     *
     * @param requestId Identifies which request must be checked.
     *
     * @return The required teaher's id.
     *
     * @throws IllegalParameterException Thrown when no data found in DataBase.
     */
    public int getTeacherIdFromRequest(int requestId) throws IllegalParameterException {
        final String query = "SELECT teacherId FROM course WHERE id = " +
                "  (SELECT courseId FROM student_course_request WHERE id = ?)";
        int teacherId = 0;
        try {
            teacherId = jdbcTemplate.queryForObject(query, Integer.class, requestId);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return teacherId;
    }
}
