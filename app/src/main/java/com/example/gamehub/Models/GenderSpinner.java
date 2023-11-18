package com.example.gamehub.Models;

public class GenderSpinner {
    private String name, imageName;

    public GenderSpinner(String name, String imageName) {
        this.name = name;
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
