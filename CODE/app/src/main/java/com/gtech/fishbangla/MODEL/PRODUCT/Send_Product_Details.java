package com.gtech.fishbangla.MODEL.PRODUCT;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Send_Product_Details {


    @SerializedName("productId")
    @Expose
    private String productId;

    public Send_Product_Details(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "Send_Product_Details{" +
                "productId='" + productId + '\'' +
                '}';
    }
}