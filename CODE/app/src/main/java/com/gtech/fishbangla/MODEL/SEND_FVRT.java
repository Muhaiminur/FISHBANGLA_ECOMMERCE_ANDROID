package com.gtech.fishbangla.MODEL;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SEND_FVRT{

    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("isFavourite")
    @Expose
    private String isFavourite;

    public SEND_FVRT(String productId, String isFavourite) {
        this.productId = productId;
        this.isFavourite = isFavourite;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(String isFavourite) {
        this.isFavourite = isFavourite;
    }

    @Override
    public String toString() {
        return "SEND_FVRT{" +
                "productId='" + productId + '\'' +
                ", isFavourite='" + isFavourite + '\'' +
                '}';
    }
}