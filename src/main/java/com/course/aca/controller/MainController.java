package com.course.aca.controller;

import com.course.aca.exception.CourseAccessDeniedException;
import com.course.aca.exception.IllegalParameterException;
import com.course.aca.model.dto.*;
import com.course.aca.service.CourseService;
import com.course.aca.service.MainService;
import com.course.aca.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

/**
 * A controller class. Provides endpoints for index.html page.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
@RestController
@RequestMapping("/")
@RequestScope
public class MainController implements FileDownloader {

    /**
     * Service classes' beans.
     *
     * Initialized using "Autowired" annotation in constructor.
     */
    private final MainService mainService;
    private final CourseService courseService;
    private final StudentService studentService;

    /**
     * Initializes autowired beans in this class.
     *
     * @param mainService
     * @param courseService
     * @param studentService
     */
    @Autowired
    public MainController(MainService mainService,
                          CourseService courseService,
                          StudentService studentService) {
        this.mainService = mainService;
        this.courseService = courseService;
        this.studentService = studentService;
    }

    /**
     * Endpoint of /getCategoryInfo URL.
     * Gets category information.
     *
     * @return {@link List<CategoryInformation>} wrapped into {@link ResponseEntity} with HttpStatus code
     * "OK" if category information has been get without any issues. Otherwise return {@link ResponseEntity}
     * with HttpStatus code "INTERNAL_SERVER_ERROR".
     */
    @RequestMapping(value = "/getCategoryInfo", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getCategoryInfo() {

        try {
            return ResponseEntity.ok(mainService.getCategoriesInfo());
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Exception in Response.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint of /getPopularCourses URL.
     * Gets most popular courses list.
     *
     * @param pageIndex Used in MySQL query's offset.
     *
     * @return {@link List<Course>} wrapped into {@link ResponseEntity} with HttpStatus code "OK". If there
     * were any issues during method execution, returns {@link ResponseEntity} with HttpStatus code
     * "INTERNAL_SERVER_ERROR".
     */
    @RequestMapping(value = "/getPopularCourses", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getPopularCourses(@RequestParam("pageIndex") int pageIndex) {

        if (pageIndex <= 0) {
            return new ResponseEntity<>("No Content", HttpStatus.NO_CONTENT);
        }

        try {
            return ResponseEntity.ok(courseService.getPopularCourses(pageIndex));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Exception in Response.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // TODO: Anna if subCategoryId is incorrect return []

    /**
     * Endpoint of /getCoursesBySubcategoryId URL.
     * Gets all courses information which belong to specified subcategory.
     *
     * @param pageIndex Used in MySQL query's offset.
     * @param subCategoryId Identifies which subcategory's courses are needed.
     *
     * @return {@link List<Course>} wrapped into {@link ResponseEntity} with HttpStatus code "OK". If there
     * were any issues during method execution, returns {@link ResponseEntity} with HttpStatus code
     * "INTERNAL_SERVER_ERROR".
     */
    @RequestMapping(value = "/getCoursesBySubcategoryId", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getCoursesBySubcategory(@RequestParam("pageIndex") int pageIndex,
                                                                @RequestParam("subCategoryId") int subCategoryId) {

        if (pageIndex <= 0) {
            return new ResponseEntity<>("No Content", HttpStatus.NO_CONTENT);
        }

        try {
            return ResponseEntity.ok(courseService.getCoursesBySubcategory(subCategoryId, pageIndex));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Exception in Response.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint of /getCoursesByTeacherId URL.
     * Gets all courses' main information which are publisher by specified teacher.
     *
     * @param teacherId Identifies which teachers courses are needed.
     * @param pageIndex Used in MySQL query's offset.
     *
     * @return {@link List<Course>} wrapped into {@link ResponseEntity} with HttpStatus code "OK". If there
     * were any issues during method execution, returns {@link ResponseEntity} with HttpStatus code
     * "INTERNAL_SERVER_ERROR".
     */
    @RequestMapping(value = "/getCoursesByTeacherId", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getCoursesByTeacher(@RequestParam("teacherId") int teacherId,
                                              @RequestParam("pageIndex") int pageIndex) {

        try {
            return ResponseEntity.ok(courseService.getCoursesByTeacherId(teacherId, pageIndex));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Exception in Response.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint of /download/{itemId} URL.
     * Starts specified item's download stream.
     *
     * @param itemId Identifies which item's download stream must be started.
     *
     * @return {@link InputStreamResource} wrapped into {@link ResponseEntity} with "OK" HttpStatus if file exists.
     * Otherwise returns {@link ResponseEntity} with "BAD_REQUEST" HttpStatus.
     */
    @RequestMapping(value = "/download/{itemId}", method = RequestMethod.GET)
    public ResponseEntity downloadFile(@PathVariable("itemId") int itemId) {
        String path;
        try {
            path = courseService.getFilePath(itemId);
        } catch (IllegalParameterException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (CourseAccessDeniedException e) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        return downloadFile(path);
    }

    /**
     * Endpoint of /courseInfo/{courseId} URL.
     * Gets specified course's information.
     *
     * @param courseId Identifies which course's full information is needed.
     *
     * @return Specified course's full information wrapped into {@link ResponseEntity} with HttpStatus code "OK" if
     * the information is found successfully, otherwise returns {@link ResponseEntity} with HttpStatus code "BAD_REQUEST".
     */
    @RequestMapping(value = "/courseInfo/{courseId}", method = RequestMethod.GET)
    public ResponseEntity getCourseInfo(@PathVariable("courseId") int courseId) {
        try {
            CourseCommonInfo course = courseService.getCourse(courseId);
            return new ResponseEntity<>(course, HttpStatus.OK);
        } catch (IllegalParameterException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint of /course/sections/{sectionId URL.
     * Gets all items' information which belong to specified section.
     *
     * @param sectionId Identifies which section's items are needed.
     *
     * @return Requested items' information wrapped into {@link ResponseEntity} with HttpStatus code "OK" if the
     * data has been get successfully, or {@link ResponseEntity} with HttpStatus code "FORBIDDEN" when trying to access
     * to not approved course's items. Also can return {@link ResponseEntity} with HttpStatus code "NO_CONTENT" if there
     * are no items in specified course.
     */
    @RequestMapping(value = "/course/sections/{sectionId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getSectionItems(@PathVariable("sectionId") int sectionId) {
        ResponseEntity entity = new ResponseEntity<>("Illegal request parameters", HttpStatus.BAD_REQUEST);

        if (sectionId < 1)
            return entity;

        List<FileItem> list = null;
        try {
            list = studentService.getSectionItems(sectionId);
        } catch (IllegalParameterException e) {
            return entity;
        } catch (CourseAccessDeniedException e) {
            return new ResponseEntity<>("access denied", HttpStatus.FORBIDDEN);
        }

        if (list == null)
            return new ResponseEntity<>("Not such section", HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * Endpoint of /sections/{courseId} URL.
     * Gets all sections' information which belong to specified course.
     *
     * @param courseId Identifies which course's sections are needed.
     *
     * @return Requested sections' information wrapped into {@link ResponseEntity} with HttpStatus code "OK" if the
     * content is found. Otherwise returns {@link ResponseEntity} with HttpStatus code "NO_CONTENT".
     */
    @RequestMapping(value = "/sections/{courseId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getSections(@PathVariable("courseId") int courseId) {

        ResponseEntity entity = new ResponseEntity<>("Illegal request parameters", HttpStatus.BAD_REQUEST);

        if (courseId < 1)
            return entity;

        List<Section> sections = null;
        try {
            sections = studentService.getSections(courseId);
        } catch (IllegalParameterException e) {
            return entity;
        }

        if (sections == null)
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(sections, HttpStatus.OK);
    }

}
