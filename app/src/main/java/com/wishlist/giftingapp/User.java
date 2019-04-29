package com.wishlist.giftingapp;

public class User  {


    private String userId, userType, adminEmail1, adminEmail2, adminEmail3, adminEmail4, id;

    //default constructor
    public User(){

    }

    //constructor to initialize all values
    public User(String id, String userID, String userType, String adminEmail1, String adminEmail2,
                String adminEmail13, String adminEmail4      ) {
        this.userId = userID;
        this.userType = userType;
        this.adminEmail1 = adminEmail1;
        this.adminEmail2 = adminEmail2;
        this.adminEmail3 = adminEmail3;
        this.adminEmail4 = adminEmail4;
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

    public String getAdminEmail1() {
        return adminEmail1;
    }

    public void setAdminEmail1(String adminEmail) {
        this.adminEmail1 = adminEmail;
    }

    public String getAdminEmail2() {
        return adminEmail2;
    }

    public void setAdminEmail2(String adminEmail2) {
        this.adminEmail2 = adminEmail2;
    }

    public String getAdminEmail3() {
        return adminEmail3;
    }

    public void setAdminEmail3(String adminEmail3) {
        this.adminEmail3 = adminEmail3;
    }

    public String getAdminEmail4() {
        return adminEmail4;
    }

    public void setAdminEmail4(String adminEmail4) {
        this.adminEmail4 = adminEmail4;
    }

    public String getID() {
        return id;
    }

    public void setID(String ID) {
        this.id = id;
    }
}



