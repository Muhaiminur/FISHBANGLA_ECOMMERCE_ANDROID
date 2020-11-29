package com.gtech.fishbangla.MODEL.PRODUCT;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SEND_FILTER_PRODUCT {

    @SerializedName("minPriceRange")
    @Expose
    private String minPriceRange;
    @SerializedName("maxPriceRange")
    @Expose
    private String maxPriceRange;
    @SerializedName("minAvgWeight")
    @Expose
    private String minAvgWeight;
    @SerializedName("maxAvgWeight")
    @Expose
    private String maxAvgWeight;
    @SerializedName("productMenuId")
    @Expose
    private String productMenuId;
    @SerializedName("appMenuId")
    @Expose
    private String appMenuId;
    @SerializedName("divisionId")
    @Expose
    private String divisionId;
    @SerializedName("postTypeId")
    @Expose
    private String postTypeId;

    public String getMinPriceRange() {
        return minPriceRange;
    }

    public void setMinPriceRange(String minPriceRange) {
        this.minPriceRange = minPriceRange;
    }

    public String getMaxPriceRange() {
        return maxPriceRange;
    }

    public void setMaxPriceRange(String maxPriceRange) {
        this.maxPriceRange = maxPriceRange;
    }

    public String getMinAvgWeight() {
        return minAvgWeight;
    }

    public void setMinAvgWeight(String minAvgWeight) {
        this.minAvgWeight = minAvgWeight;
    }

    public String getMaxAvgWeight() {
        return maxAvgWeight;
    }

    public void setMaxAvgWeight(String maxAvgWeight) {
        this.maxAvgWeight = maxAvgWeight;
    }

    public String getProductMenuId() {
        return productMenuId;
    }

    public void setProductMenuId(String productMenuId) {
        this.productMenuId = productMenuId;
    }

    public String getAppMenuId() {
        return appMenuId;
    }

    public void setAppMenuId(String appMenuId) {
        this.appMenuId = appMenuId;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getPostTypeId() {
        return postTypeId;
    }

    public void setPostTypeId(String postTypeId) {
        this.postTypeId = postTypeId;
    }

    @Override
    public String toString() {
        return "SEND_FILTER_PRODUCT{" +
                "minPriceRange='" + minPriceRange + '\'' +
                ", maxPriceRange='" + maxPriceRange + '\'' +
                ", minAvgWeight='" + minAvgWeight + '\'' +
                ", maxAvgWeight='" + maxAvgWeight + '\'' +
                ", productMenuId='" + productMenuId + '\'' +
                ", appMenuId='" + appMenuId + '\'' +
                ", divisionId='" + divisionId + '\'' +
                ", postTypeId='" + postTypeId + '\'' +
                '}';
    }
}