package com.gtech.fishbangla.MODEL.User;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Get_UserLogin {

    @SerializedName("userImage")
    @Expose
    private String userImage;
    @SerializedName("beingFollowed")
    @Expose
    private String beingFollowed;
    @SerializedName("userPhoneNo")
    @Expose
    private String userPhoneNo;
    @SerializedName("userFullName")
    @Expose
    private String userFullName;
    @SerializedName("userEmail")
    @Expose
    private String userEmail;
    @SerializedName("avgRating")
    @Expose
    private String avgRating;
    @SerializedName("userId")
    @Expose
    private String userId;

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getBeingFollowed() {
        return beingFollowed;
    }

    public void setBeingFollowed(String beingFollowed) {
        this.beingFollowed = beingFollowed;
    }

    public String getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(String userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(String avgRating) {
        this.avgRating = avgRating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Get_UserLogin{" +
                "userImage='" + userImage + '\'' +
                ", beingFollowed='" + beingFollowed + '\'' +
                ", userPhoneNo='" + userPhoneNo + '\'' +
                ", userFullName='" + userFullName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", avgRating='" + avgRating + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}