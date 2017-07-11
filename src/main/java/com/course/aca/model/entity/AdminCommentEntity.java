package com.course.aca.model.entity;

/**
 * Entity class. Contains Admin Comment information.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class AdminCommentEntity {
    private int id;
    private int courseId;
    private String comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminCommentEntity entity = (AdminCommentEntity) o;

        if (id != entity.id) return false;
        if (courseId != entity.courseId) return false;
        return comment.equals(entity.comment);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + courseId;
        result = 31 * result + comment.hashCode();
        return result;
    }
}
