package com.course.aca.controller;

import com.course.aca.annotation.Secure;
import com.course.aca.enums.Role;
import com.course.aca.exception.CourseAccessDeniedException;
import com.course.aca.exception.CourseNotExistException;
import com.course.aca.exception.IllegalParameterException;
import com.course.aca.model.dto.Course;
import com.course.aca.model.dto.CourseCommonInfo;
import com.course.aca.service.AdminService;
import com.course.aca.service.CourseService;
import com.course.aca.service.system.DirectoryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * A controller class. Contains endpoints for /admin/* URLs.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
@RestController
@RequestMapping("/admin")
public class AdminController extends AbstractCourseController {

    /**
     * Service classes' beans.
     *
     * Initialized using "Autowired" annotation in constructor.
     */
    private final AdminService adminService;
    private final CourseService courseService;

    /**
     * Initializes autowired beans in this class.
     *
     * @param adminService
     * @param courseService
     */
    @Autowired
    public AdminController(AdminService adminService, CourseService courseService) {
        super(courseService);
        this.adminService = adminService;
        this.courseService = courseService;
    }

    /**
     * Endpoint of /disapproved URL.
     * Gets disapproved courses and returns them.
     *
     * @param authToken User's security token. Identifies which user is requesting the data.
     * @param pageIndex Used in MySQL offset.
     *
     * @return JSON data wrapped into {@link ResponseEntity} with HttpStatus code "OK" if everything done
     * successfully, {@link ResponseEntity} with HttpStatus Code "BAD_REQUEST" if passed arguments are
     * inappropriate, or {@link ResponseEntity} with HttpStatus code "NO_CONTENT" if no content is found.
     */
    @Secure(role = Role.ADMIN)
    @RequestMapping(value = "/disapproved", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getAllCourses(@RequestHeader("Authorization") String authToken,
                                                      @RequestParam("pageIndex") int pageIndex) {
        if (pageIndex < 1)
            return new ResponseEntity<>("Illegal request parameters", HttpStatus.BAD_REQUEST);

        List<Course> courseList = adminService.getDisapprovedCourses(pageIndex);
        if (courseList.size() > 0) {
            return ResponseEntity.ok(courseList);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint of /pending URL.
     * Gets disapproved courses and returns them.
     *
     * @param authToken User's security token. Identifies which user is requesting the data.
     * @param pageIndex Used in MySQL offset.
     *
     * @return JSON data wrapped into {@link ResponseEntity} with HttpStatus code "OK" if everything done
     * successfully, {@link ResponseEntity} with HttpStatus Code "BAD_REQUEST" if passed arguments are
     * inappropriate, or {@link ResponseEntity} with HttpStatus code "NO_CONTENT" if no content is found.
     */
    @Secure(role = Role.ADMIN)
    @RequestMapping(value = "/pending", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getPendingCourses(@RequestHeader("Authorization") String authToken,
                                                          @RequestParam("pageIndex") int pageIndex) {
        if (pageIndex < 1)
            return new ResponseEntity<>("Illegal request parameters", HttpStatus.BAD_REQUEST);

        List<Course> courseList = adminService.getPendingCourses(pageIndex);
        if (courseList.size() > 0) {
            return ResponseEntity.ok(courseList);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint of /setStatus/{status} URL.
     * Sets already added courses status to passed value.
     *
     * @param authToken User's security token. Identifies which user is trying to modify the data.
     * @param status Specifies to which value the course's status must be changed.
     * @param courseId Identifies which course's status must be changed.
     *
     * @return JSON data wrapped into {@link ResponseEntity} with HttpStatus code "OK" if everything done
     * successfully, {@link ResponseEntity} with HttpStatus Code "BAD_REQUEST" if passed arguments are
     * inappropriate.
     */
    @Secure(role = Role.ADMIN)
    @RequestMapping(value = "/setStatus/{status}", method = RequestMethod.POST)
    public ResponseEntity setCourseStatus(@RequestHeader("Authorization") String authToken,
                                          @PathVariable("status") int status,
                                          @RequestParam("courseId") int courseId) {
        if (status > 3 || status < 1) {
            return new ResponseEntity<>("Status(value) should be 1 - 3", HttpStatus.BAD_REQUEST);
        }
        int row;
        try {
            row = adminService.setCourseStatus(courseId, status);
        } catch (IllegalParameterException e) {
            return new ResponseEntity<>("Course id is incorrect", HttpStatus.BAD_REQUEST);
        } catch (CourseAccessDeniedException e) {
            return new ResponseEntity<>("Course is approved", HttpStatus.BAD_REQUEST);
        }

        if (row == 0) {
            return new ResponseEntity<>("Course id is incorrect", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Course status has been changed");
    }

    /**
     * Endpoint of /setComment URL.
     * Adds comment to already existing course.
     *
     * @param authToken User's security token. Identifies which user is trying to modify the data.
     * @param courseId Identifies to which course the new comment must be mapped.
     * @param comment The new comments value that will be added to course.
     *
     * @return JSON data wrapped into {@link ResponseEntity} with HttpStatus code "OK" if everything done
     * successfully, {@link ResponseEntity} with HttpStatus Code "BAD_REQUEST" if passed arguments are
     * inappropriate.
     */
    @Secure(role = Role.ADMIN)
    @RequestMapping(value = "/setComment", method = RequestMethod.POST, consumes = "text/plain")
    public ResponseEntity setCourseComment(@RequestHeader("Authorization") String authToken,
                                           @RequestParam("courseId") int courseId,
                                           @RequestBody String comment) {
        int response;
        if (comment == null || comment.length() > 255 || comment.length() == 0) {
            return new ResponseEntity<>("comment shouldn't be null and long then 255", HttpStatus.BAD_REQUEST);
        }
        try {
            response = adminService.setCourseComment(courseId, comment);
        } catch (IllegalParameterException e) {
            return new ResponseEntity<>("IllegalParameterException", HttpStatus.BAD_REQUEST);
        }
        if (response == 0)
            return new ResponseEntity<>("course status is approved", HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok().build();

    }

    /**
     * Endpoint of /getComments URL.
     * Gets all comments belonging to specified course.
     *
     * @param authToken User's security token. Identifies which user is trying to get the data.
     * @param courseId Identifies which course's comments are needed.
     *
     * @return JSON data wrapped into {@link ResponseEntity} with HttpStatus code "OK" if everything done
     * successfully or {@link ResponseEntity} with HttpStatus code "NO_CONTENT" if no content is found.
     */
    @Secure(role = Role.ADMIN)
    @RequestMapping(value = "/getComments", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getCourseComments(@RequestHeader("Authorization") String authToken,
                                            @RequestParam("courseId") int courseId) {
        Map<Integer, String> comments;
        try {
            comments = adminService.getCourseComments(courseId);
        } catch (CourseNotExistException e) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(comments);
    }

    /**
     * Endpoint of /deleteComment/{commentId} URL.
     * Deletes the course's comment, specified by passed arguments.
     *
     * @param authToken User's security token. Identifies which user is trying to modify the data.
     * @param commentId Identifies which comment will be deleted.
     *
     * @return JSON data wrapped into {@link ResponseEntity} with HttpStatus code "OK" if everything done
     * successfully, {@link ResponseEntity} with HttpStatus Code "BAD_REQUEST" if passed arguments are
     * inappropriate.
     */
    @Secure(role = Role.ADMIN)
    @RequestMapping(value = "/deleteComment/{commentId}", method = RequestMethod.GET)
    public ResponseEntity deleteComment(@RequestHeader("Authorization") String authToken,
                                        @PathVariable("commentId") int commentId) {
        int row = adminService.deleteComment(commentId);
        if (row > 0)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }

    /**
     * Endpoint of /course/{courseId} URL.
     * Gets course's information.
     *
     * @param authToken User's security token. Identifies which user is trying to get data.
     * @param courseId Identifies which course's information is needed.
     *
     * @return Courses information wrapped into {@link ResponseEntity}.
     */
    @Secure(role = Role.ADMIN)
    @RequestMapping(value = "/course/{courseId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCourse(@RequestHeader("Authorization") String authToken,
                                                      @PathVariable("courseId") int courseId) {
        return super.getCourse(authToken, courseId);
    }

    /**
     * Endpoint of /course/{courseId}/sections URL.
     * Gets specified course's sections.
     *
     * @param authToken User's security token. Identifies which user is trying to get data.
     * @param courseId Identifies which course's sections are needed.
     *
     * @return Sections' information wrapped into {@link ResponseEntity}.
     */
    @Secure(role = Role.ADMIN)
    @RequestMapping(value = "/course/{courseId}/sections", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getSections(@RequestHeader("Authorization") String authToken,
                                      @PathVariable("courseId") int courseId) {
        return super.getSections(authToken, courseId);
    }

    /**
     * Endpoint of /course/{courseId}/sections/{sectionId} URL.
     * Gets specified section's all items.
     *
     * @param token User's security token. Identifies which user is trying to get data.
     * @param courseId Identifies which course's items are needed. Needed for teachers.
     * @param sectionId Identifies which section's items are needed.
     *
     * @return Specified section's all items' list wrapped into {@link ResponseEntity}.
     */
    @Secure(role = Role.ADMIN)
    @RequestMapping(value = "/course/{courseId}/sections/{sectionId}", method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity getSectionAllItems(@RequestHeader("Authorization") String token,
                                             @PathVariable("courseId") int courseId,
                                             @PathVariable("sectionId") int sectionId) {

        return super.getSectionAllItems(token, courseId, sectionId);
    }

    /**
     * Endpoint of /createCategory URL.
     * Creates new category.
     *
     * @param authToken User's security token. Identifies which user is trying add data.
     * @param categoryName The new category's name.
     *
     * @return {@link ResponseEntity} with HttpStatus code "OK" if all done successfully.
     */
    @Secure(role = Role.ADMIN)
    @RequestMapping(value = "/createCategory", method = RequestMethod.POST)
    public ResponseEntity createCategory(@RequestHeader("Authorization") String authToken,
                                         @RequestBody String categoryName) {

        try {
            if (!DirectoryHandler.createCategory(categoryName)) {
                return new ResponseEntity<>(categoryName + " category already exists", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        adminService.createCategory(categoryName);

        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint of /createSubcategory URL.
     * Create's new subcategory.
     *
     * @param authToken User's security token. Identifies which user is trying add data.
     * @param categoryId Identifies to which category new subcategory will belong.
     * @param subCategoryName The ne subcategory's name.
     *
     * @return {@link ResponseEntity} with HttpStatus code "OK" if all done successfully.
     */
    @Secure(role = Role.ADMIN)
    @RequestMapping(value = "/createSubcategory", method = RequestMethod.POST)
    public ResponseEntity createSubcategory(@RequestHeader("Authorization") String authToken,
                                            @RequestParam("categoryId") int categoryId,
                                            @RequestBody String subCategoryName) {

        if (subCategoryName == null || "".equals(subCategoryName))
            return new ResponseEntity<>("empty subcategory name",HttpStatus.BAD_REQUEST);

        String category;
        try {
            category = courseService.getCategoryName(categoryId);
        } catch (IllegalParameterException e) {
            return new ResponseEntity<>("not valid courseId", HttpStatus.BAD_REQUEST);
        }
        try {
            if (!DirectoryHandler.createSubcategory(category, subCategoryName)) {
                return new ResponseEntity<>(subCategoryName + " subcategory already exists", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        adminService.createSubcategory(categoryId, subCategoryName);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint of /download/{itemId} URL.
     *
     * @param token User's security token. Identifies which user is trying to start download stream.
     * @param itemId Identifies which item must be downloaded.
     *
     * @return Download stream parsed into {@link ResponseEntity}.
     */
    @Secure(role = Role.ADMIN)
    @RequestMapping(value = "download/{itemId}", method = RequestMethod.GET)
    public ResponseEntity download(@RequestHeader("Authorization") String token,
                                   @PathVariable("itemId") int itemId) {
        String path;
        try {
            path = adminService.getFilePath(itemId);
        } catch (IllegalParameterException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return downloadFile(path);
    }

}
