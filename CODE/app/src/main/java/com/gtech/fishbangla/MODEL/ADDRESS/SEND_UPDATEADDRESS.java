package com.gtech.fishbangla.MODEL.ADDRESS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SEND_UPDATEADDRESS {

    @SerializedName("addressId")
    @Expose
    private String addressId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("upazillaId")
    @Expose
    private String upazillaId;
    @SerializedName("receiverName")
    @Expose
    private String receiverName;
    @SerializedName("receiverPhone")
    @Expose
    private String receiverPhone;
    @SerializedName("userAddress")
    @Expose
    private String userAddress;
    @SerializedName("roadNo")
    @Expose
    private String roadNo;
    @SerializedName("buildingNo")
    @Expose
    private String buildingNo;
    @SerializedName("flatNo")
    @Expose
    private String flatNo;
    @SerializedName("userVillage")
    @Expose
    private String userVillage;
    @SerializedName("isPrimary")
    @Expose
    private String isPrimary;

    public SEND_UPDATEADDRESS() {
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUpazillaId() {
        return upazillaId;
    }

    public void setUpazillaId(String upazillaId) {
        this.upazillaId = upazillaId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getRoadNo() {
        return roadNo;
    }

    public void setRoadNo(String roadNo) {
        this.roadNo = roadNo;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getUserVillage() {
        return userVillage;
    }

    public void setUserVillage(String userVillage) {
        this.userVillage = userVillage;
    }

    public String getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(String isPrimary) {
        this.isPrimary = isPrimary;
    }

    @Override
    public String toString() {
        return "SEND_UPDATEADDRESS{" +
                "addressId='" + addressId + '\'' +
                ", userId='" + userId + '\'' +
                ", upazillaId='" + upazillaId + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", roadNo='" + roadNo + '\'' +
                ", buildingNo='" + buildingNo + '\'' +
                ", flatNo='" + flatNo + '\'' +
                ", userVillage='" + userVillage + '\'' +
                ", isPrimary='" + isPrimary + '\'' +
                '}';
    }
}