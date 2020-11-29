package com.gtech.fishbangla.MODEL.ADDRESS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SEND_ADDRESSID{

    @SerializedName("addressId")
    @Expose
    private String addressId;

    public SEND_ADDRESSID(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    @Override
    public String toString() {
        return "SEND_ADDRESSID{" +
                "addressId='" + addressId + '\'' +
                '}';
    }
}