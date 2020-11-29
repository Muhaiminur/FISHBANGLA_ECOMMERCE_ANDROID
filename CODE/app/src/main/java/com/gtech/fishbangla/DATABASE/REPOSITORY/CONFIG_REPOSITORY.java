package com.gtech.fishbangla.DATABASE.REPOSITORY;

import android.app.Application;
import android.os.AsyncTask;

import com.gtech.fishbangla.DATABASE.DAO.CONFIG_DAO;
import com.gtech.fishbangla.DATABASE.FISHBANGLA_DATABASE;
import com.gtech.fishbangla.DATABASE.TABLE.CONFIG_TABLE;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CONFIG_REPOSITORY {
    CONFIG_DAO config_dao;

    public CONFIG_REPOSITORY(Application application) {
        FISHBANGLA_DATABASE db = FISHBANGLA_DATABASE.getDatabase(application);
        config_dao = db.config_dao();
    }

    public void insert_config_repo(CONFIG_TABLE configTable) {
        new insertAsyncTask(config_dao).execute(configTable);
    }

    public void delete_config() {
        new deleteallAsyncTask(config_dao).execute();
    }

    public CONFIG_TABLE config_data() {
        return config_dao.get_config();
    }

    private static class insertAsyncTask extends AsyncTask<CONFIG_TABLE, Void, Void> {

        private CONFIG_DAO mAsyncTaskDao;

        insertAsyncTask(CONFIG_DAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CONFIG_TABLE... params) {
            mAsyncTaskDao.insert_config(params[0]);
            return null;
        }
    }

    private static class deleteallAsyncTask extends AsyncTask<String, Void, Void> {

        private CONFIG_DAO mAsyncTaskDao;

        deleteallAsyncTask(CONFIG_DAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(String... params) {
            mAsyncTaskDao.delete_config();
            return null;
        }
    }


    public CONFIG_TABLE getconfigData() throws ExecutionException, InterruptedException {

        Callable<CONFIG_TABLE> callable = new Callable<CONFIG_TABLE>() {
            @Override
            public CONFIG_TABLE call() throws Exception {
                return config_dao.get_config();
            }
        };

        Future<CONFIG_TABLE> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }
}
