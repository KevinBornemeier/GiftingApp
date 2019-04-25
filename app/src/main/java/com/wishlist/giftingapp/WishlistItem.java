package com.wishlist.giftingapp;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class WishlistItem implements Serializable {

    @Exclude
    private String id;

    private String price, title, itemURL, imageURL, profileID;

    private boolean isPurchased;



    public WishlistItem() {
    }

    public WishlistItem(String id, String price, String title, String itemURL, String imageURL, String profileID, boolean isPurchased) {

        this.id = id;
        this.price = price;
        this. title = title;
        this.itemURL = itemURL;
        this.imageURL = imageURL;
        this.profileID = profileID;
        this.isPurchased = isPurchased;

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

    public String getProfileID () { return profileID; }

    public boolean getIsPurchased() { return isPurchased; }

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

    public void setProfileID(String profileID) { this.profileID = profileID; }

    public void setIsPurchased(boolean isPurchased) { this.isPurchased = isPurchased; }
}
