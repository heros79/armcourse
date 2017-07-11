package com.course.aca.model.dto;

/**
 * Data Transfer Object for course's review information.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class Review {
    private int rating;
    private String review;

    public Review(int rating, String review) {
        this.rating = rating;
        this.review = review;
    }

    public Review() {
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
}
