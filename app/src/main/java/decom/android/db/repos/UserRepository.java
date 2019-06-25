package decom.android.db.repos;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;

import decom.android.db.AppDatabase;
import decom.android.db.dao.UserDao;
import decom.android.models.User;

public class UserRepository {

    private UserDao userDao;
    private LiveData<User> user;

    public UserRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        userDao = db.userDao();
        user = userDao.getUser();
    }

    public LiveData<User> getUser(){
        return user;
    }

    public void insert(User user){
        new insertAsyncTask(userDao).execute(user);
    }

    public void updateUser(User user){
        new updateAsyncTask(userDao).execute(user);
    }

    private static class updateAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao asyncUserDao;

        private updateAsyncTask(UserDao userDao){
            this.asyncUserDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            asyncUserDao.updateUser(users);
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao asyncUserDao;

        private insertAsyncTask(UserDao userDao){
            this.asyncUserDao = userDao;
        }

        @Override
        protected Void doInBackground(final User... users) {
            asyncUserDao.insert(users);
            return null;
        }
    }

}
