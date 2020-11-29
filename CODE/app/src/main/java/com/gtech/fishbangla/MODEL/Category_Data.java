package com.gtech.fishbangla.MODEL;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category_Data {

    @SerializedName("appMenuId")
    @Expose
    private String appMenuId;
    @SerializedName("appMenuIsCatg")
    @Expose
    private String appMenuIsCatg;
    @SerializedName("appMenuTitle")
    @Expose
    private String appMenuTitle;
    @SerializedName("appMenuPicture")
    @Expose
    private String appMenuPicture;
    @SerializedName("appMenuMetadata")
    @Expose
    private String appMenuMetadata;

    public String getAppMenuId() {
        return appMenuId;
    }

    public void setAppMenuId(String appMenuId) {
        this.appMenuId = appMenuId;
    }

    public String getAppMenuIsCatg() {
        return appMenuIsCatg;
    }

    public void setAppMenuIsCatg(String appMenuIsCatg) {
        this.appMenuIsCatg = appMenuIsCatg;
    }

    public String getAppMenuTitle() {
        return appMenuTitle;
    }

    public void setAppMenuTitle(String appMenuTitle) {
        this.appMenuTitle = appMenuTitle;
    }

    public String getAppMenuPicture() {
        return appMenuPicture;
    }

    public void setAppMenuPicture(String appMenuPicture) {
        this.appMenuPicture = appMenuPicture;
    }

    public String getAppMenuMetadata() {
        return appMenuMetadata;
    }

    public void setAppMenuMetadata(String appMenuMetadata) {
        this.appMenuMetadata = appMenuMetadata;
    }

    @Override
    public String toString() {
        return appMenuTitle;
    }
}