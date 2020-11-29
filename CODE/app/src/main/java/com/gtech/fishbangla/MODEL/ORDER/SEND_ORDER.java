package com.gtech.fishbangla.MODEL.ORDER;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gtech.fishbangla.MODEL.ADDRESS.SEND_CART_ADDRESS;

import java.util.List;

public class SEND_ORDER {
    @SerializedName("userEmail")
    @Expose
    private String userEmail;
    @SerializedName("totalAmount")
    @Expose
    private String totalAmount;
    @SerializedName("serviceCharge")
    @Expose
    private String serviceCharge;
    @SerializedName("productVat")
    @Expose
    private String productVat;
    @SerializedName("deliveryCharge")
    @Expose
    private String deliveryCharge;
    @SerializedName("discountAmount")
    @Expose
    private String discountAmount;
    @SerializedName("totalAfterDiscount")
    @Expose
    private String totalAfterDiscount;
    @SerializedName("subtotal")
    @Expose
    private String subtotal;
    @SerializedName("discountId")
    @Expose
    private String discountId;
    @SerializedName("paymentTypeId")
    @Expose
    private String paymentTypeId;
    @SerializedName("iceQty")
    @Expose
    private String iceQty;
    @SerializedName("icePrice")
    @Expose
    private String icePrice;
    @SerializedName("products")
    @Expose
    private List<SEND_CART> products = null;
    @SerializedName("address")
    @Expose
    private SEND_CART_ADDRESS address;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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

    public String getProductVat() {
        return productVat;
    }

    public void setProductVat(String productVat) {
        this.productVat = productVat;
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

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(String paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
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

    public List<SEND_CART> getProducts() {
        return products;
    }

    public void setProducts(List<SEND_CART> products) {
        this.products = products;
    }

    public SEND_CART_ADDRESS getAddress() {
        return address;
    }

    public void setAddress(SEND_CART_ADDRESS address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SEND_ORDER{" +
                "userEmail='" + userEmail + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", serviceCharge='" + serviceCharge + '\'' +
                ", productVat='" + productVat + '\'' +
                ", deliveryCharge='" + deliveryCharge + '\'' +
                ", discountAmount='" + discountAmount + '\'' +
                ", totalAfterDiscount='" + totalAfterDiscount + '\'' +
                ", subtotal='" + subtotal + '\'' +
                ", discountId='" + discountId + '\'' +
                ", paymentTypeId='" + paymentTypeId + '\'' +
                ", iceQty='" + iceQty + '\'' +
                ", icePrice='" + icePrice + '\'' +
                ", products=" + products +
                ", address=" + address +
                '}';
    }
}
/*{

    @SerializedName("totalAmount")
    @Expose
    private String totalAmount;
    @SerializedName("serviceCharge")
    @Expose
    private String serviceCharge;
    @SerializedName("productVat")
    @Expose
    private String productVat;
    @SerializedName("deliveryCharge")
    @Expose
    private String deliveryCharge;
    @SerializedName("discountAmount")
    @Expose
    private String discountAmount;
    @SerializedName("totalAfterDiscount")
    @Expose
    private String totalAfterDiscount;
    @SerializedName("subtotal")
    @Expose
    private String subtotal;
    @SerializedName("discountId")
    @Expose
    private String discountId;
    @SerializedName("paymentTypeId")
    @Expose
    private String paymentTypeId;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("iceQty")
    @Expose
    private String iceQty;
    @SerializedName("icePrice")
    @Expose
    private String icePrice;
    @SerializedName("products")
    @Expose
    private List<SEND_CART> products = null;
    @SerializedName("address")
    @Expose
    private SEND_CART_ADDRESS address;

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

    public String getProductVat() {
        return productVat;
    }

    public void setProductVat(String productVat) {
        this.productVat = productVat;
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

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(String paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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

    public List<SEND_CART> getProducts() {
        return products;
    }

    public void setProducts(List<SEND_CART> products) {
        this.products = products;
    }

    public SEND_CART_ADDRESS getAddress() {
        return address;
    }

    public void setAddress(SEND_CART_ADDRESS address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SEND_ORDER{" +
                "totalAmount='" + totalAmount + '\'' +
                ", serviceCharge='" + serviceCharge + '\'' +
                ", productVat='" + productVat + '\'' +
                ", deliveryCharge='" + deliveryCharge + '\'' +
                ", discountAmount='" + discountAmount + '\'' +
                ", totalAfterDiscount='" + totalAfterDiscount + '\'' +
                ", subtotal='" + subtotal + '\'' +
                ", discountId='" + discountId + '\'' +
                ", paymentTypeId='" + paymentTypeId + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", iceQty='" + iceQty + '\'' +
                ", icePrice='" + icePrice + '\'' +
                ", products=" + products +
                ", address=" + address +
                '}';
    }
}*/