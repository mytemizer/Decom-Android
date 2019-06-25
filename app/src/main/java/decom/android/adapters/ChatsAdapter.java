package decom.android.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import decom.android.R;
import decom.android.models.Chat;

public class ChatsAdapter extends ArrayAdapter<Chat> {

    private ArrayList<Chat> chatList;

    public ChatsAdapter(@NonNull Context context, ArrayList<Chat> chatList) {
        super(context, 0,chatList);
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        Chat chat = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_item,parent,false);
        }

        ImageView profileFoto= convertView.findViewById(R.id.profile_image);
        TextView name = convertView.findViewById(R.id.name);
        TextView lastMessage  = convertView.findViewById(R.id.last_message);

        if(chat!=null){
            profileFoto.setImageBitmap(chat.getProfileFoto());
            name.setText(chat.getChatName());
        }else {
            Log.v("Null", "contact is null");
        }
        return convertView;

    }

    public void setChats(List<Chat> chats){
        int n = chatList.size(), m = chats.size();
        if (n < m){
            // new chat added
            for (int i = n; i < m ; i++){
                chatList.add(chats.get(i));
            }
        } else if (n > m){
            chatList.clear();
            chatList.addAll(chats);
        }
        notifyDataSetChanged();
    }
}
