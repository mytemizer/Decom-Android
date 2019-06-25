package decom.android.db.repos;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import decom.android.db.AppDatabase;
import decom.android.db.dao.MessageDao;
import decom.android.models.Message;

public class MessageRepository {

    private MessageDao messageDao;
    private LiveData<List<Message>> allMessages;

    public MessageRepository(Application application, String messageId){
        AppDatabase db = AppDatabase.getDatabase(application);
        messageDao = db.messageDao();
        allMessages = messageDao.getAllMessages(messageId);
    }

    public LiveData<List<Message>> getAllMessages(){
        return allMessages;
    }

    public void insert(Message message){
        new insertAsyncTask(messageDao).execute(message);
    }

    public void delete(Message... message){
        new deleteAsyncTask(messageDao).execute(message);
    }

    public void emptyTable(){
        new emptyTableAsyncTask(messageDao).execute();
    }

    private static class deleteAsyncTask extends AsyncTask<Message, Void, Void> {

        private MessageDao asyncMessageDao;

        private deleteAsyncTask(MessageDao messageDao) {
            this.asyncMessageDao = messageDao;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            asyncMessageDao.deleteAll(messages);
            return null;
        }
    }

    private static class emptyTableAsyncTask extends AsyncTask<Message, Void, Void> {

        private MessageDao asyncMessageDao;

        private emptyTableAsyncTask(MessageDao messageDao) {
            this.asyncMessageDao = messageDao;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            asyncMessageDao.emptyTable();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Message, Void, Void> {

        private MessageDao asyncMessageDao;

        private insertAsyncTask(MessageDao messageDao) {
            this.asyncMessageDao = messageDao;
        }

        @Override
        protected Void doInBackground(final Message... messages) {
            asyncMessageDao.insert(messages[0]);
            return null;
        }
    }
}
