package com.gtech.fishbangla.MODEL.RECYCLER_MODEL;

public class Seller_Model {
    String name;
    String ratting;

    public Seller_Model(String name, String ratting) {
        this.name = name;
        this.ratting = ratting;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRatting() {
        return ratting;
    }

    public void setRatting(String ratting) {
        this.ratting = ratting;
    }

    @Override
    public String toString() {
        return "Seller_Model{" +
                "name='" + name + '\'' +
                ", ratting='" + ratting + '\'' +
                '}';
    }
}
