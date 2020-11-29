package com.gtech.fishbangla.MODEL.PRODUCT;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Send_Product_New {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("productMenuId")
    @Expose
    private String productMenuId;
    @SerializedName("postTypeId")
    @Expose
    private String postTypeId;
    @SerializedName("avgWeight")
    @Expose
    private String avgWeight;
    @SerializedName("avgWeightUnit")
    @Expose
    private String avgWeightUnit;
    @SerializedName("unitType")
    @Expose
    private String unitType;
    @SerializedName("unitSize")
    @Expose
    private String unitSize;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("minUnitQty")
    @Expose
    private String minUnitQty;
    @SerializedName("maxUnitQty")
    @Expose
    private String maxUnitQty;
    @SerializedName("upazillaId")
    @Expose
    private String upazillaId;

    public Send_Product_New() {
    }

    public Send_Product_New(String userId, String productMenuId, String postTypeId, String avgWeight, String avgWeightUnit, String unitType, String unitSize, String price, String minUnitQty, String maxUnitQty, String upazillaId) {
        this.userId = userId;
        this.productMenuId = productMenuId;
        this.postTypeId = postTypeId;
        this.avgWeight = avgWeight;
        this.avgWeightUnit = avgWeightUnit;
        this.unitType = unitType;
        this.unitSize = unitSize;
        this.price = price;
        this.minUnitQty = minUnitQty;
        this.maxUnitQty = maxUnitQty;
        this.upazillaId = upazillaId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
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

    public String getUpazillaId() {
        return upazillaId;
    }

    public void setUpazillaId(String upazillaId) {
        this.upazillaId = upazillaId;
    }

    @Override
    public String toString() {
        return "Send_Product_New{" +
                "userId='" + userId + '\'' +
                ", productMenuId='" + productMenuId + '\'' +
                ", postTypeId='" + postTypeId + '\'' +
                ", avgWeight='" + avgWeight + '\'' +
                ", avgWeightUnit='" + avgWeightUnit + '\'' +
                ", unitType='" + unitType + '\'' +
                ", unitSize='" + unitSize + '\'' +
                ", price='" + price + '\'' +
                ", minUnitQty='" + minUnitQty + '\'' +
                ", maxUnitQty='" + maxUnitQty + '\'' +
                ", upazillaId='" + upazillaId + '\'' +
                '}';
    }
}