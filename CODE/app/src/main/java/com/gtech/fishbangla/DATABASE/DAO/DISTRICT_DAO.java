package com.gtech.fishbangla.DATABASE.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gtech.fishbangla.DATABASE.TABLE.DISTRICT_TABLE;

import java.util.List;

@Dao
public interface DISTRICT_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert_district(List<DISTRICT_TABLE> table);

    @Query("DELETE FROM district_table")
    void delete_district();

    @Query("SELECT * FROM district_table")
    List<DISTRICT_TABLE> get_district();

    @Query("SELECT * FROM district_table WHERE dis_div_id =:c")
    List<DISTRICT_TABLE> find_district(String c);
}
