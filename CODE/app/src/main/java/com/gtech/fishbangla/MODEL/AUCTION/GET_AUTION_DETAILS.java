package com.gtech.fishbangla.MODEL.AUCTION;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gtech.fishbangla.MODEL.PRODUCT.Product_Image;

import java.util.List;

public class GET_AUTION_DETAILS{

    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("userPhoneNo")
    @Expose
    private String userPhoneNo;
    @SerializedName("userFullName")
    @Expose
    private String userFullName;
    @SerializedName("productPicture")
    @Expose
    private List<Product_Image> productPicture = null;
    @SerializedName("productVideo")
    @Expose
    private String productVideo;
    @SerializedName("currentPrice")
    @Expose
    private String currentPrice;
    @SerializedName("startingPrice")
    @Expose
    private String startingPrice;
    @SerializedName("productUnit")
    @Expose
    private String productUnit;
    @SerializedName("expireAt")
    @Expose
    private String expireAt;
    @SerializedName("productName")
    @Expose
    private String productName;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public List<Product_Image> getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(List<Product_Image> productPicture) {
        this.productPicture = productPicture;
    }

    public String getProductVideo() {
        return productVideo;
    }

    public void setProductVideo(String productVideo) {
        this.productVideo = productVideo;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(String startingPrice) {
        this.startingPrice = startingPrice;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(String expireAt) {
        this.expireAt = expireAt;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "GET_AUTION_DETAILS{" +
                "productId='" + productId + '\'' +
                ", userPhoneNo='" + userPhoneNo + '\'' +
                ", userFullName='" + userFullName + '\'' +
                ", productPicture=" + productPicture +
                ", productVideo='" + productVideo + '\'' +
                ", currentPrice='" + currentPrice + '\'' +
                ", startingPrice='" + startingPrice + '\'' +
                ", productUnit='" + productUnit + '\'' +
                ", expireAt='" + expireAt + '\'' +
                ", productName='" + productName + '\'' +
                '}';
    }
}