package com.gtech.fishbangla.MODEL.ORDER;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SEND_CART {

    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("orderQuantity")
    @Expose
    private String orderQuantity;

    public SEND_CART(String productId, String orderQuantity) {
        this.productId = productId;
        this.orderQuantity = orderQuantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    @Override
    public String toString() {
        return "SEND_CART{" +
                "productId='" + productId + '\'' +
                ", orderQuantity='" + orderQuantity + '\'' +
                '}';
    }
}