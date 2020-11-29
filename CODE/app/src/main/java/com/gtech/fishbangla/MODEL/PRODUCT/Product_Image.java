package com.gtech.fishbangla.MODEL.PRODUCT;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product_Image{

    @SerializedName("image")
    @Expose
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Product_Image{" +
                "image='" + image + '\'' +
                '}';
    }
}