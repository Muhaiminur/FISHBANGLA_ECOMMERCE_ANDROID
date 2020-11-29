package com.gtech.fishbangla.DATABASE;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gtech.fishbangla.DATABASE.REPOSITORY.CART_REPOSITORY;
import com.gtech.fishbangla.DATABASE.TABLE.CART_TABLE;

import java.util.List;

public class CARTDATABASE_VIEWMODEL extends AndroidViewModel {
    public CART_REPOSITORY cart_repository;
    LiveData<List<CART_TABLE>> cart_tableLiveData;

    public CARTDATABASE_VIEWMODEL(@NonNull Application application) {
        super(application);
        cart_repository = new CART_REPOSITORY(application);
    }

    public LiveData<List<CART_TABLE>> getCart_tableLiveData() {
        try {
            cart_tableLiveData = cart_repository.get_all_Cart();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
        return cart_tableLiveData;
    }

    public void insert_single_cart_data(CART_TABLE cart_table) {
        cart_repository.insert_single_cart_data(cart_table);
    }

    public void delete_cart_data(String id) {
        cart_repository.delete_cart_data(id);
    }

    public void deleteall_cart_data() {
        cart_repository.deleteall_cart_data();
    }

    public void find_single_Cart(String id) {
        try {
            cart_repository.find_single_Cart(id);
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    public void update_cart_data(String id, String amount) {
        cart_repository.update_cart_data(id, amount);
    }
}
