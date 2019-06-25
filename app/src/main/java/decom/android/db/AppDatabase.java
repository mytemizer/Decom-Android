package decom.android.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import decom.android.db.dao.ChatDao;
import decom.android.db.dao.ContactDao;
import decom.android.db.dao.MessageDao;
import decom.android.db.dao.UserDao;
import decom.android.models.Chat;
import decom.android.models.Contact;
import decom.android.models.Message;
import decom.android.models.User;

@Database(entities = {Message.class, User.class, Chat.class, Contact.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract MessageDao messageDao();

    public abstract UserDao userDao();

    public abstract ChatDao chatDao();

    public abstract ContactDao contactDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null){
            synchronized (AppDatabase.class) {
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "message_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
