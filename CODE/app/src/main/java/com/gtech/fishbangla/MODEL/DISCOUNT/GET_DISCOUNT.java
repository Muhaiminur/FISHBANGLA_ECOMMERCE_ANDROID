package com.gtech.fishbangla.MODEL.DISCOUNT;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GET_DISCOUNT{

    @SerializedName("discountPercentage")
    @Expose
    private String discountPercentage;
    @SerializedName("discountMinAmount")
    @Expose
    private String discountMinAmount;
    @SerializedName("discountMaxAmount")
    @Expose
    private String discountMaxAmount;
    @SerializedName("discountId")
    @Expose
    private String discountId;

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDiscountMinAmount() {
        return discountMinAmount;
    }

    public void setDiscountMinAmount(String discountMinAmount) {
        this.discountMinAmount = discountMinAmount;
    }

    public String getDiscountMaxAmount() {
        return discountMaxAmount;
    }

    public void setDiscountMaxAmount(String discountMaxAmount) {
        this.discountMaxAmount = discountMaxAmount;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    @Override
    public String toString() {
        return "GET_DISCOUNT{" +
                "discountPercentage='" + discountPercentage + '\'' +
                ", discountMinAmount='" + discountMinAmount + '\'' +
                ", discountMaxAmount='" + discountMaxAmount + '\'' +
                ", discountId='" + discountId + '\'' +
                '}';
    }
}