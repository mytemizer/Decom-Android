package decom.android.db.view;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import java.util.List;

import decom.android.db.repos.MessageRepository;
import decom.android.models.Message;

public class MessageViewModel extends AndroidViewModel {

    private MessageRepository messageRepository;
    private LiveData<List<Message>> allMessages;

    public MessageViewModel(@NonNull Application application, String messageId) {
        super(application);
        messageRepository = new MessageRepository(application, messageId);
        allMessages = messageRepository.getAllMessages();
    }


    public LiveData<List<Message>> getAllMessages() {
        return allMessages;
    }

    public void insert(Message message){
        messageRepository.insert(message);
    }

    public void delete(Message... message){
        messageRepository.delete(message);
    }

    public void emptyTable(){
        messageRepository.emptyTable();
    }
}
