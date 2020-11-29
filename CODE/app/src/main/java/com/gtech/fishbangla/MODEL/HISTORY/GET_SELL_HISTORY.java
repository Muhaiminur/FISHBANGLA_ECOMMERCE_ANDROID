package com.gtech.fishbangla.MODEL.HISTORY;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GET_SELL_HISTORY {

    @SerializedName("oderDate")
    @Expose
    private String oderDate;
    @SerializedName("totalAmount")
    @Expose
    private String totalAmount;
    @SerializedName("orderStatus")
    @Expose
    private String orderStatus;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("products")
    @Expose
    private List<GET_BUY_PRODUCT> products = null;

    public String getOderDate() {
        return oderDate;
    }

    public void setOderDate(String oderDate) {
        this.oderDate = oderDate;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public List<GET_BUY_PRODUCT> getProducts() {
        return products;
    }

    public void setProducts(List<GET_BUY_PRODUCT> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "GET_SELL_HISTORY{" +
                "oderDate='" + oderDate + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", products=" + products +
                '}';
    }
}