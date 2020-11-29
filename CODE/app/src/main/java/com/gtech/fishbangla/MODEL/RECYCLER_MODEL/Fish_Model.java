package com.gtech.fishbangla.MODEL.RECYCLER_MODEL;

public class Fish_Model {
    String id;
    String fish_image;
    String fish_name;
    String fish_category;
    String fish_ratting;
    String fish_price;
    String fish_minimum_sell;
    String fish_seller;

    public Fish_Model() {
    }

    public Fish_Model(String id, String fish_image, String fish_name, String fish_category, String fish_ratting, String fish_price, String fish_minimum_sell, String fish_seller) {
        this.id = id;
        this.fish_image = fish_image;
        this.fish_name = fish_name;
        this.fish_category = fish_category;
        this.fish_ratting = fish_ratting;
        this.fish_price = fish_price;
        this.fish_minimum_sell = fish_minimum_sell;
        this.fish_seller = fish_seller;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFish_image() {
        return fish_image;
    }

    public void setFish_image(String fish_image) {
        this.fish_image = fish_image;
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

    public String getFish_ratting() {
        return fish_ratting;
    }

    public void setFish_ratting(String fish_ratting) {
        this.fish_ratting = fish_ratting;
    }

    public String getFish_price() {
        return fish_price;
    }

    public void setFish_price(String fish_price) {
        this.fish_price = fish_price;
    }

    public String getFish_minimum_sell() {
        return fish_minimum_sell;
    }

    public void setFish_minimum_sell(String fish_minimum_sell) {
        this.fish_minimum_sell = fish_minimum_sell;
    }

    public String getFish_seller() {
        return fish_seller;
    }

    public void setFish_seller(String fish_seller) {
        this.fish_seller = fish_seller;
    }

    @Override
    public String toString() {
        return "Fish_Model{" +
                "id='" + id + '\'' +
                ", fish_image='" + fish_image + '\'' +
                ", fish_name='" + fish_name + '\'' +
                ", fish_category='" + fish_category + '\'' +
                ", fish_ratting='" + fish_ratting + '\'' +
                ", fish_price='" + fish_price + '\'' +
                ", fish_minimum_sell='" + fish_minimum_sell + '\'' +
                ", fish_seller='" + fish_seller + '\'' +
                '}';
    }
}
