package com.gtech.fishbangla.MODEL.User;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GET_NEW_PAYMENTUSER{

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("gatewayPageUrl")
    @Expose
    private String gatewayPageUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGatewayPageUrl() {
        return gatewayPageUrl;
    }

    public void setGatewayPageUrl(String gatewayPageUrl) {
        this.gatewayPageUrl = gatewayPageUrl;
    }

    @Override
    public String toString() {
        return "GET_NEW_PAYMENTUSER{" +
                "userId='" + userId + '\'' +
                ", gatewayPageUrl='" + gatewayPageUrl + '\'' +
                '}';
    }
}