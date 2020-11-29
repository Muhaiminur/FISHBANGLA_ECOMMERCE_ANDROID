package com.gtech.fishbangla.MODEL.Banner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Banner_Data {
    @SerializedName("metadata")
    @Expose
    private String metadata;
    @SerializedName("bannerTitle")
    @Expose
    private String bannerTitle;
    @SerializedName("bannerId")
    @Expose
    private String bannerId;
    @SerializedName("bannerImageURL")
    @Expose
    private String bannerImageURL;
    @SerializedName("metadataBrowse")
    @Expose
    private String metadataBrowse;

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getBannerTitle() {
        return bannerTitle;
    }

    public void setBannerTitle(String bannerTitle) {
        this.bannerTitle = bannerTitle;
    }

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerImageURL() {
        return bannerImageURL;
    }

    public void setBannerImageURL(String bannerImageURL) {
        this.bannerImageURL = bannerImageURL;
    }

    public String getMetadataBrowse() {
        return metadataBrowse;
    }

    public void setMetadataBrowse(String metadataBrowse) {
        this.metadataBrowse = metadataBrowse;
    }
}