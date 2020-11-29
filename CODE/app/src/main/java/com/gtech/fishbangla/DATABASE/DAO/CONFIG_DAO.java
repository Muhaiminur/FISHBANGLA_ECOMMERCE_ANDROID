package com.gtech.fishbangla.DATABASE.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gtech.fishbangla.DATABASE.TABLE.CONFIG_TABLE;

@Dao
public interface CONFIG_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert_config(CONFIG_TABLE table);

    @Query("DELETE FROM config_info")
    void delete_config();

    @Query("SELECT * FROM config_info")
    CONFIG_TABLE get_config();
}
