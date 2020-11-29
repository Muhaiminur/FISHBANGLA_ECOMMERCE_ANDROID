package com.gtech.fishbangla.MODEL.RECYCLER_MODEL;

public class History_Model {
    String name;
    String price;

    public History_Model(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "History_Model{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
