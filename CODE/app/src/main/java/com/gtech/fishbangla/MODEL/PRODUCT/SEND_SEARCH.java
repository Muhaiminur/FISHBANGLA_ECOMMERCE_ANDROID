package com.gtech.fishbangla.MODEL.PRODUCT;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SEND_SEARCH {

    @SerializedName("productMenuId")
    @Expose
    private String productMenuId;

    public SEND_SEARCH(String productMenuId) {
        this.productMenuId = productMenuId;
    }

    public String getProductMenuId() {
        return productMenuId;
    }

    public void setProductMenuId(String productMenuId) {
        this.productMenuId = productMenuId;
    }

    @Override
    public String toString() {
        return "SEND_SEARCH{" +
                "productMenuId='" + productMenuId + '\'' +
                '}';
    }
}