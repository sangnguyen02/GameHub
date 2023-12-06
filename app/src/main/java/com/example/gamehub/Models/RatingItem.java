package com.example.gamehub.Models;

import com.google.firebase.database.PropertyName;

public class RatingItem {
    @PropertyName("Fullname")
    private String fullname;

    @PropertyName("UserID")
    private String userId;

    @PropertyName("RatingScore")
    private String ratingScore;

    public RatingItem() {
    }

    public RatingItem(String fullname, String userId, String ratingScore) {
        this.fullname = fullname;
        this.userId = userId;
        this.ratingScore = ratingScore;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(String ratingScore) {
        this.ratingScore = ratingScore;
    }
}
