package com.gtech.fishbangla.DATABASE.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gtech.fishbangla.DATABASE.TABLE.DIVISION_TABLE;

import java.util.List;

@Dao
public interface DIVISION_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert_division(List<DIVISION_TABLE> table);

    @Query("DELETE FROM division_table")
    void delete_division();

    @Query("SELECT * FROM division_table")
    List<DIVISION_TABLE> get_division();
}
