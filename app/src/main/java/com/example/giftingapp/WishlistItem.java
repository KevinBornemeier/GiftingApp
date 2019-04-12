package com.example.giftingapp;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class WishlistItem implements Serializable {

    @Exclude
    private String id;

    private String price, title, itemURL, imageURL;



    public WishlistItem() {
    }

    public String getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getItemURL() {
        return itemURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getId() {
        return id;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setItemURL(String itemURL) {
        this.itemURL = itemURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setId(String id) {
        this.id = id;
    }

}
