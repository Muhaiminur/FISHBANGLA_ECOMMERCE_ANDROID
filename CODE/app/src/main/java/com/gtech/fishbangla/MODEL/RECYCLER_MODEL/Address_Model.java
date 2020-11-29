package com.gtech.fishbangla.MODEL.RECYCLER_MODEL;

public class Address_Model {
    String address_name;

    public Address_Model(String address_name) {
        this.address_name = address_name;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    @Override
    public String toString() {
        return "Address_Model{" +
                "address_name='" + address_name + '\'' +
                '}';
    }
}
