package decom.android.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

import decom.android.models.Chat;

@Dao
public interface ChatDao {

    @Insert
    void insert(Chat chat);

    @Query("SELECT * FROM chat_table")
    LiveData<List<Chat>> selectAllChats();

    @Delete
    void delete(Chat chat);

    @Delete
    void deleteAll(Chat... chats);

    @Query("DELETE FROM chat_table")
    void emptyTable();
}
