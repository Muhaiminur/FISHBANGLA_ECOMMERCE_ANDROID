package com.gtech.fishbangla.MODEL;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Send_UserID {
    @SerializedName("userId")
    @Expose
    private String userId;

    public Send_UserID(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Send_UserID{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
