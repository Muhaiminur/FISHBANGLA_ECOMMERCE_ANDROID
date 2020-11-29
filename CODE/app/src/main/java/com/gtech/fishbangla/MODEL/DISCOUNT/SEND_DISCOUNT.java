package com.gtech.fishbangla.MODEL.DISCOUNT;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SEND_DISCOUNT {
    @SerializedName("discountKeyword")
    @Expose
    private String discountKeyword;
    @SerializedName("userId")
    @Expose
    private String userId;

    public String getDiscountKeyword() {
        return discountKeyword;
    }

    public void setDiscountKeyword(String discountKeyword) {
        this.discountKeyword = discountKeyword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SEND_DISCOUNT{" +
                "discountKeyword='" + discountKeyword + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
