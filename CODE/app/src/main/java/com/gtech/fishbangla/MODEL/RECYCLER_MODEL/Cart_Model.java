package com.gtech.fishbangla.MODEL.RECYCLER_MODEL;

public class Cart_Model {
    String image;
    String fish_name;
    String fish_category;
    String fish_minimum_buy;
    String fish_seller;
    String fish_unit_price;
    String fish_total_price;
    String fish_unit;

    public Cart_Model(String image, String fish_name, String fish_category, String fish_minimum_buy, String fish_seller, String fish_unit_price, String fish_total_price, String fish_unit) {
        this.image = image;
        this.fish_name = fish_name;
        this.fish_category = fish_category;
        this.fish_minimum_buy = fish_minimum_buy;
        this.fish_seller = fish_seller;
        this.fish_unit_price = fish_unit_price;
        this.fish_total_price = fish_total_price;
        this.fish_unit = fish_unit;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFish_name() {
        return fish_name;
    }

    public void setFish_name(String fish_name) {
        this.fish_name = fish_name;
    }

    public String getFish_category() {
        return fish_category;
    }

    public void setFish_category(String fish_category) {
        this.fish_category = fish_category;
    }

    public String getFish_minimum_buy() {
        return fish_minimum_buy;
    }

    public void setFish_minimum_buy(String fish_minimum_buy) {
        this.fish_minimum_buy = fish_minimum_buy;
    }

    public String getFish_seller() {
        return fish_seller;
    }

    public void setFish_seller(String fish_seller) {
        this.fish_seller = fish_seller;
    }

    public String getFish_unit_price() {
        return fish_unit_price;
    }

    public void setFish_unit_price(String fish_unit_price) {
        this.fish_unit_price = fish_unit_price;
    }

    public String getFish_total_price() {
        return fish_total_price;
    }

    public void setFish_total_price(String fish_total_price) {
        this.fish_total_price = fish_total_price;
    }

    public String getFish_unit() {
        return fish_unit;
    }

    public void setFish_unit(String fish_unit) {
        this.fish_unit = fish_unit;
    }
}
