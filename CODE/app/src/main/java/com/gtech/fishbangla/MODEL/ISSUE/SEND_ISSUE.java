package com.gtech.fishbangla.MODEL.ISSUE;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SEND_ISSUE {
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("userIssueId")
    @Expose
    private String userIssueId;
    @SerializedName("userMessage")
    @Expose
    private String userMessage;

    public SEND_ISSUE(String userId, String userIssueId, String userMessage) {
        this.userId = userId;
        this.userIssueId = userIssueId;
        this.userMessage = userMessage;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserIssueId() {
        return userIssueId;
    }

    public void setUserIssueId(String userIssueId) {
        this.userIssueId = userIssueId;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    @Override
    public String toString() {
        return "SEND_ISSUE{" +
                "userId='" + userId + '\'' +
                ", userIssueId='" + userIssueId + '\'' +
                ", userMessage='" + userMessage + '\'' +
                '}';
    }
}
