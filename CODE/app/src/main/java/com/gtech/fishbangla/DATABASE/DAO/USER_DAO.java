package com.gtech.fishbangla.DATABASE.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gtech.fishbangla.DATABASE.TABLE.USER_PROFILE;

@Dao
public interface USER_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void user_insert(USER_PROFILE userProfile);

    @Query("DELETE FROM user_profile")
    void delete_user();

    @Query("SELECT * FROM user_profile")
    USER_PROFILE user_profile();

    @Query("UPDATE user_profile SET userimage=:new_image WHERE userid = :user_id")
    void update_User_Image(String user_id, String new_image);
}
