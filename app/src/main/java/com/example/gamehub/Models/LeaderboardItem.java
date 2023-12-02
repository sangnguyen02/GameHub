package com.example.gamehub.Models;

import com.google.firebase.database.PropertyName;

public class LeaderboardItem {
    @PropertyName("Fullname")
    private String fullname;

    @PropertyName("UserID")
    private String userId;

    @PropertyName("UserScore")
    private int userScore;

    public LeaderboardItem() {
        // Default constructor required for calls to DataSnapshot.getValue(LeaderboardItem.class)
    }

    public LeaderboardItem(String fullname, String userId, int userScore) {
        this.fullname = fullname;
        this.userId = userId;
        this.userScore = userScore;
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

    public int getUserScore() {
        return userScore;
    }

    public void setUserScore(int userScore) {
        this.userScore = userScore;
    }
}
