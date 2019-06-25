package decom.android.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import decom.android.models.Message;

public class MessageAdapter extends ArrayAdapter<Message> {

    private Context mContext;
    private ArrayList<Message> messageArrayList ;

    public MessageAdapter(@NonNull Context context, ArrayList<Message> messages) {
        super(context, 0, messages);
        mContext=context;
        messageArrayList = messages;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Message currentMessage = messageArrayList.get(position);
        if(currentMessage == null){
            currentMessage = new Message("null message","default", "default");
        }
        return currentMessage.getListItem(mContext,parent);

    }

    public void setMessages(List<Message> messages){
        int n = messageArrayList.size(), m = messages.size();
        if(m > n){
            for (int i = n; i < m ; i++){
                messageArrayList.add(messages.get(i));
            }
        } else if(n > m){
            messageArrayList.clear();
            messageArrayList.addAll(messages);
        }
        notifyDataSetChanged();
    }
}
