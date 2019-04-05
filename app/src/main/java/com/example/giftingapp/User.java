package com.example.giftingapp;

public class User  {


    private String userId, userType;

    //default constructor
    public User(){

    }

    //constructor to initialize all values
    public User(String userID, String userType) {
        this.userId = userID;
        this.userType = userType;

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
}



