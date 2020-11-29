package com.gtech.fishbangla.DATABASE.REPOSITORY;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.gtech.fishbangla.DATABASE.DAO.CART_DAO;
import com.gtech.fishbangla.DATABASE.FISHBANGLA_DATABASE;
import com.gtech.fishbangla.DATABASE.TABLE.CART_TABLE;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CART_REPOSITORY {

    CART_DAO cart_dao;

    public CART_REPOSITORY(Application application) {
        FISHBANGLA_DATABASE db = FISHBANGLA_DATABASE.getDatabase(application);
        cart_dao = db.cart_dao();
    }

    public void insert_single_cart_data(CART_TABLE cartTable) {
        new insert_single_AsyncTask(cart_dao).execute(cartTable);
    }

    private static class insert_single_AsyncTask extends AsyncTask<CART_TABLE, Void, Void> {
        private CART_DAO mAsyncTaskDao;

        insert_single_AsyncTask(CART_DAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CART_TABLE... params) {
            mAsyncTaskDao.insert_cart(params[0]);
            return null;
        }
    }

    public void delete_cart_data(String id) {
        new delete_single_AsyncTask(cart_dao, id).execute("");
    }

    private static class delete_single_AsyncTask extends AsyncTask<String, Void, Void> {
        private CART_DAO mAsyncTaskDao;
        String product_id;

        delete_single_AsyncTask(CART_DAO dao, String i) {
            mAsyncTaskDao = dao;
            product_id = i;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.delete_cart_single(product_id);
            return null;
        }
    }

    public void deleteall_cart_data() {
        new deleteall_single_AsyncTask(cart_dao).execute("");
    }

    private static class deleteall_single_AsyncTask extends AsyncTask<String, Void, Void> {
        private CART_DAO mAsyncTaskDao;

        deleteall_single_AsyncTask(CART_DAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.delete_cart_all();
            return null;
        }
    }

    public LiveData<List<CART_TABLE>> get_all_Cart() throws ExecutionException, InterruptedException {
        Callable<LiveData<List<CART_TABLE>>> callable = new Callable<LiveData<List<CART_TABLE>>>() {
            @Override
            public LiveData<List<CART_TABLE>> call() throws Exception {
                return cart_dao.get_all_cart();
            }
        };
        Future<LiveData<List<CART_TABLE>>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public CART_TABLE find_single_Cart(String id) throws ExecutionException, InterruptedException {

        Callable<CART_TABLE> callable = new Callable<CART_TABLE>() {
            @Override
            public CART_TABLE call() throws Exception {
                return cart_dao.find_cart_single(id);
            }
        };

        Future<CART_TABLE> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }

    public void update_cart_data(String id, String amount) {
        new update_cart_AsyncTask(cart_dao, id, amount).execute("");
    }

    private static class update_cart_AsyncTask extends AsyncTask<String, Void, Void> {
        private CART_DAO mAsyncTaskDao;
        String product_id;
        String amount;

        update_cart_AsyncTask(CART_DAO dao, String i, String amo) {
            mAsyncTaskDao = dao;
            product_id = i;
            amount = amo;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.update_cart_amount(product_id, amount);
            return null;
        }
    }
}
