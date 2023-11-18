package com.example.gamehub.Models;

import com.google.firebase.database.PropertyName;

public class User {
    private String userId;

    @PropertyName("Email")
    private String email;
    @PropertyName("Fullname")
    private String fullname;
    @PropertyName("Gender")
    private String gender;
    @PropertyName("Location")
    private String location;
    @PropertyName("Phone")
    private String phone;
    @PropertyName("Status")
    private String status;

    @PropertyName("image")
    private String image;

    public User() {

    }

    public User(String email, String fullname, String gender, String location, String phone, String status, String image) {
        this.email = email;
        this.fullname = fullname;
        this.gender = gender;
        this.location = location;
        this.phone = phone;
        this.status = status;
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
