package com.course.aca.model.entity;

/**
 * Entity class. Contains Student-Course purchase request information.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class StudentCourseRequestEntity {

    private int id;
    private int studentId;
    private String studentFullName;
    private int courseId;
    private String courseName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getStudentFullName() {
        return studentFullName;
    }

    public void setStudentFullName(String studentFullName) {
        this.studentFullName = studentFullName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
