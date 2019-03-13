package com.example.giftingapp;

public class Profile {

    private String name, imageUrl, shoeSize, shirtSize, pantsSize, favoriteColor, userID;

    //default constructor
    public Profile(){

    }

    //constructor to initialize all values
    public Profile(String userID, String name, String imageUrl, String shoeSize, String shirtSize, String pantsSize, String favoriteColor) {
        this.userID = userID;
        this.name = name;
        this.shoeSize = shoeSize;
        this.shirtSize = shirtSize;
        this.pantsSize = pantsSize;
        this.favoriteColor = favoriteColor;
        this.imageUrl = imageUrl;
    }

    public String getUserID() {
        return userID;
    }

    public String getImageUrl() {
        return imageUrl;
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
