package com.gtech.fishbangla.MODEL.User;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Get_Sellerlist{

    @SerializedName("userImage")
    @Expose
    private String userImage;
    @SerializedName("sellerRating")
    @Expose
    private String sellerRating;
    @SerializedName("userPhoneNo")
    @Expose
    private String userPhoneNo;
    @SerializedName("userFullName")
    @Expose
    private String userFullName;
    @SerializedName("totalRating")
    @Expose
    private String totalRating;
    @SerializedName("userId")
    @Expose
    private String userId;

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getSellerRating() {
        return sellerRating;
    }

    public void setSellerRating(String sellerRating) {
        this.sellerRating = sellerRating;
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

    public String getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(String totalRating) {
        this.totalRating = totalRating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}