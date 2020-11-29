package com.gtech.fishbangla.MODEL.PRODUCT;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SEND_PRODUCT_STATUS {

    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("productToken")
    @Expose
    private String productToken;
    @SerializedName("userId")
    @Expose
    private String userId;

    public SEND_PRODUCT_STATUS(String productId, String productToken, String userId) {
        this.productId = productId;
        this.productToken = productToken;
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductToken() {
        return productToken;
    }

    public void setProductToken(String productToken) {
        this.productToken = productToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SEND_PRODUCT_STATUS{" +
                "productId='" + productId + '\'' +
                ", productToken='" + productToken + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}