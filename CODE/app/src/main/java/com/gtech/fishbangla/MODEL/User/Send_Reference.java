package com.gtech.fishbangla.MODEL.User;

public class Send_Reference {

    String phone;

    String userId;

    public Send_Reference(String phone, String userId) {
        this.phone = phone;
        this.userId = userId;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String value) {
        this.phone = value;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String value) {
        this.userId = value;
    }

    @Override
    public String toString() {
        return "Send_Reference{" +
                "phone='" + phone + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}