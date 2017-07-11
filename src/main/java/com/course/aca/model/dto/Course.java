package com.course.aca.model.dto;

/**
 * Data Transfer Object for Course-relative minimal necessary data.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class Course {

    private int id;
    private String name;
    private double rating;
    private int reviewCount;
    private String teacherName;
    private int price;

    public Course(int id, String name, double rating, int reviewCount, String teacherName, int price) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.teacherName = teacherName;
        this.price = price;
    }

    public Course() {
    }

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
}
