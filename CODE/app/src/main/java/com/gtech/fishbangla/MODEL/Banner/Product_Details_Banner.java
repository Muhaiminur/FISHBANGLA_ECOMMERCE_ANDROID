package com.gtech.fishbangla.MODEL.Banner;

public class Product_Details_Banner {
    String media_url;
    String media_type;

    public Product_Details_Banner() {
    }

    public Product_Details_Banner(String media_url, String media_type) {
        this.media_url = media_url;
        this.media_type = media_type;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    @Override
    public String toString() {
        return "Product_Details_Banner{" +
                "media_url='" + media_url + '\'' +
                ", media_type='" + media_type + '\'' +
                '}';
    }
}
