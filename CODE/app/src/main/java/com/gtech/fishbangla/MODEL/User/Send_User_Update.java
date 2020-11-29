package com.gtech.fishbangla.MODEL.User;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Send_User_Update {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("userEmail")
    @Expose
    private String userEmail;
    @SerializedName("userFullName")
    @Expose
    private String userFullName;

    public Send_User_Update(String userId, String userEmail, String userFullName) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userFullName = userFullName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    @Override
    public String toString() {
        return "Send_User_Update{" +
                "userId='" + userId + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userFullName='" + userFullName + '\'' +
                '}';
    }
}