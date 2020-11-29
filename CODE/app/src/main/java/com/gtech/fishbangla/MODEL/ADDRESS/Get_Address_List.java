package com.gtech.fishbangla.MODEL.ADDRESS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gtech.fishbangla.DATABASE.TABLE.DISTRICT_TABLE;
import com.gtech.fishbangla.DATABASE.TABLE.DIVISION_TABLE;
import com.gtech.fishbangla.DATABASE.TABLE.UPAZILLA_TABLE;

import java.util.List;

public class Get_Address_List {

    @SerializedName("division")
    @Expose
    private List<DIVISION_TABLE> division = null;
    @SerializedName("district")
    @Expose
    private List<DISTRICT_TABLE> district = null;
    @SerializedName("upazilla")
    @Expose
    private List<UPAZILLA_TABLE> upazilla = null;

    public List<DIVISION_TABLE> getDivision() {
        return division;
    }

    public void setDivision(List<DIVISION_TABLE> division) {
        this.division = division;
    }

    public List<DISTRICT_TABLE> getDistrict() {
        return district;
    }

    public void setDistrict(List<DISTRICT_TABLE> district) {
        this.district = district;
    }

    public List<UPAZILLA_TABLE> getUpazilla() {
        return upazilla;
    }

    public void setUpazilla(List<UPAZILLA_TABLE> upazilla) {
        this.upazilla = upazilla;
    }

    @Override
    public String toString() {
        return "Get_Address_List{" +
                "division=" + division +
                ", district=" + district +
                ", upazilla=" + upazilla +
                '}';
    }
}