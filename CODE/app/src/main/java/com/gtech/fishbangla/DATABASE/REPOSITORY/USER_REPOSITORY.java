package com.gtech.fishbangla.DATABASE.REPOSITORY;

import android.app.Application;
import android.os.AsyncTask;

import com.gtech.fishbangla.DATABASE.DAO.USER_DAO;
import com.gtech.fishbangla.DATABASE.FISHBANGLA_DATABASE;
import com.gtech.fishbangla.DATABASE.TABLE.USER_PROFILE;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class USER_REPOSITORY {
    USER_DAO user_dao;

    public USER_REPOSITORY(Application application) {
        FISHBANGLA_DATABASE db = FISHBANGLA_DATABASE.getDatabase(application);
        user_dao = db.user_dao();
    }

    public void insert_user(USER_PROFILE userProfile) {
        new insertAsyncTask(user_dao).execute(userProfile);
    }

    public void deleteall() {
        new deleteallAsyncTask(user_dao).execute();
    }

    public void update_user_Image(String id, String image) {
        new update_user_Image(user_dao, id, image).execute("");
    }

    public USER_PROFILE userdata() {
        return user_dao.user_profile();
    }

    private static class insertAsyncTask extends AsyncTask<USER_PROFILE, Void, Void> {

        private USER_DAO mAsyncTaskDao;

        insertAsyncTask(USER_DAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final USER_PROFILE... params) {
            mAsyncTaskDao.user_insert(params[0]);
            return null;
        }
    }

    private static class deleteallAsyncTask extends AsyncTask<String, Void, Void> {

        private USER_DAO mAsyncTaskDao;

        deleteallAsyncTask(USER_DAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(String... params) {
            mAsyncTaskDao.delete_user();
            return null;
        }
    }


    public USER_PROFILE getuserData() throws ExecutionException, InterruptedException {

        Callable<USER_PROFILE> callable = new Callable<USER_PROFILE>() {
            @Override
            public USER_PROFILE call() throws Exception {
                return user_dao.user_profile();
            }
        };

        Future<USER_PROFILE> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }

    private static class update_user_Image extends AsyncTask<String, Void, Void> {
        private USER_DAO mAsyncTaskDao;
        String user_id;
        String image;

        update_user_Image(USER_DAO dao, String i, String amo) {
            mAsyncTaskDao = dao;
            user_id = i;
            image = amo;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.update_User_Image(user_id, image);
            return null;
        }
    }
}
