package com.course.aca.model.entity;

/**
 * Entity class. Contains Student-Course information.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class StudentCourseEntity {

    private int id;
    private int studentId;
    private int courseId;
    private int rating;
    private String review;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentCourseEntity entity = (StudentCourseEntity) o;

        if (id != entity.id) return false;
        if (studentId != entity.studentId) return false;
        if (courseId != entity.courseId) return false;
        if (Double.compare(entity.rating, rating) != 0) return false;
        return review.equals(entity.review);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + studentId;
        result = 31 * result + courseId;
        temp = Double.doubleToLongBits(rating);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + review.hashCode();
        return result;
    }
}
