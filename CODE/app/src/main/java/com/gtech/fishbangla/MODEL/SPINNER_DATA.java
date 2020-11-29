package com.gtech.fishbangla.MODEL;

public class SPINNER_DATA {
    String name;
    String id;

    public SPINNER_DATA() {
    }

    public SPINNER_DATA(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}
