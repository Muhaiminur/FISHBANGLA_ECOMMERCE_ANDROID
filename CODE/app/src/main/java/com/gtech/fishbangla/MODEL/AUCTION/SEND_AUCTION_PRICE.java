package com.gtech.fishbangla.MODEL.AUCTION;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SEND_AUCTION_PRICE{

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("bidAmount")
    @Expose
    private String bidAmount;

    public SEND_AUCTION_PRICE(String userId, String productId, String bidAmount) {
        this.userId = userId;
        this.productId = productId;
        this.bidAmount = bidAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(String bidAmount) {
        this.bidAmount = bidAmount;
    }

    @Override
    public String toString() {
        return "SEND_AUCTION_PRICE{" +
                "userId='" + userId + '\'' +
                ", productId='" + productId + '\'' +
                ", bidAmount='" + bidAmount + '\'' +
                '}';
    }
}