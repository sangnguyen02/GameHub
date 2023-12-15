package com.example.gamehub.Models;

public class Game {
    private int imageResource;
    private String gameName;

    public Game(int imageResource, String gameName) {
        this.imageResource = imageResource;
        this.gameName = gameName;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getGameName() {
        return gameName;
    }
}
