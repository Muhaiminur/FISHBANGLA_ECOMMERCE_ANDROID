package com.gtech.fishbangla.MODEL.FEEDBACK;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SEND_FEEDBACK{

    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("postRating")
    @Expose
    private String postRating;
    @SerializedName("postRemark")
    @Expose
    private String postRemark;

    public SEND_FEEDBACK() {
    }

    public SEND_FEEDBACK(String productId, String postRating, String postRemark) {
        this.productId = productId;
        this.postRating = postRating;
        this.postRemark = postRemark;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPostRating() {
        return postRating;
    }

    public void setPostRating(String postRating) {
        this.postRating = postRating;
    }

    public String getPostRemark() {
        return postRemark;
    }

    public void setPostRemark(String postRemark) {
        this.postRemark = postRemark;
    }

    @Override
    public String toString() {
        return "SEND_FEEDBACK{" +
                "productId='" + productId + '\'' +
                ", postRating='" + postRating + '\'' +
                ", postRemark='" + postRemark + '\'' +
                '}';
    }
}