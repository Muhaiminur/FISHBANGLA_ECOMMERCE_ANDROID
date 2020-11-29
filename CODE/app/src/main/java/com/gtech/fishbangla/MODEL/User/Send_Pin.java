package com.gtech.fishbangla.MODEL.User;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Send_Pin {

    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("userOtpPin")
    @Expose
    private String userOtpPin;

    public Send_Pin(String phone, String userOtpPin) {
        this.phone = phone;
        this.userOtpPin = userOtpPin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserOtpPin() {
        return userOtpPin;
    }

    public void setUserOtpPin(String userOtpPin) {
        this.userOtpPin = userOtpPin;
    }

    @Override
    public String toString() {
        return "Send_Pin{" +
                "phone='" + phone + '\'' +
                ", userOtpPin='" + userOtpPin + '\'' +
                '}';
    }
}