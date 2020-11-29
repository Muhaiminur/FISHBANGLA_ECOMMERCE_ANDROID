package com.gtech.fishbangla.MODEL.ADDRESS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SEND_CART_ADDRESS{

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

    @Override
    public String toString() {
        return "SEND_CART_ADDRESS{" +
                "upazillaId='" + upazillaId + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", roadNo='" + roadNo + '\'' +
                ", buildingNo='" + buildingNo + '\'' +
                ", flatNo='" + flatNo + '\'' +
                ", userVillage='" + userVillage + '\'' +
                '}';
    }
}