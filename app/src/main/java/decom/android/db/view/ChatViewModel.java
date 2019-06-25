package decom.android.db.view;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;


import java.util.List;

import decom.android.db.repos.ChatRepository;
import decom.android.models.Chat;

public class ChatViewModel extends AndroidViewModel {

    private ChatRepository chatRepository;
    private LiveData<List<Chat>> chats;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        chatRepository = new ChatRepository(application);
        chats = chatRepository.getChats();
    }

    public LiveData<List<Chat>> getChats() {
        return chats;
    }

    public void insert(Chat chat){
        chatRepository.insert(chat);
    }

    public void delete(Chat... chat){
        chatRepository.delete(chat);
    }

    public void emptyTable(){
        chatRepository.emptyTable();
    }
}
