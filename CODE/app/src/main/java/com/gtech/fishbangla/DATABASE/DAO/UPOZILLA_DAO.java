package com.gtech.fishbangla.DATABASE.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gtech.fishbangla.DATABASE.TABLE.UPAZILLA_TABLE;

import java.util.List;

@Dao
public interface UPOZILLA_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert_upazilla(List<UPAZILLA_TABLE> table);

    @Query("DELETE FROM upozilla_table")
    void delete_upazilla();

    @Query("SELECT * FROM upozilla_table")
    List<UPAZILLA_TABLE> get_upazilla();

    @Query("SELECT * FROM upozilla_table WHERE upa_dis_id =:c")
    List<UPAZILLA_TABLE> find_upazilla(String c);
}
