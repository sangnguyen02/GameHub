package com.unity3d.player.Models;
public class LeaderboardItem {
    private String userId;
    private String userName;
    private int userScore;

    public LeaderboardItem() {
        // Default constructor required for calls to DataSnapshot.getValue(LeaderboardItem.class)
    }

    public LeaderboardItem(String userId, String userName, int userScore) {
        this.userId = userId;
        this.userName = userName;
        this.userScore = userScore;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserScore() {
        return userScore;
    }
}
