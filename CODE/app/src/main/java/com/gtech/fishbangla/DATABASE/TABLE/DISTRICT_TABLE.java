package com.gtech.fishbangla.DATABASE.TABLE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "district_table")
public class DISTRICT_TABLE {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "district_id")
    private String districtId;


    @NonNull
    @ColumnInfo(name = "district_name")
    private String districtName;

    @NonNull
    @ColumnInfo(name = "dis_div_id")
    private String divisionId;

    public DISTRICT_TABLE() {
    }

    @NonNull
    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(@NonNull String districtId) {
        this.districtId = districtId;
    }

    @NonNull
    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(@NonNull String districtName) {
        this.districtName = districtName;
    }

    @NonNull
    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(@NonNull String divisionId) {
        this.divisionId = divisionId;
    }

    @Override
    public String toString() {
        return districtName;
    }
}
