package com.course.aca.model.entity;

import java.util.List;

/**
 * Entity class. Contains course's full information.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class CourseCommonInfoEntity {

    private int id;
    private String name;
    private String status;
    private String description;
    private double rating;
    private int reviewCount;
    private int price;
    private String teacherName;
    private int teacherId;
    private int subCategoryId;
    private double totalHours;
    private int totalMaterials;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(double totalHours) {
        this.totalHours = totalHours;
    }

    public int getTotalMaterials() {
        return totalMaterials;
    }

    public void setTotalMaterials(int totalMaterials) {
        this.totalMaterials = totalMaterials;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseCommonInfoEntity that = (CourseCommonInfoEntity) o;

        if (id != that.id) return false;
        if (Double.compare(that.rating, rating) != 0) return false;
        if (reviewCount != that.reviewCount) return false;
        if (price != that.price) return false;
        if (teacherId != that.teacherId) return false;
        if (subCategoryId != that.subCategoryId) return false;
        if (Double.compare(that.totalHours, totalHours) != 0) return false;
        if (totalMaterials != that.totalMaterials) return false;
        if (!name.equals(that.name)) return false;
        if (!status.equals(that.status)) return false;
        if (!description.equals(that.description)) return false;
        return teacherName.equals(that.teacherName);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + description.hashCode();
        temp = Double.doubleToLongBits(rating);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + reviewCount;
        result = 31 * result + price;
        result = 31 * result + teacherName.hashCode();
        result = 31 * result + teacherId;
        result = 31 * result + subCategoryId;
        temp = Double.doubleToLongBits(totalHours);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + totalMaterials;
        return result;
    }
}
