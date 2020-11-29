package com.gtech.fishbangla.DATABASE.TABLE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_table")
public class CART_TABLE {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "cart_id")
    private String cart_Id;

    @ColumnInfo(name = "cart_url")
    private String cart_url;

    @NonNull
    @ColumnInfo(name = "cart_name")
    private String cart_name;

    @NonNull
    @ColumnInfo(name = "cart_minimum")
    private String cart_minimum;

    @NonNull
    @ColumnInfo(name = "cart_minimum_unit")
    private String cart_minimum_unit;

    @NonNull
    @ColumnInfo(name = "cart_seller")
    private String cart_seller;

    @NonNull
    @ColumnInfo(name = "cart_unit_price")
    private String cart_unit_price;

    @NonNull
    @ColumnInfo(name = "cart_unit_size")
    private String cart_unit_size;
    @NonNull
    @ColumnInfo(name = "cart_unit_akok")
    private String cart_unit_akok;

    @NonNull
    @ColumnInfo(name = "cart_unit_amount")
    private String cart_unit_amount;

    public CART_TABLE() {
    }

    public CART_TABLE(@NonNull String cart_Id, String cart_url, @NonNull String cart_name, @NonNull String cart_minimum, @NonNull String cart_minimum_unit, @NonNull String cart_seller, @NonNull String cart_unit_price, @NonNull String cart_unit_size, @NonNull String cart_unit_akok, @NonNull String cart_unit_amount) {
        this.cart_Id = cart_Id;
        this.cart_url = cart_url;
        this.cart_name = cart_name;
        this.cart_minimum = cart_minimum;
        this.cart_minimum_unit = cart_minimum_unit;
        this.cart_seller = cart_seller;
        this.cart_unit_price = cart_unit_price;
        this.cart_unit_size = cart_unit_size;
        this.cart_unit_akok = cart_unit_akok;
        this.cart_unit_amount = cart_unit_amount;
    }

    @NonNull
    public String getCart_Id() {
        return cart_Id;
    }

    public void setCart_Id(@NonNull String cart_Id) {
        this.cart_Id = cart_Id;
    }

    public String getCart_url() {
        return cart_url;
    }

    public void setCart_url(String cart_url) {
        this.cart_url = cart_url;
    }

    @NonNull
    public String getCart_name() {
        return cart_name;
    }

    public void setCart_name(@NonNull String cart_name) {
        this.cart_name = cart_name;
    }

    @NonNull
    public String getCart_minimum() {
        return cart_minimum;
    }

    public void setCart_minimum(@NonNull String cart_minimum) {
        this.cart_minimum = cart_minimum;
    }

    @NonNull
    public String getCart_minimum_unit() {
        return cart_minimum_unit;
    }

    public void setCart_minimum_unit(@NonNull String cart_minimum_unit) {
        this.cart_minimum_unit = cart_minimum_unit;
    }

    @NonNull
    public String getCart_seller() {
        return cart_seller;
    }

    public void setCart_seller(@NonNull String cart_seller) {
        this.cart_seller = cart_seller;
    }

    @NonNull
    public String getCart_unit_price() {
        return cart_unit_price;
    }

    public void setCart_unit_price(@NonNull String cart_unit_price) {
        this.cart_unit_price = cart_unit_price;
    }

    @NonNull
    public String getCart_unit_size() {
        return cart_unit_size;
    }

    public void setCart_unit_size(@NonNull String cart_unit_size) {
        this.cart_unit_size = cart_unit_size;
    }

    @NonNull
    public String getCart_unit_akok() {
        return cart_unit_akok;
    }

    public void setCart_unit_akok(@NonNull String cart_unit_akok) {
        this.cart_unit_akok = cart_unit_akok;
    }

    @NonNull
    public String getCart_unit_amount() {
        return cart_unit_amount;
    }

    public void setCart_unit_amount(@NonNull String cart_unit_amount) {
        this.cart_unit_amount = cart_unit_amount;
    }

    @Override
    public String toString() {
        return "CART_TABLE{" +
                "cart_Id='" + cart_Id + '\'' +
                ", cart_url='" + cart_url + '\'' +
                ", cart_name='" + cart_name + '\'' +
                ", cart_minimum='" + cart_minimum + '\'' +
                ", cart_minimum_unit='" + cart_minimum_unit + '\'' +
                ", cart_seller='" + cart_seller + '\'' +
                ", cart_unit_price='" + cart_unit_price + '\'' +
                ", cart_unit_size='" + cart_unit_size + '\'' +
                ", cart_unit_akok='" + cart_unit_akok + '\'' +
                ", cart_unit_amount='" + cart_unit_amount + '\'' +
                '}';
    }
}
