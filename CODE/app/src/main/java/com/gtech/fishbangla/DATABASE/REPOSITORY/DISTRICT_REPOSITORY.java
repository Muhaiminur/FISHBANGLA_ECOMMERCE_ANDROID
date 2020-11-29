package com.gtech.fishbangla.DATABASE.REPOSITORY;

import android.app.Application;
import android.os.AsyncTask;

import com.gtech.fishbangla.DATABASE.DAO.DISTRICT_DAO;
import com.gtech.fishbangla.DATABASE.FISHBANGLA_DATABASE;
import com.gtech.fishbangla.DATABASE.TABLE.DISTRICT_TABLE;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DISTRICT_REPOSITORY {
    DISTRICT_DAO district_dao;

    public DISTRICT_REPOSITORY(Application application) {
        FISHBANGLA_DATABASE db = FISHBANGLA_DATABASE.getDatabase(application);
        district_dao = db.district_dao();
    }

    public void insert_district(List<DISTRICT_TABLE> configTable) {
        new DISTRICT_REPOSITORY.insertAsyncTask(district_dao, configTable).execute("");
    }

    public void delete_district() {
        new DISTRICT_REPOSITORY.district_delete_all(district_dao).execute();
    }

    public List<DISTRICT_TABLE> district_data() {
        return district_dao.get_district();
    }

    private static class insertAsyncTask extends AsyncTask<String, Void, Void> {

        private DISTRICT_DAO mAsyncTaskDao;
        List<DISTRICT_TABLE> district_tables;

        insertAsyncTask(DISTRICT_DAO dao, List<DISTRICT_TABLE> tables) {
            mAsyncTaskDao = dao;
            district_tables = tables;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.insert_district(district_tables);
            return null;
        }
    }

    private static class district_delete_all extends AsyncTask<String, Void, Void> {

        private DISTRICT_DAO mAsyncTaskDao;

        district_delete_all(DISTRICT_DAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(String... params) {
            mAsyncTaskDao.delete_district();
            return null;
        }
    }


    public List<DISTRICT_TABLE> getdistrictData() throws ExecutionException, InterruptedException {

        Callable<List<DISTRICT_TABLE>> callable = new Callable<List<DISTRICT_TABLE>>() {
            @Override
            public List<DISTRICT_TABLE> call() throws Exception {
                return district_dao.get_district();
            }
        };

        Future<List<DISTRICT_TABLE>> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }


    public List<DISTRICT_TABLE> finddistrictData(String s) throws ExecutionException, InterruptedException {

        Callable<List<DISTRICT_TABLE>> callable = new Callable<List<DISTRICT_TABLE>>() {
            @Override
            public List<DISTRICT_TABLE> call() throws Exception {
                return district_dao.find_district(s);
            }
        };

        Future<List<DISTRICT_TABLE>> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }
}
