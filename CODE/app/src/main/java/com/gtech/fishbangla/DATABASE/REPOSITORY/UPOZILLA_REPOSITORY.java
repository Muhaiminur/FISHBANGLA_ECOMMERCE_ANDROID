package com.gtech.fishbangla.DATABASE.REPOSITORY;

import android.app.Application;
import android.os.AsyncTask;

import com.gtech.fishbangla.DATABASE.DAO.UPOZILLA_DAO;
import com.gtech.fishbangla.DATABASE.FISHBANGLA_DATABASE;
import com.gtech.fishbangla.DATABASE.TABLE.UPAZILLA_TABLE;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UPOZILLA_REPOSITORY {
    UPOZILLA_DAO upozilla_dao;

    public UPOZILLA_REPOSITORY(Application application) {
        FISHBANGLA_DATABASE db = FISHBANGLA_DATABASE.getDatabase(application);
        upozilla_dao = db.upozilla_dao();
    }

    public void insert_upozilla(List<UPAZILLA_TABLE> configTable) {
        new insertAsyncTask(upozilla_dao, configTable).execute("");
    }

    public void delete_upozilla() {
        new upozilla_delete_all(upozilla_dao).execute();
    }

    public List<UPAZILLA_TABLE> upozilla_data() {
        return upozilla_dao.get_upazilla();
    }

    private static class insertAsyncTask extends AsyncTask<String, Void, Void> {

        private UPOZILLA_DAO mAsyncTaskDao;
        List<UPAZILLA_TABLE> upazilla_tables;

        insertAsyncTask(UPOZILLA_DAO dao, List<UPAZILLA_TABLE> tables) {
            mAsyncTaskDao = dao;
            upazilla_tables = tables;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.insert_upazilla(upazilla_tables);
            return null;
        }
    }

    private static class upozilla_delete_all extends AsyncTask<String, Void, Void> {

        private UPOZILLA_DAO mAsyncTaskDao;

        upozilla_delete_all(UPOZILLA_DAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(String... params) {
            mAsyncTaskDao.delete_upazilla();
            return null;
        }
    }


    public List<UPAZILLA_TABLE> getupazillaData() throws ExecutionException, InterruptedException {

        Callable<List<UPAZILLA_TABLE>> callable = new Callable<List<UPAZILLA_TABLE>>() {
            @Override
            public List<UPAZILLA_TABLE> call() throws Exception {
                return upozilla_dao.get_upazilla();
            }
        };

        Future<List<UPAZILLA_TABLE>> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }


    public List<UPAZILLA_TABLE> findupazillaData(String s) throws ExecutionException, InterruptedException {

        Callable<List<UPAZILLA_TABLE>> callable = new Callable<List<UPAZILLA_TABLE>>() {
            @Override
            public List<UPAZILLA_TABLE> call() throws Exception {
                return upozilla_dao.find_upazilla(s);
            }
        };

        Future<List<UPAZILLA_TABLE>> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }
}
