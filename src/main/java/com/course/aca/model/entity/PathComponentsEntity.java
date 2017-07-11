package com.course.aca.model.entity;

/**
 * Entity class. Contains course's path's components information.
 * Used in Course's and it's item's full and direct path generation logic.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class PathComponentsEntity {
    private String categoryName;
    private String subcategoryName;
    private String courseName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
