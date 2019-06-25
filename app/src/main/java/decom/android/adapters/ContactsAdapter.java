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
import decom.android.models.Contact;

public class ContactsAdapter extends ArrayAdapter<Contact> {

    private ArrayList<Contact> contacts;

    public ContactsAdapter(@NonNull Context context, ArrayList<Contact> contacts) {
        super(context, 0,contacts);
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        Contact contact = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_item,parent,false);
        }

        ImageView profileFoto= convertView.findViewById(R.id.profile_image);
        TextView name = convertView.findViewById(R.id.name);
        TextView lastMessage  = convertView.findViewById(R.id.last_message);
        if(contact!=null){
            profileFoto.setImageBitmap(contact.getProfileFoto());
            name.setText(contact.getName());
        }else {
            Log.v("Null", "contact is null");
        }
        return convertView;

    }

    public void setContacts(List<Contact> contacts){
        int n = this.contacts.size(), m = contacts.size();
        if (m > n){
            for (int i = n; i < m ; i++){
                this.contacts.add(contacts.get(i));
            }
        }else if(m < n) {
            this.contacts.clear();
            this.contacts.addAll(contacts);
        }
        notifyDataSetChanged();
    }
}
