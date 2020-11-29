package com.gtech.fishbangla.DATABASE.TABLE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "division_table")
public class DIVISION_TABLE {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "division_id")
    private String divisionId;


    @NonNull
    @ColumnInfo(name = "division_name")
    private String divisionName;


    public DIVISION_TABLE() {
    }

    @NonNull
    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(@NonNull String divisionName) {
        this.divisionName = divisionName;
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
        return divisionName;
    }
}
