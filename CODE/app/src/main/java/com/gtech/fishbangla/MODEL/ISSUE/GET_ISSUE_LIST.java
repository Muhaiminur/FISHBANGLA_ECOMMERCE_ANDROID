package com.gtech.fishbangla.MODEL.ISSUE;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GET_ISSUE_LIST {

    @SerializedName("userIssueId")
    @Expose
    private String userIssueId;
    @SerializedName("userIssueTitle")
    @Expose
    private String userIssueTitle;

    public String getUserIssueId() {
        return userIssueId;
    }

    public void setUserIssueId(String userIssueId) {
        this.userIssueId = userIssueId;
    }

    public String getUserIssueTitle() {
        return userIssueTitle;
    }

    public void setUserIssueTitle(String userIssueTitle) {
        this.userIssueTitle = userIssueTitle;
    }

    @Override
    public String toString() {
        return userIssueTitle;
    }
}