package decom.android.db.repos;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;

import java.util.List;

import decom.android.db.AppDatabase;
import decom.android.db.dao.ChatDao;
import decom.android.models.Chat;

public class ChatRepository {

    private ChatDao chatDao;
    private LiveData<List<Chat>> chats;

    public ChatRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        chatDao = db.chatDao();
        chats =chatDao.selectAllChats();
    }

    public LiveData<List<Chat>> getChats(){
        return chats;
    }

    public void insert(Chat chat){
        new insertAsyncTask(chatDao).execute(chat);
    }

    public void delete(Chat... chat){
        new deleteAsyncTask(chatDao).execute(chat);
    }

    public void emptyTable(){
        new emptyTableAsyncTask(chatDao).execute();
    }

    private static class deleteAsyncTask extends AsyncTask<Chat, Void, Void> {

        private ChatDao asyncChatDao;

        private deleteAsyncTask(ChatDao chatDao){
            this.asyncChatDao = chatDao;
        }

        @Override
        protected Void doInBackground(Chat... chats) {
            asyncChatDao.deleteAll(chats);
            return null;
        }
    }


    private static class emptyTableAsyncTask extends AsyncTask<Chat, Void, Void> {

        private ChatDao asyncChatDao;

        private emptyTableAsyncTask(ChatDao chatDao){
            this.asyncChatDao = chatDao;
        }

        @Override
        protected Void doInBackground(Chat... chats) {
            asyncChatDao.emptyTable();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Chat, Void, Void> {

        private ChatDao asyncChatDao;

        private insertAsyncTask(ChatDao chatDao){
            this.asyncChatDao = chatDao;
        }

        @Override
        protected Void doInBackground(Chat... chats) {
            try {
                asyncChatDao.insert(chats[0]);
            } catch (SQLiteConstraintException ignored){

            }
            return null;
        }
    }
}
