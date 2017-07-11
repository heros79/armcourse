package com.course.aca.controller;

import com.course.aca.annotation.BelongToTeacher;
import com.course.aca.annotation.Secure;
import com.course.aca.enums.Role;
import com.course.aca.exception.IllegalParameterException;
import com.course.aca.model.dto.Course;
import com.course.aca.model.dto.StudentCourseRequest;
import com.course.aca.model.entity.PathComponentsEntity;
import com.course.aca.service.CourseService;
import com.course.aca.service.TeacherService;
import com.course.aca.service.system.DirectoryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * A controller class. Contains endpoints for /teacher/* URLs.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController extends AbstractCourseController {

    /**
     * Service classes' beans.
     *
     * Initialized using "Autowired" annotation in constructor.
     */
    private final TeacherService teacherService;
    private final CourseService courseService;

    /**
     * Initializes autowired beans in this class.
     *
     * @param teacherService
     * @param courseService
     */
    @Autowired
    public TeacherController(TeacherService teacherService, CourseService courseService) {
        super(courseService);
        this.teacherService = teacherService;
        this.courseService = courseService;
    }

    /**
     * Secured endpoint of /pending URL.
     * Gets specified teacher's courses with status "Pending".
     *
     * @param authToken Teacher's security token. Identifies which user's pending course's are needed.
     * @param pageIndex Used in MySQL query's offset.
     *
     * @return Required {@link List<Course>} wrapped into {@link ResponseEntity} with HttpStatus code "OK" if the data
     * has been get successfully, Otherwise returns {@link ResponseEntity} with HttpStatus code "BAD_REQUEST".
     */
    @Secure(role = Role.TEACHER)
    @RequestMapping(value = "/pending", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getPendingCourses(@RequestHeader("Authorization") String authToken,
                                            @RequestParam("pageIndex") int pageIndex) {
        if (pageIndex < 1)
            return new ResponseEntity<>("pageId should be > 0", HttpStatus.BAD_REQUEST);

        List<Course> courseList = teacherService.getCoursesByTeacherId(authToken, pageIndex, 3);
        if (courseList != null)
            return ResponseEntity.ok(courseList);
        else
            return ResponseEntity.noContent().build();
    }

    /**
     * Secured endpoint of /disapproved URL.
     * Gets specified teacher's courses with status "Disapproved".
     *
     * @param authToken Teacher's security token. Identifies which user's disapproved course's are needed.
     * @param pageIndex Used in MySQL query's offset.
     *
     * @return Required {@link List<Course>} wrapped into {@link ResponseEntity} with HttpStatus code "OK" if the data
     * has been get successfully, Otherwise returns {@link ResponseEntity} with HttpStatus code "BAD_REQUEST".
     */
    @Secure(role = Role.TEACHER)
    @RequestMapping(value = "/disapproved", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getDisapprovedCourses(@RequestHeader("Authorization") String authToken,
                                                @RequestParam("pageIndex") int pageIndex) {

        if (pageIndex < 1)
            return new ResponseEntity<>("pageId should be > 0", HttpStatus.BAD_REQUEST);

        List<Course> courseList = teacherService.getCoursesByTeacherId(authToken, pageIndex, 2);
        if (courseList != null)
            return ResponseEntity.ok(courseList);
        else
            return ResponseEntity.noContent().build();
    }

    /**
     * Secured endpoint of /approved URL.
     * Gets specified teacher's courses with status "Approved".
     *
     * @param authToken Teacher's security token. Identifies which user's approoved course's are needed.
     * @param pageIndex Used in MySQL query's offset.
     *
     * @return Required {@link List<Course>} wrapped into {@link ResponseEntity} with HttpStatus code "OK" if the data
     * has been get successfully, Otherwise returns {@link ResponseEntity} with HttpStatus code "BAD_REQUEST".
     */
    @Secure(role = Role.TEACHER)
    @RequestMapping(value = "/approved", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getApprovedCourses(@RequestHeader("Authorization") String authToken,
                                             @RequestParam("pageIndex") int pageIndex) {

        if (pageIndex < 1)
            return new ResponseEntity<>("pageId should be > 0", HttpStatus.BAD_REQUEST);


        List<Course> courseList = teacherService.getCoursesByTeacherId(authToken, pageIndex, 1);
        if (courseList != null)
            return ResponseEntity.ok(courseList);
        else
            return ResponseEntity.noContent().build();
    }

    /**
     * Secured endpoint of /createCourse URL.
     * Provides course creation functionality.
     *
     * @param authToken Teacher's  security token. Identifies which teacher is trying to crate new course.
     * @param categoryId Identifies to which category the new course must belong.
     * @param subcategoryId Identifies to which subcategory the new course must belong.
     * @param courseName Newly created course's name.
     *
     * @return Newly created course's id.
     */
    @Secure(role = Role.TEACHER)
    @RequestMapping(value = "/createCourse", method = RequestMethod.POST, consumes = "text/plain")
    public ResponseEntity createCourse(@RequestHeader("Authorization") String authToken,
                                       @RequestParam("categoryId") int categoryId,
                                       @RequestParam("subcategoryId") int subcategoryId,
                                       @RequestBody String courseName) {

        if (courseName == null || "".equals(courseName))
            return new ResponseEntity<>("empty course name", HttpStatus.BAD_REQUEST);

        try {
            if (courseService.getCategoryIdBySubcategory(subcategoryId) != categoryId) {
                return new ResponseEntity<>("subcategory doesn't belong to this category", HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalParameterException e) {
            return new ResponseEntity<>("categoryId or subcategoryId is incorrect", HttpStatus.BAD_REQUEST);
        }

        String category = null;
        String subcategory = null;
        try {
            category = courseService.getCategoryName(categoryId);
            subcategory = courseService.getSubcategoryName(subcategoryId);
        } catch (IllegalParameterException e) {
            e.printStackTrace();
        }

        try {
            if (!DirectoryHandler.createCourse(category, subcategory, courseName)) {
                return new ResponseEntity<>("Duplicate course name: " + courseName +
                        " is already exist", HttpStatus.CONFLICT);
            }
            int id = teacherService.createCourse(authToken, subcategoryId, courseName);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Couldn't create folder " + courseName, HttpStatus.EXPECTATION_FAILED);
        }
    }

    /**
     * Secured endpoint of /requestList URL.
     * Gets all Student-Course purchase requests' list for specified teacher.
     *
     * @param authToken Teacher's security token. Identifies which teacher's Student-Course purchase requests are needed.
     *
     * @return Requested {@link List<StudentCourseRequest>} wrapped into {@link ResponseEntity} with HttpStatus code "OK".
     */
    @Secure(role = Role.TEACHER)
    @RequestMapping(value = "/requestList", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<StudentCourseRequest>> getStudentRequestList(@RequestHeader("Authorization") String authToken) {

        List<StudentCourseRequest> studentCourseList = teacherService.getStudentCourseRequestList(authToken);

        return ResponseEntity.ok(studentCourseList);
    }

    /**
     * Secured endpoint of /requestList/approveRequest URL.
     * Approves specified student's Student-Course purchase request.
     *
     * @param authToken Teacher's security token. Identifies which teacher will approve the request.
     * @param requestId Identifies the Student-Course purchase request that will be approved.
     *
     * @return {@link ResponseEntity} with HttpStatus code "OK" if the request has been approved. Otherwise returns
     * {@link ResponseEntity} with HttpStatus code "BAD_REQUEST".
     */
    @Secure(role = Role.TEACHER)
    @RequestMapping(value = "/requestList/approveRequest", method = RequestMethod.GET)
    public ResponseEntity approveStudentRequest(@RequestHeader("Authorization") String authToken,
                                                @RequestParam("RequestId") int requestId) {
        try {
            if (!teacherService.requestIsAddressedToTeacher(authToken, requestId)) {
                return new ResponseEntity<>("This course doesn't belong to You.", HttpStatus.BAD_REQUEST);
            }
            teacherService.approveRequest(requestId);
        } catch (IllegalParameterException e) {
            e.printStackTrace();
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Request has been approved.");
    }

    /**
     * Secured endpoint of /createSection URL.
     * Add a new section to already existing course.
     *
     * @param authToken Teacher's security token. Identifies which teacher is going to add new section.
     * @param courseId Identifies to which course the new section must belong.
     * @param sectionName Newly created section's name.
     *
     * @return Newly created section's id wrapped into {@link ResponseEntity} with HttpStatus code "OK" if the new
     * section has been created successfully, {@link ResponseEntity} with HttpStatus code "BAD_REQUEST" if the passed
     * parameters have invalid format, {@link ResponseEntity} with HttpStatus code "CONFLICT" if the section with
     * specified name already exists or {@link ResponseEntity} with HttpStatus code "EXPECTATION_FAILED" if the directory
     * hasn't been created due to I/O issues.
     */
    @BelongToTeacher
    @RequestMapping(value = "/createSection", method = RequestMethod.POST)
    public ResponseEntity createSection(@RequestHeader("Authorization") String authToken,
                                        @RequestParam("courseId") int courseId,
                                        @RequestBody String sectionName) {
        if (sectionName == null || "".equals(sectionName))
            return new ResponseEntity<>("empty section name", HttpStatus.BAD_REQUEST);

        PathComponentsEntity path = teacherService.getPathComponents(courseId);

        try {
            if (!DirectoryHandler.createCourseSection(path.getCategoryName(), path.getSubcategoryName(),
                    path.getCourseName(), sectionName)) {
                return new ResponseEntity<>("Duplicate section name:" + sectionName +
                        " is already exist", HttpStatus.CONFLICT);
            }
            int sectionId = teacherService.createSection(courseId, sectionName);
            return new ResponseEntity<>(sectionId, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Couldn't create folder", HttpStatus.EXPECTATION_FAILED);
        }

    }

    /**
     * Secured endpoint of /course/{courseId} URL.
     * Gets specified course, published by specified teacher.
     *
     * @param authToken Teacher's Authentication token. Identifies which teacher's course id needed.
     * @param courseId Identifies which course's is needed.
     *
     * @return Requested course wrapped into {@link ResponseEntity} with HttpStatus code "OK".
     */
    @BelongToTeacher
    @RequestMapping(value = "/course/{courseId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCourse(@RequestHeader("Authorization") String authToken,
                                    @PathVariable("courseId") int courseId) {
        return super.getCourse(authToken, courseId);
    }

    /**
     * Secured endpoint of /course/{courseId}/sections URL.
     * Gets specified teacher's specified course's all sections.
     *
     * @param authToken Teacher's security token. Identifies which teacher is asking for data.
     * @param courseId Identifies which course's sections are needed.
     *
     * @return Requested Sections information wrapped into {@link ResponseEntity} with HttpStatus code "OK".
     */
    @BelongToTeacher
    @RequestMapping(value = "/course/{courseId}/sections", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getSections(@RequestHeader("Authorization") String authToken,
                                      @PathVariable("courseId") int courseId) {
        return super.getSections(authToken, courseId);
    }

    /**
     * Secured endpoint of course/{courseId}/sections/{sectionId} URL.
     *
     * @param token Teacher's security token. Identifies which teacher is asking for data.
     * @param courseId Used in identifying which course belongs to specific teacher.
     * @param sectionId Identifies which section's items' information is requested.
     *
     * @return Requested items' list wrapped into {@link ResponseEntity} with HttpStatus code "OK" if data is gotten
     * without any issues. Otherwise returns {@link ResponseEntity} with HttpStatus code "BAD_REQUEST".
     */
    @BelongToTeacher
    @RequestMapping(value = "/course/{courseId}/sections/{sectionId}", method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity getSectionAllItems(@RequestHeader("Authorization") String token,
                                             @PathVariable("courseId") int courseId,
                                             @PathVariable("sectionId") int sectionId) {
        int id;

        try {
            id = teacherService.getCourseIdBySection(sectionId);
        } catch (IllegalParameterException e) {
            return new ResponseEntity<>("Illegal Parameter Exception", HttpStatus.BAD_REQUEST);
        }

        if (courseId != id)
            return new ResponseEntity<>("not such section in this course", HttpStatus.BAD_REQUEST);

        return super.getSectionAllItems(token, courseId, sectionId);
    }

    /**
     * Secured endpoint of /download/{itemId} URl.
     * Starts download stream for specified item.
     *
     * @param authToken Teacher's security token. Identifies which user is trying to start downloading.
     * @param itemId Identifies which item will be downloaded.
     *
     * @return {@link InputStreamResource} wrapped into {@link ResponseEntity} with "OK" HttpStatus if file exists.
     * Otherwise returns {@link ResponseEntity} with "BAD_REQUEST" HttpStatus.
     */
    @Secure(role = Role.TEACHER)
    @RequestMapping(value = "/download/{itemId}", method = RequestMethod.GET)
    public ResponseEntity download(@RequestHeader("Authorization") String authToken,
                                   @PathVariable("itemId") int itemId) {

        try {
            if (!teacherService.isItemBelongToTeacher(itemId, authToken))
                return new ResponseEntity<>("FORBIDDEN", HttpStatus.FORBIDDEN);
        } catch (IllegalParameterException e) {
            return new ResponseEntity<>("IllegalParameterException", HttpStatus.BAD_REQUEST);
        }


        String path;
        try {
            path = teacherService.getFilePath(itemId);
        } catch (IllegalParameterException e) {
            return new ResponseEntity<>("Illegal request parameter", HttpStatus.BAD_REQUEST);
        }
        return downloadFile(path);
    }

    /**
     * Secured endpoint of /setDescription URL.
     * Sets specified course's description to specified value.
     *
     * @param authToken Teacher's security token. Identifies which teacher is going to add new description.
     * @param courseId Identifies which course's description will be added.
     * @param description New description's value.
     *
     * @return {@link ResponseEntity} with HttpStatus code "OK".
     */
    @BelongToTeacher
    @RequestMapping(value = "/setDescription", method = RequestMethod.PUT)
    public ResponseEntity setDescription(@RequestHeader("Authorization") String authToken,
                                         @RequestParam("courseId") int courseId,
                                         @RequestBody String description) {
        teacherService.setDescription(description, courseId);
        return ResponseEntity.ok().build();
    }

    /**
     * Secured endpoint of /setPrice URL.
     * Sets specified course's price to specified value.
     *
     * @param authToken Teacher's security token. Identifies which teacher is going to add new price.
     * @param courseId Identifies which course's price will be added.
     * @param price New price's value.
     *
     * @return {@link ResponseEntity} with HttpStatus code "OK" if everything gone without issues, otherwise returns
     * {@link ResponseEntity} with HttpStatus code "BAD_REQUEST".
     */
    @BelongToTeacher
    @RequestMapping(value = "/setPrice", method = RequestMethod.POST)
    public ResponseEntity setPrice(@RequestHeader("Authorization") String authToken,
                                   @RequestParam("courseId") int courseId,
                                   @RequestParam("price") int price) {
        if (courseId < 1 || price < 0)
            return new ResponseEntity<>("Illegal request parameters", HttpStatus.BAD_REQUEST);

        teacherService.setCoursePrice(courseId, price);
        return ResponseEntity.ok().build();
    }
}