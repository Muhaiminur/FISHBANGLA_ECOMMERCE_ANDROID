package com.gtech.fishbangla.MODEL.FEEDBACK;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feedback{

    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("productName")
    @Expose
    private String productName;

    public Feedback(String productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }

    public Feedback() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                '}';
    }
}