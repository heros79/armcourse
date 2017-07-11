package com.course.aca.controller;

import com.course.aca.exception.IllegalParameterException;
import com.course.aca.model.dto.CourseCommonInfo;
import com.course.aca.model.dto.FileItem;
import com.course.aca.model.dto.Section;
import com.course.aca.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Provides by default implemented methods that are frequently used in controllers layer.
 *
 * Implements {@link FileDownloader} interface with already implemented functionality.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public abstract class AbstractCourseController implements FileDownloader {

    /**
     * Bean of class {@link CourseService}.
     *
     * Initialize using "Autowired" annotation in constructor.
     */
    private CourseService courseService;

    /**
     * Initializes {@link CourseService}'s bean using "Autowired" annotation.
     *
     * @param courseService Required by "Autowired" annotation.
     */
    @Autowired
    public AbstractCourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     *
     * @param authToken Authentication token, stored in request header.
     * @param courseId Needed {@link CourseCommonInfo}'s id. Will be passed to
     * {@link CourseService#getCourseExcludeStatus(int)}
     *
     * @return {@link CourseCommonInfo}'s object found by his courseId wrapped into {@link ResponseEntity} if exists,
     * otherwise returns new {@link ResponseEntity} with "BAD_REQUEST" HttpStatus code.
     */
    public ResponseEntity getCourse(String authToken, int courseId) {

        if (courseId < 1)
            return new ResponseEntity<>("Illegal request parameters", HttpStatus.BAD_REQUEST);

        CourseCommonInfo course;
        try {
            course = courseService.getCourseExcludeStatus(courseId);
        } catch (IllegalParameterException e) {
            e.printStackTrace();
            return new ResponseEntity<>(" ", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(course);
    }

    /**
     *
     * @param authToken Authentication token, stored in request header.
     * @param courseId Needed {@link List<Section>}'s id. Will be passed to
     * {@link CourseService#getSectionsNotConsiderStatus(int)}
     *
     * @return {@link List<Section>}'s object found by their courseId wrapped into {@link ResponseEntity} if exists,
     * otherwise returns new {@link ResponseEntity} with "BAD_REQUEST" HttpStatus code.
     */
    public ResponseEntity getSections(String authToken, int courseId) {

        if (courseId < 1)
            return new ResponseEntity<>("Illegal request parameters", HttpStatus.BAD_REQUEST);

        List<Section> sections;
        try {
            sections = courseService.getSectionsNotConsiderStatus(courseId);
        } catch (IllegalParameterException e) {
            return new ResponseEntity<>("Illegal request parameters", HttpStatus.BAD_REQUEST);
        }

        if (sections == null)
            return ResponseEntity.noContent().build();

        return new ResponseEntity<>(sections, HttpStatus.OK);
    }

    /**
     *
     * @param token Authentication token, stored in request header.
     * @param courseId Used in identifying which course belongs to specific teacher. Useless for custom users.
     * @param sectionId Used in identifying which {@link Section}'s {@link FileItem}s' list is looked for.
     *
     * @return {@link List<FileItem>} wrapped into {@link ResponseEntity} with HttpStatus code "OK" if content is found,
     *          new {@link ResponseEntity} with HttpStatus code "BAD_REQUEST" if request is damaged or if
     *              {@link IllegalParameterException} is caught,
     *          new {@link ResponseEntity} with HttpStatus code "NO_CONTENT" if there is no content.
     */
    public ResponseEntity getSectionAllItems(String token, int courseId, int sectionId) {

        if (sectionId < 1 || courseId < 1)
            return new ResponseEntity<>("Illegal request parameters", HttpStatus.BAD_REQUEST);

        List<FileItem> list;
        try {
            list = courseService.getSectionAllItems(sectionId);
        } catch (IllegalParameterException e) {
            return new ResponseEntity<>("Illegal request parameters", HttpStatus.BAD_REQUEST);
        }

        if (list == null)
            return new ResponseEntity<>("Not such section", HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(list, HttpStatus.OK);

    }


}

