package com.gtech.fishbangla.MODEL.CREATE_PRODUCT;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Send_CategoryID{

    @SerializedName("appMenuId")
    @Expose
    private String appMenuId;

    public Send_CategoryID(String appMenuId) {
        this.appMenuId = appMenuId;
    }

    public String getAppMenuId() {
        return appMenuId;
    }

    public void setAppMenuId(String appMenuId) {
        this.appMenuId = appMenuId;
    }

    @Override
    public String toString() {
        return "Send_CategoryID{" +
                "appMenuId='" + appMenuId + '\'' +
                '}';
    }
}