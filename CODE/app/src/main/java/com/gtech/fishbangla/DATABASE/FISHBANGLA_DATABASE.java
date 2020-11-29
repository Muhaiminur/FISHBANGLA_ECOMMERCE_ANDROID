package com.gtech.fishbangla.DATABASE;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.gtech.fishbangla.DATABASE.DAO.CART_DAO;
import com.gtech.fishbangla.DATABASE.DAO.CONFIG_DAO;
import com.gtech.fishbangla.DATABASE.DAO.DISTRICT_DAO;
import com.gtech.fishbangla.DATABASE.DAO.DIVISION_DAO;
import com.gtech.fishbangla.DATABASE.DAO.UPOZILLA_DAO;
import com.gtech.fishbangla.DATABASE.DAO.USER_DAO;
import com.gtech.fishbangla.DATABASE.TABLE.CART_TABLE;
import com.gtech.fishbangla.DATABASE.TABLE.CONFIG_TABLE;
import com.gtech.fishbangla.DATABASE.TABLE.DISTRICT_TABLE;
import com.gtech.fishbangla.DATABASE.TABLE.DIVISION_TABLE;
import com.gtech.fishbangla.DATABASE.TABLE.UPAZILLA_TABLE;
import com.gtech.fishbangla.DATABASE.TABLE.USER_PROFILE;

@Database(entities = {USER_PROFILE.class, CONFIG_TABLE.class, DIVISION_TABLE.class, DISTRICT_TABLE.class, UPAZILLA_TABLE.class, CART_TABLE.class}, version = 2, exportSchema = false)
public abstract class FISHBANGLA_DATABASE extends RoomDatabase {

    public abstract USER_DAO user_dao();

    public abstract CONFIG_DAO config_dao();

    public abstract DIVISION_DAO division_dao();

    public abstract DISTRICT_DAO district_dao();

    public abstract UPOZILLA_DAO upozilla_dao();

    public abstract CART_DAO cart_dao();

    private static FISHBANGLA_DATABASE INSTANCE;

    public static FISHBANGLA_DATABASE getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FISHBANGLA_DATABASE.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), FISHBANGLA_DATABASE.class, "fishbangla_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
