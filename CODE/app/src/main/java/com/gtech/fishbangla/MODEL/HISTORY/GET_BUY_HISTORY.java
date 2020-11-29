package com.gtech.fishbangla.MODEL.HISTORY;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gtech.fishbangla.MODEL.PRODUCT.Get_Product;

import java.util.List;

public class GET_BUY_HISTORY {

    @SerializedName("oderDate")
    @Expose
    private String oderDate;
    @SerializedName("iceQty")
    @Expose
    private String iceQty;
    @SerializedName("icePrice")
    @Expose
    private String icePrice;
    @SerializedName("upazillaName")
    @Expose
    private String upazillaName;
    @SerializedName("districtName")
    @Expose
    private String districtName;
    @SerializedName("productVat")
    @Expose
    private String productVat;
    @SerializedName("userFullName")
    @Expose
    private String userFullName;
    @SerializedName("divisionName")
    @Expose
    private String divisionName;
    @SerializedName("orderStatus")
    @Expose
    private String orderStatus;
    @SerializedName("deliveryCharge")
    @Expose
    private String deliveryCharge;
    @SerializedName("discountAmount")
    @Expose
    private String discountAmount;
    @SerializedName("totalAfterDiscount")
    @Expose
    private String totalAfterDiscount;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("nearbyLandmark")
    @Expose
    private String nearbyLandmark;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("paymentType")
    @Expose
    private String paymentType;
    @SerializedName("products")
    @Expose
    private List<GET_BUY_PRODUCT> products = null;
    @SerializedName("userAddress")
    @Expose
    private String userAddress;
    @SerializedName("totalAmount")
    @Expose
    private String totalAmount;
    @SerializedName("serviceCharge")
    @Expose
    private String serviceCharge;
    @SerializedName("userVillage")
    @Expose
    private String userVillage;
    @SerializedName("subtotal")
    @Expose
    private String subtotal;
    @SerializedName("paymentStatus")
    @Expose
    private String paymentStatus;

    public String getOderDate() {
        return oderDate;
    }

    public void setOderDate(String oderDate) {
        this.oderDate = oderDate;
    }

    public String getIceQty() {
        return iceQty;
    }

    public void setIceQty(String iceQty) {
        this.iceQty = iceQty;
    }

    public String getIcePrice() {
        return icePrice;
    }

    public void setIcePrice(String icePrice) {
        this.icePrice = icePrice;
    }

    public String getUpazillaName() {
        return upazillaName;
    }

    public void setUpazillaName(String upazillaName) {
        this.upazillaName = upazillaName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getProductVat() {
        return productVat;
    }

    public void setProductVat(String productVat) {
        this.productVat = productVat;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getTotalAfterDiscount() {
        return totalAfterDiscount;
    }

    public void setTotalAfterDiscount(String totalAfterDiscount) {
        this.totalAfterDiscount = totalAfterDiscount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNearbyLandmark() {
        return nearbyLandmark;
    }

    public void setNearbyLandmark(String nearbyLandmark) {
        this.nearbyLandmark = nearbyLandmark;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public List<GET_BUY_PRODUCT> getProducts() {
        return products;
    }

    public void setProducts(List<GET_BUY_PRODUCT> products) {
        this.products = products;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getUserVillage() {
        return userVillage;
    }

    public void setUserVillage(String userVillage) {
        this.userVillage = userVillage;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "GET_BUY_HISTORY{" +
                "oderDate='" + oderDate + '\'' +
                ", iceQty='" + iceQty + '\'' +
                ", icePrice='" + icePrice + '\'' +
                ", upazillaName='" + upazillaName + '\'' +
                ", districtName='" + districtName + '\'' +
                ", productVat='" + productVat + '\'' +
                ", userFullName='" + userFullName + '\'' +
                ", divisionName='" + divisionName + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", deliveryCharge='" + deliveryCharge + '\'' +
                ", discountAmount='" + discountAmount + '\'' +
                ", totalAfterDiscount='" + totalAfterDiscount + '\'' +
                ", userId='" + userId + '\'' +
                ", nearbyLandmark='" + nearbyLandmark + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", products=" + products +
                ", userAddress='" + userAddress + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", serviceCharge='" + serviceCharge + '\'' +
                ", userVillage='" + userVillage + '\'' +
                ", subtotal='" + subtotal + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}