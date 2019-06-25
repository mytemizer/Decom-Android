package decom.android.activities;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import decom.android.R;
import decom.android.adapters.CheckBoxAdapter;
import decom.android.core.App;
import decom.android.enums.GroupRespond;
import decom.android.models.Chat;
import decom.android.models.Contact;
import decom.android.sockets.SocketBuilder;
import decom.android.utils.Constants;
import decom.android.utils.JsonBuilder;

public class AddGroupActivity extends AppCompatActivity {
    private ArrayList<Contact> selectedItems = new ArrayList<>();
    StringBuffer buffer;
    Chat chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        setTitle(R.string.add_group_title);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView addGroupListView = findViewById(R.id.add_group_list);

        final ArrayList<Contact> contactArrayList = App.user.getContactList();

        CheckBoxAdapter checkBoxAdapter = new CheckBoxAdapter(this,contactArrayList);
        checkBoxAdapter.notifyDataSetChanged();

        addGroupListView.setAdapter(checkBoxAdapter);
        // When list view item is clicked.
        addGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {

                final TextView groupName = findViewById(R.id.newGroupName);

                // Get user selected item.
                Object itemObject = adapterView.getAdapter().getItem(itemIndex);

                // Translate the selected item to DTO object.
                Contact itemDto = (Contact)itemObject;

                // Get the checkbox.
                CheckBox itemCheckbox = view.findViewById(R.id.group_checkbox);

                // Reverse the checkbox and clicked item check state.
                if(itemDto.isChecked())
                {
                    selectedItems.remove(contactArrayList.get(itemIndex));
                    System.out.println(itemIndex);
                    itemCheckbox.setChecked(false);
                    itemDto.setChecked(false);
                    if (selectedItems.size()<2){
                        groupName.setEnabled(false);
                        groupName.setHighlightColor(111111);
                    }
                }else
                {
                    selectedItems.add(contactArrayList.get(itemIndex));
                    System.out.println(itemIndex);
                    itemCheckbox.setChecked(true);
                    itemDto.setChecked(true);
                    if (selectedItems.size()>1){
                        groupName.setEnabled(true);
                    }
                }

                //Toast.makeText(getApplicationContext(), "select item text : " + itemDto.getItemText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ArrayList<Contact> getSelectedItems() {
        return this.selectedItems;
    }

    public void addGroupToChatList(View view) {
        final TextView groupName = findViewById(R.id.newGroupName);
        ArrayList<Contact> selectedList = getSelectedItems();

        if (selectedList.size()==1){
            chat = new Chat(selectedList.get(0).getName(), BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.smiley));

        } else{
            chat = new Chat(groupName.getText().toString(), BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.smiley));
        }


        for (Contact contact : selectedList){
            chat.getGroupMembers().add(contact);
            chat.getContactIds().add(contact.getId());
        }

        onBackPressed();

        for (final Contact contact : selectedList){
            final String contactIP = contact.getIpAddress();

            Thread getThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket socket = SocketBuilder.getSSLSocket(contactIP, Constants.PORT);
                        if (socket == null){
                            System.out.println("Error while getting sender socket");
                            return;
                        }
                        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        JSONObject jsonObject = JsonBuilder.buildGroupRequest(chat, contact.getId());
                        if (jsonObject != null) {
                            dataOutputStream.write(jsonObject.toString().getBytes());
                        }
                        byte[] response = new byte[100];
                        int read = dataInputStream.read(response);
                        if (read != -1){
                            String responseString = new String(response);
                            JSONObject responseJson = new JSONObject(responseString);
                            String respondType = responseJson.getString("respond_type");
                            if (respondType.equals(GroupRespond.GROUP_OK.name())){
                                App.user.addChat(chat);
                            }

                        }
                    } catch (IOException | JSONException e) {
                        System.out.println("myPrints Error while getting streams");
                        e.printStackTrace();
                    }
                }
            });
            getThread.start();

            try {
                getThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
