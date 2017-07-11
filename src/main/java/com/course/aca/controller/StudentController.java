package com.course.aca.controller;

import com.course.aca.annotation.Secure;
import com.course.aca.enums.Role;
import com.course.aca.exception.CourseAccessDeniedException;
import com.course.aca.exception.DuplicateRowException;
import com.course.aca.exception.IllegalParameterException;
import com.course.aca.exception.UnpaidCourseException;
import com.course.aca.model.dto.Course;
import com.course.aca.model.dto.Review;
import com.course.aca.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A controller class. Contains endpoints for /student/* URLs.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
@RestController
@RequestMapping("/student")
public class StudentController implements FileDownloader {

    /**
     * Service classes' beans.
     *
     * Initialized using "Autowired" annotation in constructor.
     */
    private StudentService studentService;

    /**
     * Initializes autowired beans in this class.
     *
     * @param studentService
     */
    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Secured endpoint of /courses/paid URL.
     * Gets all purchased courses' which belong to specified student.
     *
     * @param token Student's security token. Identifies which student's purchased courses' information is needed.
     *
     * @return {@link List<Course>} wrapped into {@link ResponseEntity} with HttpStatus code "OK" if information
     * has been found successfully. Otherwise returns {@link ResponseEntity} with HttpStatus code "NO_CONTENT".
     */
    @Secure(role = Role.STUDENT)
    @RequestMapping(value = "/courses/paid", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getBoughtCourses(@RequestHeader("Authorization") String token) {
        List<Course> courses = studentService.getBoughtCourses(token);

        return courses == null ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(courses, HttpStatus.OK);
    }

    /**
     * Secured endpoint of /courses/notPaid URL.
     * Gets all not purchased courses' which belong to specified student.
     *
     * @param token Student's security token. Identifies which student's not purchased courses' information is needed.
     *
     * @return {@link List<Course>} wrapped into {@link ResponseEntity} with HttpStatus code "OK" if information
     * has been found successfully. Otherwise returns {@link ResponseEntity} with HttpStatus code "NO_CONTENT".
     */
    @Secure(role = Role.STUDENT)
    @RequestMapping(value = "/courses/notPaid", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getNotPaidCourses(@RequestHeader("Authorization") String token) {
        List<Course> courses = studentService.getNotPaidCourses(token);

        return courses == null ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(courses, HttpStatus.OK);
    }

    /**
     * Secured endpoint of /buyCourse/{courseId} URL.
     * Provides course purchase functionality.
     *
     * @param token Student's security token. Identifies which student is going to buy the specified course.
     * @param courseId Identifies which course will be purchased.
     *
     * @return {@link ResponseEntity} with HttpStatus code "OK" if request has been successfully sent,
     * {@link ResponseEntity} with HttpStatus code "BAD_REQUEST" if data sent with request has invalid format,
     * {@link ResponseEntity} with HttpStatus code "ALREADY_REPORTED" if the request for specified course already exists.
     */
    @Secure(role = Role.STUDENT)
    @RequestMapping(value = "/buyCourse/{courseId}", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity buyCourse(@RequestHeader("Authorization") String token,
                                    @PathVariable("courseId") int courseId) {

        if (courseId < 1)
            return new ResponseEntity<>("Illegal request parameters.", HttpStatus.BAD_REQUEST);

        try {
            studentService.buyCourse(token, courseId);
        } catch (IllegalParameterException e) {
            return new ResponseEntity<>("Illegal request parameters.", HttpStatus.BAD_REQUEST);
        } catch (DuplicateRowException e) {
            return new ResponseEntity<>("Already requested.", HttpStatus.ALREADY_REPORTED);
        }
        return new ResponseEntity<>("Request is sent.", HttpStatus.OK);
    }

    /**
     * Secured endpoint of /addReview/{courseId} URL.
     * Adds user's review to specified course.
     *
     * @param token User's security token, identifies which user is going to add review.
     * @param courseId Identifies to which course the review is must be mapped.
     * @param review The review that will be mapped to specified course.
     *
     * @return {@link ResponseEntity} with HttpStatus code "OK" if review has been successfully added,
     * {@link ResponseEntity} with HttpStatus code "BAD_REQUEST" if data sent with request has invalid format,
     * {@link ResponseEntity} with HttpStatus code "FORBIDDEN" if the course isn't bought by specified user.
     */
    @Secure(role = Role.STUDENT)
    @RequestMapping(value = "/addReview/{courseId}", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity addReview(@RequestHeader("Authorization") String token,
                                    @PathVariable("courseId") int courseId,
                                    @RequestParam("review") String review) {

        if (courseId < 1)
            return new ResponseEntity<>("Illegal request parameters", HttpStatus.BAD_REQUEST);

        try {
            studentService.addReview(token, courseId, review);
        } catch (IllegalParameterException e) {
            return new ResponseEntity<>("Illegal request parameters", HttpStatus.BAD_REQUEST);
        } catch (UnpaidCourseException e) {
            return new ResponseEntity<>("course isn't bought", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("review is added", HttpStatus.OK);
    }

    /**
     * Secured endpoint of /addRating/{courseId} URL.
     * Adds user's rating to specified course.
     *
     * @param token User's security token, identifies which user is going to rate a course.
     * @param courseId Identifies to which course the rating must be mapped.
     * @param rating The rating that will be mapped to specified course.
     *
     * @return {@link ResponseEntity} with HttpStatus code "OK" if rating has been successfully added,
     * {@link ResponseEntity} with HttpStatus code "BAD_REQUEST" if data sent with request has invalid format,
     * {@link ResponseEntity} with HttpStatus code "FORBIDDEN" if the course isn't bought by specified user.
     */
    @Secure(role = Role.STUDENT)
    @RequestMapping(value = "/addRating/{courseId}", method = RequestMethod.POST)
    public ResponseEntity addRating(@RequestHeader("Authorization") String token,
                                    @PathVariable("courseId") int courseId,
                                    @RequestParam("rating") int rating) {

        if (courseId < 1 || rating < 1 || rating > 5)
            return new ResponseEntity<>("Illegal request parameters", HttpStatus.BAD_REQUEST);

        try {
            studentService.addRating(token, courseId, rating);
        } catch (IllegalParameterException e) {
            return new ResponseEntity<>("Illegal request parameters", HttpStatus.BAD_REQUEST);
        } catch (UnpaidCourseException e) {
            e.printStackTrace();
            return new ResponseEntity<>("course isn't bought", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>("rating is added", HttpStatus.OK);
    }

    /**
     * Secured endpoint of /showReview/{courseId} URL.
     * Gets the specified course's review which has been added by identified by security token student.
     *
     * @param token Student's Security token. Identifies which student is requesting for review.
     * @param courseId Identifies which course's review must be get.
     *
     * @return Requested {@link Review} wrapped into {@link ResponseEntity} with HttpStatus code "OK" if the data has
     * been found successfully, {@link ResponseEntity} with HttpStatus code "BAD_REQUEST" if data sent with request has
     * invalid format, {@link ResponseEntity} with HttpStatus code "FORBIDDEN" if the course isn't bought by specified user.
     */
    @Secure(role = Role.STUDENT)
    @RequestMapping(value = "/showReview/{courseId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity showCourseReview(@RequestHeader("Authorization") String token,
                                           @PathVariable("courseId") int courseId) {

        if (courseId < 1)
            return new ResponseEntity<>("Illegal request parameters", HttpStatus.BAD_REQUEST);

        Review review;

        try {

            review = studentService.showStudentReview(token, courseId);
        } catch (IllegalParameterException e) {
            return new ResponseEntity<>("Illegal request parameters", HttpStatus.BAD_REQUEST);
        } catch (UnpaidCourseException e) {
            return new ResponseEntity<>("course isn't bought", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(review, HttpStatus.OK);
    }

    /**
     * Secured endpoint of /download/{itemId} URL.
     * Starts specified item's download stream.
     *
     * @param token Identifies which user is trying to start download stream.
     * @param itemId Identifies which item's download stream must be started.
     *
     * @return {@link InputStreamResource} wrapped into {@link ResponseEntity} with "OK" HttpStatus if file exists.
     * Otherwise returns {@link ResponseEntity} with "BAD_REQUEST" HttpStatus.
     */
    @Secure(role = Role.STUDENT)
    @RequestMapping(value = "/download/{itemId}", method = RequestMethod.GET)
    public ResponseEntity download(@RequestHeader("Authorization") String token,
                                   @PathVariable("itemId") int itemId) {

        String path;
        try {
            path = studentService.getFilePath(token, itemId);
        } catch (IllegalParameterException e) {
            return new ResponseEntity<>("Illegal request parameter", HttpStatus.BAD_REQUEST);
        } catch (CourseAccessDeniedException e) {
            return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
        }
        return downloadFile(path);
    }

}
