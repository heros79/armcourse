package com.course.aca.model.entity;

/**
 * Entity class. Contains student's review information about class.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class ReviewEntity {
    private int rating;
    private String review;

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
