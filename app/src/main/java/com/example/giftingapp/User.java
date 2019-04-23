package com.example.giftingapp;

public class User  {


    private String userId, userType, adminEmail, id;

    //default constructor
    public User(){

    }

    //constructor to initialize all values
    public User(String id, String userID, String userType, String adminEmail) {
        this.userId = userID;
        this.userType = userType;
        this.adminEmail = adminEmail;
        this.id = id;

    }

    public String getUserId() {
        return userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getID() {
        return id;
    }

    public void setID(String ID) {
        this.id = id;
    }
}



