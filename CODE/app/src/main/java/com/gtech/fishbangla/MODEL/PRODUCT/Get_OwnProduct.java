package com.gtech.fishbangla.MODEL.PRODUCT;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Get_OwnProduct {

    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("upazillaName")
    @Expose
    private String upazillaName;
    @SerializedName("districtName")
    @Expose
    private String districtName;
    @SerializedName("userFullName")
    @Expose
    private String userFullName;
    @SerializedName("unitSize")
    @Expose
    private String unitSize;
    @SerializedName("divisionName")
    @Expose
    private String divisionName;
    @SerializedName("appMenuTitle")
    @Expose
    private String appMenuTitle;
    @SerializedName("productStatus")
    @Expose
    private String productStatus;
    @SerializedName("maxUnitQty")
    @Expose
    private String maxUnitQty;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("avgWeightUnit")
    @Expose
    private String avgWeightUnit;
    @SerializedName("unitType")
    @Expose
    private String unitType;
    @SerializedName("productToken")
    @Expose
    private String productToken;
    @SerializedName("productPostType")
    @Expose
    private String productPostType;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("productPicture")
    @Expose
    private String productPicture;
    @SerializedName("productVideo")
    @Expose
    private String productVideo;
    @SerializedName("avgWeight")
    @Expose
    private String avgWeight;
    @SerializedName("productRating")
    @Expose
    private String productRating;
    @SerializedName("minUnitQty")
    @Expose
    private String minUnitQty;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUpazillaName() {
        return upazillaName;
    }

    public void setUpazillaName(String upazillaName) {
        this.upazillaName = upazillaName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUnitSize() {
        return unitSize;
    }

    public void setUnitSize(String unitSize) {
        this.unitSize = unitSize;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getAppMenuTitle() {
        return appMenuTitle;
    }

    public void setAppMenuTitle(String appMenuTitle) {
        this.appMenuTitle = appMenuTitle;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public String getMaxUnitQty() {
        return maxUnitQty;
    }

    public void setMaxUnitQty(String maxUnitQty) {
        this.maxUnitQty = maxUnitQty;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAvgWeightUnit() {
        return avgWeightUnit;
    }

    public void setAvgWeightUnit(String avgWeightUnit) {
        this.avgWeightUnit = avgWeightUnit;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getProductToken() {
        return productToken;
    }

    public void setProductToken(String productToken) {
        this.productToken = productToken;
    }

    public String getProductPostType() {
        return productPostType;
    }

    public void setProductPostType(String productPostType) {
        this.productPostType = productPostType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(String productPicture) {
        this.productPicture = productPicture;
    }

    public String getProductVideo() {
        return productVideo;
    }

    public void setProductVideo(String productVideo) {
        this.productVideo = productVideo;
    }

    public String getAvgWeight() {
        return avgWeight;
    }

    public void setAvgWeight(String avgWeight) {
        this.avgWeight = avgWeight;
    }

    public String getProductRating() {
        return productRating;
    }

    public void setProductRating(String productRating) {
        this.productRating = productRating;
    }

    public String getMinUnitQty() {
        return minUnitQty;
    }

    public void setMinUnitQty(String minUnitQty) {
        this.minUnitQty = minUnitQty;
    }

    @Override
    public String toString() {
        return "Get_OwnProduct{" +
                "productId='" + productId + '\'' +
                ", upazillaName='" + upazillaName + '\'' +
                ", districtName='" + districtName + '\'' +
                ", userFullName='" + userFullName + '\'' +
                ", unitSize='" + unitSize + '\'' +
                ", divisionName='" + divisionName + '\'' +
                ", appMenuTitle='" + appMenuTitle + '\'' +
                ", productStatus='" + productStatus + '\'' +
                ", maxUnitQty='" + maxUnitQty + '\'' +
                ", userId=" + userId +
                ", productName='" + productName + '\'' +
                ", avgWeightUnit='" + avgWeightUnit + '\'' +
                ", unitType='" + unitType + '\'' +
                ", productToken='" + productToken + '\'' +
                ", productPostType='" + productPostType + '\'' +
                ", price='" + price + '\'' +
                ", productPicture='" + productPicture + '\'' +
                ", productVideo='" + productVideo + '\'' +
                ", avgWeight='" + avgWeight + '\'' +
                ", productRating='" + productRating + '\'' +
                ", minUnitQty='" + minUnitQty + '\'' +
                '}';
    }
}