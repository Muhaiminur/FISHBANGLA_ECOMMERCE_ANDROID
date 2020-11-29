package com.gtech.fishbangla.DATABASE.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gtech.fishbangla.DATABASE.TABLE.CART_TABLE;

import java.util.List;

@Dao
public interface CART_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert_cart(CART_TABLE cart_table);

    @Query("DELETE FROM cart_table WHERE cart_id = :product_id")
    void delete_cart_single(String product_id);

    @Query("DELETE FROM cart_table")
    void delete_cart_all();

    @Query("SELECT * from cart_table ORDER BY cart_id ASC")
    LiveData<List<CART_TABLE>> get_all_cart();

    @Query("SELECT * FROM cart_table WHERE cart_id =:c")
    CART_TABLE find_cart_single(String c);

    @Query("UPDATE cart_table SET cart_unit_amount=:new_amount WHERE cart_id = :cart_id")
    void update_cart_amount(String cart_id, String new_amount);
}
