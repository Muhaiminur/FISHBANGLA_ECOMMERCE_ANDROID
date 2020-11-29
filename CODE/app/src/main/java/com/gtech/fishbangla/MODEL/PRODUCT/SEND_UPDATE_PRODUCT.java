package com.gtech.fishbangla.MODEL.PRODUCT;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SEND_UPDATE_PRODUCT{

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("productMenuId")
    @Expose
    private String productMenuId;
    @SerializedName("postTypeId")
    @Expose
    private String postTypeId;
    @SerializedName("upazillaId")
    @Expose
    private String upazillaId;
    @SerializedName("minUnitQty")
    @Expose
    private String minUnitQty;
    @SerializedName("maxUnitQty")
    @Expose
    private String maxUnitQty;
    @SerializedName("unitSize")
    @Expose
    private String unitSize;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("unitType")
    @Expose
    private String unitType;
    @SerializedName("avgWeight")
    @Expose
    private String avgWeight;
    @SerializedName("avgWeightUnit")
    @Expose
    private String avgWeightUnit;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductMenuId() {
        return productMenuId;
    }

    public void setProductMenuId(String productMenuId) {
        this.productMenuId = productMenuId;
    }

    public String getPostTypeId() {
        return postTypeId;
    }

    public void setPostTypeId(String postTypeId) {
        this.postTypeId = postTypeId;
    }

    public String getUpazillaId() {
        return upazillaId;
    }

    public void setUpazillaId(String upazillaId) {
        this.upazillaId = upazillaId;
    }

    public String getMinUnitQty() {
        return minUnitQty;
    }

    public void setMinUnitQty(String minUnitQty) {
        this.minUnitQty = minUnitQty;
    }

    public String getMaxUnitQty() {
        return maxUnitQty;
    }

    public void setMaxUnitQty(String maxUnitQty) {
        this.maxUnitQty = maxUnitQty;
    }

    public String getUnitSize() {
        return unitSize;
    }

    public void setUnitSize(String unitSize) {
        this.unitSize = unitSize;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getAvgWeight() {
        return avgWeight;
    }

    public void setAvgWeight(String avgWeight) {
        this.avgWeight = avgWeight;
    }

    public String getAvgWeightUnit() {
        return avgWeightUnit;
    }

    public void setAvgWeightUnit(String avgWeightUnit) {
        this.avgWeightUnit = avgWeightUnit;
    }

    @Override
    public String toString() {
        return "SEND_UPDATE_PRODUCT{" +
                "userId='" + userId + '\'' +
                ", productId='" + productId + '\'' +
                ", productMenuId='" + productMenuId + '\'' +
                ", postTypeId='" + postTypeId + '\'' +
                ", upazillaId='" + upazillaId + '\'' +
                ", minUnitQty='" + minUnitQty + '\'' +
                ", maxUnitQty='" + maxUnitQty + '\'' +
                ", unitSize='" + unitSize + '\'' +
                ", price='" + price + '\'' +
                ", unitType='" + unitType + '\'' +
                ", avgWeight='" + avgWeight + '\'' +
                ", avgWeightUnit='" + avgWeightUnit + '\'' +
                '}';
    }
}