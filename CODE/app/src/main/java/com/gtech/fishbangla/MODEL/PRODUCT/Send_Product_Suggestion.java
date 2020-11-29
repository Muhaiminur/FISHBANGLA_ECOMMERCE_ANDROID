package com.gtech.fishbangla.MODEL.PRODUCT;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Send_Product_Suggestion{

    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("productPostType")
    @Expose
    private String productPostType;


    public Send_Product_Suggestion() {
    }

    public Send_Product_Suggestion(String productId, String productPostType) {
        this.productId = productId;
        this.productPostType = productPostType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductPostType() {
        return productPostType;
    }

    public void setProductPostType(String productPostType) {
        this.productPostType = productPostType;
    }

    @Override
    public String toString() {
        return "Send_Product_Suggestion{" +
                "productId='" + productId + '\'' +
                ", productPostType='" + productPostType + '\'' +
                '}';
    }
}