package com.course.aca.service;

import com.course.aca.dao.CourseDao;
import com.course.aca.dao.FileInfoDao;
import com.course.aca.enums.Status;
import com.course.aca.exception.CourseAccessDeniedException;
import com.course.aca.exception.IllegalParameterException;
import com.course.aca.model.dto.Course;
import com.course.aca.model.dto.CourseCommonInfo;
import com.course.aca.model.dto.FileItem;
import com.course.aca.model.dto.Section;
import com.course.aca.model.entity.CourseCommonInfoEntity;
import com.course.aca.model.entity.CourseEntity;
import com.course.aca.model.entity.FileItemEntity;
import com.course.aca.model.entity.SectionEntity;
import com.course.aca.service.system.DirectoryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides methods associated with courses.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
@Service
public class CourseService {

    /**
     * Bean of class {@link CourseDao }.
     *
     * Initialize using "Autowired" annotation in constructor.
     */
    private CourseDao courseDao;

    /**
     * Bean of class {@link FileInfoDao }.
     *
     * Initialize using "Autowired" annotation in constructor.
     */
    private FileInfoDao fileInfoDao;

    /**
     * Initializes {@link CourseDao}'s bean using "Autowired" annotation.
     * Initializes {@link FileInfoDao}'s bean using "Autowired" annotation.
     *
     * @param courseDao Required by "Autowired" annotation.
     * @param fileInfoDao Required by "Autowired" annotation.
     */
    @Autowired
    public CourseService(CourseDao courseDao, FileInfoDao fileInfoDao) {
        this.courseDao = courseDao;
        this.fileInfoDao = fileInfoDao;
    }

    /**
     * Converts {@link List<CourseEntity>} entities to {@link List<Course>} DTOs.
     *
     * @param entities Entities that will be converted.
     *
     * @return {@link List<Course>} by default. Returns null if passed argument entities is also null.
     */
    static List<Course> parseToCourseList(List<CourseEntity> entities) {
        if (entities == null)
            return null;

        return entities
                .stream().map(e -> new Course(e.getId(),
                                e.getName(),
                                e.getRating(),
                                e.getReviewCount(),
                                e.getTeacherName(),
                                e.getPrice()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Converts {@link CourseCommonInfoEntity} entity to {@link CourseCommonInfo} DTO.
     *
     * @param entity Entity that will be converted.
     *
     * @return {@link CourseCommonInfo} by default. Returns null if passed argument entity is also null.
     */
    private CourseCommonInfo parseToCourseCommonInfo(CourseCommonInfoEntity entity) {

        if (entity == null)
            return null;

        CourseCommonInfo course = new CourseCommonInfo();

        course.setId(entity.getId());
        course.setName(entity.getName());
        course.setDescription(entity.getDescription());
        course.setTeacherId(entity.getTeacherId());
        course.setTeacherName(entity.getTeacherName());
        course.setReviewCount(entity.getReviewCount());
        course.setRating(entity.getRating());
        course.setTotalMaterials(entity.getTotalMaterials());
        course.setTotalHours(entity.getTotalHours());
        course.setPrice(entity.getPrice());

        return course;
    }

    /**
     * Public method. Gets courses by their subcategoryId from DataBase in "packs". One "pack"'s capacity is 10.
     * Gets data from DataBase using {@link CourseService#courseDao} bean.
     *
     * @param subCategoryId Subcategory ID which is necessary to find required courses.
     * @param pageIndex Pack's index number. Starting from 1.
     *
     * @return {@link List<Course>} - The result.
     */
    public List<Course> getCoursesBySubcategory(int subCategoryId, int pageIndex){

        return parseToCourseList(courseDao.getCoursesBySubcategory(subCategoryId, pageIndex));
    }

    /**
     * Gets {@link CourseCommonInfoEntity} from DataBase, converts it into {@link CourseCommonInfo} and returns it.
     *
     * @param courseId Wanted course's id in DataBase.
     *
     * @return {@link CourseCommonInfo}, converted using
     * {@link CourseService#parseToCourseCommonInfo(CourseCommonInfoEntity)} method
     *
     * @throws IllegalParameterException Thrown when requested course isn't approved yet.
     */
    public CourseCommonInfo getCourse(int courseId) throws IllegalParameterException {

        if (courseDao.getCourseStatus(courseId) != Status.APPROVED.getStatusId())
            throw new IllegalArgumentException();

        CourseCommonInfoEntity entity = courseDao.getCourseCommonInfo(courseId);

        return parseToCourseCommonInfo(entity);
    }

    /**
     * Gets Category name using it's categoryId.
     *
     * @param categoryId Category id that is used to find category's name.
     *
     * @return {@link String} Wanted Category name.
     *
     * @throws IllegalParameterException Chained up from {@link CourseDao#getCategoryName(int)}
     */
    public String getCategoryName(int categoryId) throws IllegalParameterException {
        return courseDao.getCategoryName(categoryId);
    }

    /**
     * Gets subcategory name using it's subcategoryId.
     *
     * @param subcategoryId subcategory id that is used to find category's name.
     *
     * @return {@link String} Wanted subcategory name.
     *
     * @throws IllegalParameterException Chained up from {@link CourseDao#getSubcategoryName(int)}
     */
    public String getSubcategoryName(int subcategoryId) throws IllegalParameterException {
        return courseDao.getSubcategoryName(subcategoryId);
    }

    /**
     * Public method. Gets Category id using subcategory id passed to method.
     *
     * @param subcategoryId Needed in category id detection using {@link CourseDao#getCategoryId(int)}.
     *
     * @return {@link Integer} Required category's id.
     *
     * @throws IllegalParameterException Chained up from {@link CourseDao#getCategoryId(int)}
     */
    public int getCategoryIdBySubcategory(int subcategoryId) throws IllegalParameterException {
        return courseDao.getCategoryId(subcategoryId);
    }

    /**
     * Generates local file's direct and full path and returns it.
     *
     * @param itemId Wanted File's id.
     *
     * @return {@link String} Generated file's path.
     *
     * @throws IllegalParameterException Chained up from {@link CourseDao}
     * @throws CourseAccessDeniedException Thrown when trying to get not approved course's item's path or if
     * that item is not public.
     */
    public String getFilePath(int itemId) throws IllegalParameterException, CourseAccessDeniedException {

        int courseId = courseDao.getCourseIdByItem(itemId);

        String path;

        if (courseDao.getCourseStatus(courseId) != Status.APPROVED.getStatusId()) {
            throw new CourseAccessDeniedException();
        }

        path = DirectoryHandler.root + File.separator + courseDao.getFilePath(itemId, courseId);

        if (courseDao.isPublicItem(itemId))
            return path;

        throw new CourseAccessDeniedException();
    }

    /**
     * Public method. Gets courses by teacherId which published that course from DataBase in "packs".
     * One "pack"'s capacity is 10. Gets data from DataBase using {@link CourseService#courseDao} bean.
     *
     * @param teacherId Teacher ID which is necessary to find required courses.
     * @param pageId  Pack's index number. Starting from 1.
     *
     * @return {@link List<Course>} required courses' list.
     */
    public List<Course> getCoursesByTeacherId(int teacherId,int pageId){

        return parseToCourseList(courseDao.getCoursesByTeacher(teacherId,pageId,1));
    }

    /**
     * Public method. Gets most popular courses from DataBase in "packs".
     * One "pack"'s capacity is 10. Gets data from DataBase using {@link CourseService#courseDao} bean.
     *
     * @param pageIndex Pack's index number. Starting from 1.
     *
     * @return {@link List<Course>} required courses' list.
     */
    public List<Course> getPopularCourses(int pageIndex){

        return parseToCourseList(courseDao.getPopularCourses(pageIndex));
    }

    /**
     * Public method. Gets list of {@link Section}s using courseId which they belong to, regardless to
     * that courses' statuses.
     *
     * @param courseId Course ID which is necessary to find required sections.
     *
     * @return {@link List<Section>} if the required data is found. Otherwise returns null.
     *
     * @throws IllegalParameterException //todo why is this thrown?))
     */
    public List<Section> getSectionsNotConsiderStatus(int courseId) throws IllegalParameterException {

        List<SectionEntity> list = fileInfoDao.getSections(courseId);

        if (list.isEmpty())
            return null;

        return list.stream()
                .map(e -> new Section(e.getId(), e.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Public method. Gets all file's information from {@link FileInfoDao} using sectionId and
     * returns it wrapped into {@Link list<FileItem>}.
     *
     * @param sectionId Section's ID which file's information is required.
     *
     * @return {@Link list<FileItem>} which contains required file's information, if there are files in that section,
     * otherwise returns null.
     *
     * @throws IllegalParameterException //todo why is this thrown?))
     */
    public List<FileItem> getSectionAllItems(int sectionId) throws IllegalParameterException{

        List<FileItemEntity> entities = fileInfoDao.getSectionItems(sectionId);

        if (entities.isEmpty())
            return null;

        return entities.stream()
                .map(e -> new FileItem(e.getId(), e.getName(), e.isPublic()))
                .collect(Collectors.toList());

    }

    /**
     * Public method. Gets "course-relative" information using courseId regardless to required course's status.
     *
     * @param courseId Course's ID which is required.
     *
     * @return {@link CourseCommonInfo} which contains "course-relative" information.
     *
     * @throws IllegalParameterException //todo why is this thrown?))
     */
    public CourseCommonInfo getCourseExcludeStatus(int courseId) throws IllegalParameterException {

        CourseCommonInfoEntity entity = courseDao.getCourseCommonInfo(courseId);

        return parseToCourseCommonInfo(entity);
    }

}
