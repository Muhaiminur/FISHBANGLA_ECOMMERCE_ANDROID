package com.gtech.fishbangla.MODEL.User;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Send_Mbl {

    @SerializedName("phone")
    @Expose
    private String phone;

    public Send_Mbl(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Send_Mbl{" +
                "phone='" + phone + '\'' +
                '}';
    }
}