package com.gtech.fishbangla.MODEL.HISTORY;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GET_BUY_PRODUCT {
    @SerializedName("unitType")
    @Expose
    private String unitType;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("unitSize")
    @Expose
    private String unitSize;
    @SerializedName("productName")
    @Expose
    private String productName;



    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnitSize() {
        return unitSize;
    }

    public void setUnitSize(String unitSize) {
        this.unitSize = unitSize;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "GET_BUY_PRODUCT{" +
                "unitType='" + unitType + '\'' +
                ", price='" + price + '\'' +
                ", unitSize='" + unitSize + '\'' +
                ", productName='" + productName + '\'' +
                '}';
    }
}