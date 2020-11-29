package com.gtech.fishbangla.MODEL;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product_Data {

    @SerializedName("productMenuId")
    @Expose
    private Integer productMenuId;
    @SerializedName("productName")
    @Expose
    private String productName;

    public Product_Data() {
    }

    public Integer getProductMenuId() {
        return productMenuId;
    }

    public void setProductMenuId(Integer productMenuId) {
        this.productMenuId = productMenuId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return productName;
    }
}