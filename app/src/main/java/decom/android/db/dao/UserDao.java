package decom.android.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import decom.android.models.User;

@Dao
public interface UserDao {
    @Insert
    void insert(User... user);

    @Delete
    void delete(User user);

    @Delete
    void deleteAll(User... users);

    @Query("SELECT * FROM user_table")
    LiveData<User> getUser();

    @Update
    void updateUser(User... user);

}
