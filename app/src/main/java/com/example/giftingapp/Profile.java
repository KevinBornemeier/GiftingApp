package com.example.giftingapp;

public class Profile {

    private String name, shoeSize, shirtSize, pantsSize, favoriteColor, userID;

    //default constructor
    public Profile(){

    }

    //constructor to initialize all values
    public Profile(String userID, String name, String shoeSize, String shirtSize, String pantsSize, String favoriteColor) {
        this.userID = userID;
        this.name = name;
        this.shoeSize = shoeSize;
        this.shirtSize = shirtSize;
        this.pantsSize = pantsSize;
        this.favoriteColor = favoriteColor;
    }

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getShoeSize() {
        return shoeSize;
    }

    public String getShirtSize() {
        return shirtSize;
    }

    public String getPantsSize() {
        return pantsSize;
    }

    public String getFavoriteColor() {
        return favoriteColor;
    }
}
