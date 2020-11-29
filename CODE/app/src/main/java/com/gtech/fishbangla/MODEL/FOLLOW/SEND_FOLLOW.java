package com.gtech.fishbangla.MODEL.FOLLOW;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SEND_FOLLOW {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("followerId")
    @Expose
    private String followerId;

    public SEND_FOLLOW(String userId, String followerId) {
        this.userId = userId;
        this.followerId = followerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    @Override
    public String toString() {
        return "SEND_FOLLOW{" +
                "userId='" + userId + '\'' +
                ", followerId='" + followerId + '\'' +
                '}';
    }
}