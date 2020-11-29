package com.gtech.fishbangla.MODEL.PRODUCT;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Send_Product_Category{

    @SerializedName("appMenuId")
    @Expose
    private String appMenuId;
    @SerializedName("postTypeId")
    @Expose
    private String postTypeId;

    public Send_Product_Category(String appMenuId, String postTypeId) {
        this.appMenuId = appMenuId;
        this.postTypeId = postTypeId;
    }

    public String getAppMenuId() {
        return appMenuId;
    }

    public void setAppMenuId(String appMenuId) {
        this.appMenuId = appMenuId;
    }

    public String getPostTypeId() {
        return postTypeId;
    }

    public void setPostTypeId(String postTypeId) {
        this.postTypeId = postTypeId;
    }

    @Override
    public String toString() {
        return "Send_Product_Category{" +
                "appMenuId='" + appMenuId + '\'' +
                ", postTypeId='" + postTypeId + '\'' +
                '}';
    }
}