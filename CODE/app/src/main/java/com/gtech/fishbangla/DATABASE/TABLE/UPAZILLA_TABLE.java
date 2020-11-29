package com.gtech.fishbangla.DATABASE.TABLE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "upozilla_table")
public class UPAZILLA_TABLE {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "upazilla_id")
    private String upazillaId;


    @NonNull
    @ColumnInfo(name = "upazilla_name")
    private String upazillaName;

    @NonNull
    @ColumnInfo(name = "upa_dis_id")
    private String districtId;

    public UPAZILLA_TABLE() {
    }

    @NonNull
    public String getUpazillaId() {
        return upazillaId;
    }

    public void setUpazillaId(@NonNull String upazillaId) {
        this.upazillaId = upazillaId;
    }

    @NonNull
    public String getUpazillaName() {
        return upazillaName;
    }

    public void setUpazillaName(@NonNull String upazillaName) {
        this.upazillaName = upazillaName;
    }

    @NonNull
    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(@NonNull String districtId) {
        this.districtId = districtId;
    }

    @Override
    public String toString() {
        return upazillaName;
    }
}
