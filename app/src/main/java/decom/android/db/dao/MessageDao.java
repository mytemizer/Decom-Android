package decom.android.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

import decom.android.models.Message;

@Dao
public interface MessageDao {

    @Insert
    void insert(Message message);

    @Delete
    void delete(Message message);

    @Delete
    void deleteAll(Message... messages);

    @Query("SELECT * from message_table WHERE chat_id = :chatId")
    LiveData<List<Message>> getAllMessages(String chatId);

    @Query("SELECT * from message_table")
    LiveData<List<Message>> getAllMessages();

    @Query("DELETE FROM message_table")
    void emptyTable();


}
