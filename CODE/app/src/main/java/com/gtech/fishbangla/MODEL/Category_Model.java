package com.gtech.fishbangla.MODEL;

public class Category_Model {
    String id;
    String category_name;
    String category_image;

    public Category_Model(String id, String category_name, String category_image) {
        this.id = id;
        this.category_name = category_name;
        this.category_image = category_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    @Override
    public String toString() {
        return "Category_Model{" +
                "id='" + id + '\'' +
                ", category_name='" + category_name + '\'' +
                ", category_image='" + category_image + '\'' +
                '}';
    }
}
