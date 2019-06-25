package decom.android.db.view;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MessageViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;
    private String senderName;

    public MessageViewModelFactory(Application mApplication, String senderName){
        this.mApplication = mApplication;
        this.senderName = senderName;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MessageViewModel(mApplication, senderName);
    }
}
