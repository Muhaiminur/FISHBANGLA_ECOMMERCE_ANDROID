package com.gtech.fishbangla.MODEL.AUCTION;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GET_AUCTION_LIST{

    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("productPicture")
    @Expose
    private String productPicture;
    @SerializedName("productUnit")
    @Expose
    private String productUnit;
    @SerializedName("startingPrice")
    @Expose
    private String startingPrice;
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

    public String getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(String productPicture) {
        this.productPicture = productPicture;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(String startingPrice) {
        this.startingPrice = startingPrice;
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
        return "GET_AUCTION_LIST{" +
                "productId='" + productId + '\'' +
                ", productPicture='" + productPicture + '\'' +
                ", productUnit='" + productUnit + '\'' +
                ", startingPrice='" + startingPrice + '\'' +
                ", expireAt='" + expireAt + '\'' +
                ", productName='" + productName + '\'' +
                '}';
    }
}