package com.course.aca.dao;

import com.course.aca.exception.DuplicateRowException;
import com.course.aca.model.entity.CourseEntity;
import com.course.aca.model.entity.ReviewEntity;
import com.course.aca.model.mapper.CourseMapper;
import com.course.aca.model.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
public class StudentDao {

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
    public StudentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Gets specified student's bought courses list from DataBase.
     *
     * @param studentId Identifies which student's purchased courses are needed.
     *
     * @return {@link List<CourseEntity>} The needed bought courses.
     */
    public List<CourseEntity> getBoughtCourses(int studentId) {
        String query = "SELECT  c.id, c.name, c.price, c.reviewCount, c.rating," +
                " concat(t.firstName, ' ', t.lastName) as teacherName" +
                "  FROM course c INNER JOIN user t ON c.teacherId = t.id" +
                "  INNER JOIN student_course sc ON c.id = sc.courseId WHERE sc.studentId = ?";

        return jdbcTemplate.query(query, new CourseMapper(), studentId);
    }

    /**
     * Gets specified student's not bought courses list from DataBase.
     *
     * @param studentId Identifies which student's not purchased courses are needed.
     *
     * @return {@link List<CourseEntity>} The needed not paid courses.
     */
    public List<CourseEntity> getNotPaidCourses(int studentId) {
        String query = "SELECT c.id, c.name, c.price, c.reviewCount," +
                "  c.rating, concat(t.firstName, ' ', t.lastName) as teacherName " +
                " FROM course c INNER JOIN user t ON c.teacherId = t.id" +
                "  INNER JOIN student_course_request sc ON c.id = sc.courseId WHERE sc.studentId = ?";

        return jdbcTemplate.query(query, new CourseMapper(), studentId);
    }

    /**
     * Transactional method. Adds to DataBase information about done purchase.
     *
     * @param studentId Identifies which student purchased the course.
     * @param courseId Identifies which course has been purchased.
     *
     * @throws DuplicateRowException Thrown when trying to purchase already purchased course.
     */
    @Transactional
    public void buyCourse(int studentId, int courseId) throws DuplicateRowException {
        String query = "INSERT INTO student_course_request(studentId, courseId) VALUE (?,?)";

        try {
            jdbcTemplate.update(query, studentId, courseId);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new DuplicateRowException();
        }
    }

    /**
     * Gets Student's review of specified course.
     *
     * @param token Student's security token.
     * @param courseId Identifies which course's review is needed.
     *
     * @return {@link ReviewEntity} Contains student's review details.
     */
    public ReviewEntity showStudentReview(String token, int courseId) {
        String query = "SELECT student_course.rating, student_course.review FROM student_course WHERE " +
                " student_course.studentId = (SELECT userId FROM user_session WHERE token = ?)  AND courseId = ?";

        return jdbcTemplate.queryForObject(query, new ReviewMapper(), token, courseId);
    }

    /**
     * Transactional method. Add student's review to course in DataBase.
     *
     * @param studentId Identifies which student's review will be added.
     * @param courseId Identifies which course's review will be added.
     * @param review The review entity, that will be used to add review to DataBase.
     */
    @Transactional
    public void addReviewToCourse(int studentId, int courseId, String review) {

        String query = "UPDATE  student_course SET review = ? WHERE studentId = ? AND courseId = ?";

        jdbcTemplate.update(query, review, studentId, courseId);
    }

    /**
     * Transactional method. Adds rating to course, rate by specified student.
     *
     * @param studentId Identifies which student rated the course.
     * @param courseId Identifies which course has been rated.
     * @param rating The rating, that will be added to course.
     */
    @Transactional
    public void addRating(int studentId, int courseId, int rating) {

        String query = "UPDATE  student_course SET rating = ? WHERE studentId = ? AND courseId = ?";

        jdbcTemplate.update(query, rating, studentId, courseId);
    }

    /**
     * Checks if student is already bought course or not.
     *
     * @param token Student's security token.
     * @param courseId Identifies which course must be checked.
     *
     * @return True if course is already paid. Otherwise returns False.
     */
    public boolean isPaid(String token, int courseId) {
        String query = "SELECT student_course.id FROM student_course WHERE student_course.studentId = " +
                " (SELECT userId FROM user_session WHERE token = ?) AND student_course.courseId = ? ";

        Integer id;
        try {
            id = jdbcTemplate.queryForObject(query, Integer.class, token, courseId);
            if (id > 0)
                return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
