package com.gtech.fishbangla.MODEL.NOTIFICATION;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GET_NOTIFICATION {

    @SerializedName("readStatus")
    @Expose
    private String readStatus;
    @SerializedName("metadata")
    @Expose
    private String metadata;
    @SerializedName("notificationId")
    @Expose
    private String notificationId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("metadataBrowse")
    @Expose
    private String metadataBrowse;
    @SerializedName("url")
    @Expose
    private String url;

    public GET_NOTIFICATION() {
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMetadataBrowse() {
        return metadataBrowse;
    }

    public void setMetadataBrowse(String metadataBrowse) {
        this.metadataBrowse = metadataBrowse;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "GET_NOTIFICATION{" +
                "readStatus='" + readStatus + '\'' +
                ", metadata='" + metadata + '\'' +
                ", notificationId='" + notificationId + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", metadataBrowse='" + metadataBrowse + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}