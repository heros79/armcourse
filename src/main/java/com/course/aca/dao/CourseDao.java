package com.course.aca.dao;

import com.course.aca.exception.IllegalParameterException;
import com.course.aca.model.entity.CourseCommonInfoEntity;
import com.course.aca.model.entity.CourseEntity;
import com.course.aca.model.mapper.CourseCommonInfoMapper;
import com.course.aca.model.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object. Contains public methods which allow to connect to DataBase, Create, Read, Update or
 * delete data from there.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
@Repository
public class CourseDao {

    /**
     * Instance of Spring JDBC framework's @{@link JdbcTemplate} class.
     *
     * It's initialized in public constructor.
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * The Mapper beans that are used in DB queries.
     */
    private final CourseCommonInfoMapper courseCommonInfoMapper;
    private final CourseMapper courseMapper;

    /**
     * Initializes autowired beans in this class.
     *
     * @param jdbcTemplate
     */
    @Autowired
    public CourseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.courseCommonInfoMapper = new CourseCommonInfoMapper();
        this.courseMapper = new CourseMapper();
    }

    /**
     * Gets Courses from DataBase using subcategory id.
     *
     * @param subcategoryId Identifies which subcategory's courses are required.
     * @param pageId Used in offset.
     *
     * @return {@link List<CourseEntity>} Required list of courses.
     */
    public List<CourseEntity> getCoursesBySubcategory(int subcategoryId, int pageId) {
        final String query = "SELECT course.id, course.name, course.rating, course.reviewCount, course.price, " +
                "  concat(user.firstName,' ',user.lastName) AS teacherName FROM course, user " +
                " WHERE course.status = 1 AND course.subCategoryId = ? AND  user.id=course.teacherId LIMIT 5 OFFSET ?";
        int offset = (pageId - 1) * 5;

        return jdbcTemplate
                .query(query, courseMapper, subcategoryId, offset);
    }

    /**
     * Gets course-related almost all information from DataBase.
     *
     * @param courseId Identifies which course's information is needed.
     *
     * @return {@link CourseCommonInfoEntity}
     */
    public CourseCommonInfoEntity getCourseCommonInfo(int courseId) {

        final String query = "SELECT course.id, course.name, (rf_course_status.status) AS status, course.description," +
                " course.rating, course.reviewCount, course.price, (concat(user.firstName, ' ', user.lastName)) AS " +
                " teacherName, course.teacherId, course.subCategoryId, course_info.totalHours, course_info.totalMaterials" +
                " FROM course, course_info, user, rf_course_status WHERE course.status = rf_course_status.id AND" +
                " course.teacherId = user.id AND course.id = course_info.courseId AND course.id = ?";

        return jdbcTemplate.queryForObject(query, courseCommonInfoMapper, courseId);

    }

    /**
     * Gets Courses from DataBase that belong to specified teacher.
     *
     * @param teacherId Identifies which teacher's courses are needed.
     * @param pageId Used in offset.
     * @param status Specifies witch which status courses are needed.
     *
     * @return {@link List<CourseEntity>} Required list of courses.
     */
    public List<CourseEntity> getCoursesByTeacher(int teacherId, int pageId, int status) {
        String query = "SELECT course.id, course.name, course.rating, course.reviewCount, course.price," +
                " concat(user.firstName,' ',user.lastName) AS teacherName FROM course, user " +
                " WHERE  course.teacherId = ? AND course.teacherId = user.id AND course.status = ? LIMIT 5 OFFSET ? ";

        int offset = (pageId - 1) * 5;

        return jdbcTemplate.query(query, courseMapper, teacherId, status, offset);
    }

    /**
     * Gets specified course's status from DataBase.
     *
     * @param courseId Identifies which course's status is neede.
     *
     * @return The status that's needed.
     *
     * @throws IllegalParameterException Thrown when no data found in DataBase.
     */
    public int getCourseStatus(int courseId) throws IllegalParameterException {
        String query = " SELECT status FROM course WHERE id = ? ";

        int status;
        try {
            status = jdbcTemplate.queryForObject(query, Integer.class, courseId);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            throw new IllegalParameterException();
        }

        return status;
    }

    /**
     * Gets specified category's name from DataBase.
     *
     * @param categoryId Identifies which category's name is needed.
     *
     * @return {@link String} The needed category name.
     *
     * @throws IllegalParameterException Thrown when no data found in DataBase.
     */
    public String getCategoryName(int categoryId) throws IllegalParameterException {

        String query = "SELECT category.name FROM category WHERE category.id = ?;";

        String name;

        try {
            name = jdbcTemplate.queryForObject(query, String.class, categoryId);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new IllegalParameterException();
        }

        return name;
    }

    /**
     * Gets specified subcategory's name from DataBase.
     *
     * @param subcategoryId Identifies which subcategory's name is needed.
     *
     * @return {@link String} The needed subcategory name.
     *
     * @throws IllegalParameterException Thrown when no data found in DataBase.
     */
    public String getSubcategoryName(int subcategoryId) throws IllegalParameterException {

        String query = "SELECT subCategory.name FROM subCategory WHERE subCategory.id = ?";
        String subcategory;
        try {
            subcategory = jdbcTemplate.queryForObject(query, String.class, subcategoryId);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new IllegalParameterException();
        }
        return subcategory;
    }

    /**
     * Gets Category's id from DataBase, using one of it's subcategories' id.
     *
     * @param subcategoryId Identifies which subcategory's categoryId is needed.
     *
     * @return The needed category Id.
     *
     * @throws IllegalParameterException Thrown when no data found in DataBase.
     */
    public int getCategoryId(int subcategoryId) throws IllegalParameterException {

        final String query = "SELECT subCategory.categoryId FROM subCategory WHERE subCategory.id = ? ";

        int id;
        try {
            id = jdbcTemplate.queryForObject(query, Integer.class, subcategoryId);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new IllegalParameterException();
        }
        return id;
    }

    /**
     * Gets Course's id using one of it's sections' id.
     *
     * @param sectionId Identifies which section's courseId is needed.
     *
     * @return The needed course id.
     *
     * @throws IllegalParameterException Thrown when no data found in DataBase.
     */
    public int getCourseId(int sectionId) throws IllegalParameterException {
        String query = "SELECT courseId FROM section WHERE id = ?";
        Integer id;

        try {
            id = jdbcTemplate.queryForObject(query, Integer.class, sectionId);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new IllegalParameterException();
        }

        return id;
    }

    /**
     * Gets Course Id from DataBase using one of it's item's id.
     *
     * @param itemId Identifies which item's Course id is needed.
     *
     * @return The required course Id.
     *
     * @throws IllegalParameterException Thrown when no data found in DataBase.
     */
    public int getCourseIdByItem(int itemId) throws IllegalParameterException {
        String query = " SELECT courseId FROM section WHERE section.id = " +
                " (SELECT sectionId FROM course_item WHERE course_item.id = ?)";
        int id;

        try {
            id = jdbcTemplate.queryForObject(query, Integer.class, itemId);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new IllegalParameterException();
        }
        return id;
    }

    /**
     * Check's in DataBase if specified item is public or not.
     *
     * @param itemId Identifies which item must be checked.
     *
     * @return True if specified item is public. Otherwise returns False.
     *
     * @throws IllegalParameterException Thrown when no data found in DataBase.
     */
    public boolean isPublicItem(int itemId) throws IllegalParameterException {
        String query = " SELECT isPublic FROM course_item WHERE id = ?";
        boolean isPublic;
        try {
            isPublic = jdbcTemplate.queryForObject(query, Boolean.class, itemId);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new IllegalParameterException();
        }
        return isPublic;
    }

    /**
     * Generate Item's path, using information, get from Database.
     *
     * @param itemId Identifies which file's path must be generated.
     * @param courseId Identifies to which course the item below belongs.
     *
     * @return The requested path.
     *
     * @throws IllegalParameterException Thrown when no data found in DataBase.
     */
    public String getFilePath(int itemId, int courseId) throws IllegalParameterException {
        String query1 = "SELECT c.name  AS course, sb.name AS subcategory, ct.name AS category FROM course c " +
                " INNER JOIN subcategory sb ON c.subCategoryId = sb.id " +
                " INNER JOIN category ct ON sb.categoryId = ct.id WHERE c.id = ?";

        String query2 = "SELECT sc.name AS section, rs.resourceName AS resource FROM course_item ci " +
                " INNER JOIN section sc ON ci.sectionId = sc.id " +
                " INNER JOIN resource rs ON ci.id = rs.courseItemId WHERE ci.id = ?";

        String path;

        try {
            Map<String, Object> map1 = jdbcTemplate.queryForMap(query1, courseId);
            Map<String, Object> map2 = jdbcTemplate.queryForMap(query2, itemId);

            path = map1.get("category") + File.separator +
                    map1.get("subcategory") + File.separator +
                    map1.get("course") + File.separator +
                    map2.get("section") + File.separator +
                    map2.get("resource");

            return path;
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new IllegalParameterException();
        }
    }

    /**
     * Gets teacher's id from DataBase, who published the passed course.
     *
     * @param courseId Identifies which course's teacherId is needed.
     *
     * @return The requested teacher Id.
     *
     * @throws IllegalParameterException Thrown when no data found in DataBase.
     */
    public int getCourseTeacherId(int courseId) throws IllegalParameterException {
        final String query = "SELECT teacherId FROM course WHERE course.id = ?";
        Integer teacherId;
        try {
            teacherId = jdbcTemplate.queryForObject(query, Integer.class, courseId);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new IllegalParameterException();
        }
        return teacherId;
    }

    /**
     * Gets popular courses from DataBase.
     *
     * @param pageIndex Used in offset.
     *
     * @return {@link List<CourseEntity>} The most popular courses' list.
     */
    public List<CourseEntity> getPopularCourses(int pageIndex) {

        final String query = "SELECT course.id, course.name, course.rating, course.reviewCount," +
                " course.price, concat(user.firstName,' ',user.lastName) AS teacherName FROM " +
                "(SELECT * FROM course ORDER BY course.rating DESC) AS course, user WHERE" +
                " course.status = 1 AND user.id=course.teacherId ORDER BY course.rating DESC LIMIT 5 OFFSET ?";
        int offset = (pageIndex - 1) * 5;

        return jdbcTemplate.query(query, courseMapper, offset);
    }

    /**
     * Checks if course with specified courseId exists in DataBase or not.
     *
     * @param courseId Identifies which course is need to be checked.
     *
     * @return True if course exists in DataBase. Otherwise returns False.
     */
    public boolean isCourseExist(int courseId) {
        String query = "SELECT name FROM course WHERE id = ?";

        Integer id;
        try {
            id = jdbcTemplate.queryForObject(query, Integer.class, courseId);
            return id == courseId;
        } catch (DataAccessException e) {
            return false;
        }

    }
}
