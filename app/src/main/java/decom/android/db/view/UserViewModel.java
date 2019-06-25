package decom.android.db.view;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import decom.android.db.repos.UserRepository;
import decom.android.models.User;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private LiveData<User> user;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        user = userRepository.getUser();
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void insert(User user){
        userRepository.insert(user);
    }

    public void updateUser(User user){
        userRepository.updateUser(user);
    }
}
