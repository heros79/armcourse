package com.course.aca.model.entity;

/**
 * Entity class. Contains course's main information.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class CourseEntity {
    private int id;
    private String name;
    private int price;
    private double rating;
    private int reviewCount;
    private String teacherName;


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

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseEntity)) return false;

        CourseEntity that = (CourseEntity) o;

        if (getId() != that.getId()) return false;
        if (Double.compare(that.getRating(), getRating()) != 0) return false;
        if (getReviewCount() != that.getReviewCount()) return false;
        if (getPrice() != that.getPrice()) return false;
        if (!getName().equals(that.getName())) return false;
        return getTeacherName().equals(that.getTeacherName());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getId();
        result = 31 * result + getName().hashCode();
        temp = Double.doubleToLongBits(getRating());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getReviewCount();
        result = 31 * result + getTeacherName().hashCode();
        result = 31 * result + getPrice();
        return result;
    }
}
