package com.gtech.fishbangla.MODEL;

public class Unit_Data {
    String name;
    String name_bn;

    public Unit_Data() {
    }

    public Unit_Data(String name, String name_bn) {
        this.name = name;
        this.name_bn = name_bn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_bn() {
        return name_bn;
    }

    public void setName_bn(String name_bn) {
        this.name_bn = name_bn;
    }

    @Override
    public String toString() {
        return name_bn;
    }
}
