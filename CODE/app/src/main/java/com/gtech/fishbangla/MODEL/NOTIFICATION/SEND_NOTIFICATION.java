package com.gtech.fishbangla.MODEL.NOTIFICATION;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SEND_NOTIFICATION {

    @SerializedName("notificationId")
    @Expose
    private String notificationId;
    @SerializedName("updateStatus")
    @Expose
    private String updateStatus;

    public SEND_NOTIFICATION(String notificationId, String updateStatus) {
        this.notificationId = notificationId;
        this.updateStatus = updateStatus;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    @Override
    public String toString() {
        return "SEND_NOTIFICATION{" +
                "notificationId='" + notificationId + '\'' +
                ", updateStatus='" + updateStatus + '\'' +
                '}';
    }
}