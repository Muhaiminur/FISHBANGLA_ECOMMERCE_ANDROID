package com.gtech.fishbangla.DATABASE.REPOSITORY;

import android.app.Application;
import android.os.AsyncTask;

import com.gtech.fishbangla.DATABASE.DAO.DIVISION_DAO;
import com.gtech.fishbangla.DATABASE.FISHBANGLA_DATABASE;
import com.gtech.fishbangla.DATABASE.TABLE.DIVISION_TABLE;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DIVISION_REPOSITORY {
    DIVISION_DAO division_dao;

    public DIVISION_REPOSITORY(Application application) {
        FISHBANGLA_DATABASE db = FISHBANGLA_DATABASE.getDatabase(application);
        division_dao = db.division_dao();
    }

    public void insert_division(List<DIVISION_TABLE> configTable) {
        new DIVISION_REPOSITORY.insertAsyncTask(division_dao, configTable).execute("");
    }

    public void delete_division() {
        new DIVISION_REPOSITORY.division_delete_all(division_dao).execute();
    }

    public List<DIVISION_TABLE> division_data() {
        return division_dao.get_division();
    }

    private static class insertAsyncTask extends AsyncTask<String, Void, Void> {

        private DIVISION_DAO mAsyncTaskDao;
        List<DIVISION_TABLE> division_tables;

        insertAsyncTask(DIVISION_DAO dao, List<DIVISION_TABLE> tables) {
            mAsyncTaskDao = dao;
            division_tables = tables;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.insert_division(division_tables);
            return null;
        }
    }

    private static class division_delete_all extends AsyncTask<String, Void, Void> {

        private DIVISION_DAO mAsyncTaskDao;

        division_delete_all(DIVISION_DAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(String... params) {
            mAsyncTaskDao.delete_division();
            return null;
        }
    }


    public List<DIVISION_TABLE> getdivisionData() throws ExecutionException, InterruptedException {

        Callable<List<DIVISION_TABLE>> callable = new Callable<List<DIVISION_TABLE>>() {
            @Override
            public List<DIVISION_TABLE> call() throws Exception {
                return division_dao.get_division();
            }
        };

        Future<List<DIVISION_TABLE>> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }
}
