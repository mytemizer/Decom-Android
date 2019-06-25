package decom.android.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import decom.android.models.Contact;

@Dao
public interface ContactDao {

    @Insert
    void insert(Contact contact);

    @Query("SELECT * FROM contact_table")
    LiveData<List<Contact>> getContacts();

    @Delete
    void delete(Contact contact);

    @Delete
    void deleteAll(Contact... contacts);


    @Query("DELETE FROM contact_table")
    void emptyTable();
}
