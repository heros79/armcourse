package com.course.aca.service;

import com.course.aca.dao.AdminDao;
import com.course.aca.dao.CourseDao;
import com.course.aca.enums.Status;
import com.course.aca.exception.CourseAccessDeniedException;
import com.course.aca.exception.CourseNotExistException;
import com.course.aca.exception.IllegalParameterException;
import com.course.aca.model.dto.Course;
import com.course.aca.model.entity.AdminCommentEntity;
import com.course.aca.model.entity.CourseEntity;
import com.course.aca.service.system.DirectoryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides public methods associated with admin page and admin functionality.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
@Service
public class AdminService {

    /**
     * Bean of class {@link AdminDao}.
     *
     * Initialize using "Autowired" annotation in constructor.
     */
    private final AdminDao adminDao;

    /**
     * Bean of class {@link CourseDao}.
     *
     * Initialize using "Autowired" annotation in constructor.
     */
    private final CourseDao courseDao;

    /**
     * Initializes {@link AdminDao}'s bean using "Autowired" annotation.
     * Initializes {@link CourseDao}'s bean using "Autowired" annotation.
     *
     * @param adminDao Required by "Autowired" annotation.
     * @param courseDao Required by "Autowired" annotation.
     */
    @Autowired
    public AdminService(AdminDao adminDao, CourseDao courseDao) {
        this.adminDao = adminDao;
        this.courseDao = courseDao;
    }

    /**
     * Public method returns disapproved courses from DataBase in "packs". One "pack"'s capacity is 10.
     * Gets data from DataBase using {@link AdminService#adminDao} bean.
     *
     * @param pageIndex Pack's index number. Starting from 1.
     *
     * @return {@link List<Course>} converted from {@link List<CourseEntity>}.
     */
    public List<Course> getDisapprovedCourses(int pageIndex) {
        List<Course> courseList;
        List<CourseEntity> courseEntities = adminDao.getDisapprovedCourses(pageIndex);
        courseList = CourseService.parseToCourseList(courseEntities);
        return courseList;
    }

    /**
     * Public method returns pending courses from DataBase in "packs". One "pack"'s capacity is 10.
     * Gets data from DataBase using {@link AdminService#adminDao} bean.
     *
     * @param pageIndex Pack's index number. Starting from 1.
     *
     * @return {@link List<Course>} converted from {@link List<CourseEntity>}.
     */
    public List<Course> getPendingCourses(int pageIndex) {
        List<Course> courseList;
        List<CourseEntity> courseEntities = adminDao.getPendingCourses(pageIndex);
        courseList = CourseService.parseToCourseList(courseEntities);
        return courseList;
    }

    /**
     * Public method. Changes course status in DataBase using {@link AdminService#courseDao} bean.
     *
     * @param courseId Which course's status needs to be changed.
     * @param status Status value, to which course's status is going to change.
     *
     * @return Affected row's id in DataBase.
     *
     * @throws IllegalParameterException Chained up from {@link CourseDao#getCourseStatus(int)}
     * @throws CourseAccessDeniedException Thrown when trying to change already approved course's status.
     */
    public int setCourseStatus(int courseId, int status) throws IllegalParameterException, CourseAccessDeniedException {
        if (courseDao.getCourseStatus(courseId) == Status.APPROVED.getStatusId())
            throw new CourseAccessDeniedException();

        return adminDao.updateCourseStatus(courseId, status);
    }

    /**
     * Public method. Sets course comment in DataBase using {@link AdminService#courseDao} bean.
     *
     * @param courseId To which course needs to be added the comment.
     * @param comment The comment that is going to be added to course.
     *
     * @return Affected row's id in DataBase or 0, if course's status is already approved.
     *
     * @throws IllegalParameterException Chained up from {@link CourseDao#getCourseStatus(int)}
     */
    public int setCourseComment(int courseId, String comment) throws IllegalParameterException {

        int status;
        status = courseDao.getCourseStatus(courseId);
        if (status == Status.APPROVED.getStatusId()) {
            return 0;
        }
        return adminDao.setCourseComment(courseId, comment);
    }

    /**
     * Public method. Gets course's all comments by given courseId from DataBase using
     * {@link AdminDao#getCourseComments(int)}
     *
     * @param courseId Which course's comments are looked for.
     *
     * @return {@link Map<Integer, String>} which contains comment's id(as key) and the comment(as value)
     *
     * @throws CourseNotExistException Thrown when course with given courseId isn't found in DataBase.
     */
    public Map<Integer, String> getCourseComments(int courseId) throws CourseNotExistException {
        if (!courseDao.isCourseExist(courseId))
            throw new CourseNotExistException();

        List<AdminCommentEntity> adminCommentEntities = adminDao.getCourseComments(courseId);
        Map<Integer, String> comments = new HashMap<>();
        for (AdminCommentEntity entity : adminCommentEntities) {
            int k = entity.getId();
            String a = entity.getComment();
            comments.put(k, a);
        }
        return comments;
    }

    /**
     * Public method. Deletes admin comment with specified commentId from DataBase using
     * {@link AdminDao#deleteCourseComment(int)}
     *
     * @param commentId Comment's id, that is going to be deleted.
     *
     * @return Affected row's number in DataBase.
     */
    public int deleteComment(int commentId) {
        return adminDao.deleteCourseComment(commentId);

    }

    /**
     * Public method. Adds new category, which name is stored in categoryName, into DataBase.
     *
     * @param categoryName Name of category that is going to be added into DataBase.
     */
    public void createCategory(String categoryName) {

        adminDao.createCategory(categoryName);
    }

    /**
     * Public method. Adds new subcategory, which name is stored in subCategoryName, into DataBase.
     *
     * @param categoryId To which category our new subcategory is going to belong.
     * @param subCategoryName New subcategory's name.
     */
    public void createSubcategory(int categoryId, String subCategoryName) {
        adminDao.createSubcategory(categoryId, subCategoryName);
    }

    /**
     * Public method. Generates searched file's full path in hard disk.
     *
     * @param itemId Item's id, which path is required to get.
     *
     * @return {@link String} Full direct path to required file in hard disk.
     *
     * @throws IllegalParameterException Chained up by {@link CourseDao#getCourseIdByItem(int)} and
     * {@link CourseDao#getFilePath(int, int)}
     */
    public String getFilePath(int itemId) throws IllegalParameterException {
        int courseId = courseDao.getCourseIdByItem(itemId);

        return DirectoryHandler.root + File.separator + courseDao.getFilePath(itemId, courseId);
    }


}