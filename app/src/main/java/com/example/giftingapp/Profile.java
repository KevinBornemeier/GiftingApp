package com.example.giftingapp;

import java.io.Serializable;


public class Profile implements Serializable {

    //@Exclude private String id;

    private String name, imageUrl, shoeSize, shirtSize, pantsSize, favoriteColor, userID, id, adminEmail;

    //default constructor
    public Profile(){

    }

    //constructor to initialize all values
    public Profile(String id,String userID, String name, String imageUrl, String shoeSize, String shirtSize, String pantsSize, String favoriteColor, String adminEmail) {
        this.id = id;
        this.userID = userID;
        this.name = name;
        this.shoeSize = shoeSize;
        this.shirtSize = shirtSize;
        this.pantsSize = pantsSize;
        this.favoriteColor = favoriteColor;
        this.imageUrl = imageUrl;
        this.adminEmail = adminEmail;
    }

    public void setID(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setShoeSize(String shoeSize) {
        this.shoeSize = shoeSize;
    }

    public void setShirtSize(String shirtSize) {
        this.shirtSize = shirtSize;
    }

    public void setPantsSize(String pantsSize) {
        this.pantsSize = pantsSize;
    }

    public void setFavoriteColor(String favoriteColor) {
        this.favoriteColor = favoriteColor;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getID() {
        return id;
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


    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }
}
