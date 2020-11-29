package com.gtech.fishbangla.MODEL.User;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Send_Pin_Change {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("oldPin")
    @Expose
    private String oldPin;
    @SerializedName("newPin")
    @Expose
    private String newPin;


    public Send_Pin_Change(String userId, String oldPin, String newPin) {
        this.userId = userId;
        this.oldPin = oldPin;
        this.newPin = newPin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOldPin() {
        return oldPin;
    }

    public void setOldPin(String oldPin) {
        this.oldPin = oldPin;
    }

    public String getNewPin() {
        return newPin;
    }

    public void setNewPin(String newPin) {
        this.newPin = newPin;
    }

    @Override
    public String toString() {
        return "Send_Pin_Change{" +
                "userId='" + userId + '\'' +
                ", oldPin='" + oldPin + '\'' +
                ", newPin='" + newPin + '\'' +
                '}';
    }
}