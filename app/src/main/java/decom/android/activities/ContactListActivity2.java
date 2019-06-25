package decom.android.activities;

import android.annotation.SuppressLint;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.List;

import decom.android.R;
import decom.android.adapters.ChatsAdapter;
import decom.android.adapters.ContactsAdapter;
import decom.android.adapters.DisabledViewPager;
import decom.android.adapters.PagerAdapter;
import decom.android.core.App;
import decom.android.db.view.ChatViewModel;
import decom.android.db.view.ContactViewModel;
import decom.android.db.view.MessageViewModel;
import decom.android.db.view.MessageViewModelFactory;
import decom.android.db.view.UserViewModel;
import decom.android.models.Chat;
import decom.android.models.Contact;
import decom.android.models.User;
import decom.android.sockets.ListenerServer;
import decom.android.utils.Constants;
import decom.android.utils.Utils;

import static java.lang.Thread.sleep;


public class ContactListActivity2 extends AppCompatActivity {
    public static TextView ipAddress;
    private TextView userName;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private ContactsAdapter contactsAdapter;
    private ChatsAdapter chatsAdapter;
    private static ChatViewModel chatViewModel;
    private static ContactViewModel contactViewModel;
    public static MessageViewModel messageViewModel;
    private boolean exitFlag = false;
    public static Context context ;

    private int[] tabIcons = {
            R.drawable.message_cloud,
            R.drawable.group
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Context self = this;
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_chat_contact_menu);
        ipAddress= findViewById(R.id.device_ip);
        userName = findViewById(R.id.user_name);

        App.resources = getResources();

        App.user = new User();

        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if(user != null){
                    App.user.deepCopy(user);
                    userName.setText(App.user.getName());
                } else {
                    Intent mainActivity = new Intent(self, MainActivity.class);
                    startActivity(mainActivity);
                }
            }
        });

        Thread server = new Thread(new ListenerServer());
        server.start();

        contactsAdapter = new ContactsAdapter(self, App.user.getContactList());
        chatsAdapter= new ChatsAdapter(self, App.user.getChatList());

        chatViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);

        contactViewModel =  ViewModelProviders.of(this).get(ContactViewModel.class);

        messageViewModel = ViewModelProviders.of(this,
                new MessageViewModelFactory(this.getApplication(), ""))
                .get(MessageViewModel.class);

        chatViewModel.getChats().observe(this, new Observer<List<Chat>>() {
            @Override
            public void onChanged(@Nullable List<Chat> chats) {
                chatsAdapter.setChats(chats);
            }
        });

        contactViewModel.getContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(@Nullable List<Contact> contacts) {
                contactsAdapter.setContacts(contacts);
            }
        });

        startIpThread();

        final TabLayout tabLayout = createTabLayout();

        viewPager =(DisabledViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.lightBlue));
        final Intent chatScreen = new Intent(this, ChatScreenActivity.class);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                System.out.println(tab.getText());
                if(tab.getText() == "Contacts"){

                    // find the list view that will display contacts
                    final SwipeMenuListView contactsView = findViewById(R.id.contact_list);

                    // when a list item clicked
                    contactsView.setOnItemClickListener(new  AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            new AlertDialog.Builder(self)
                                    .setTitle(App.user.getContactList().get(position).getName())
                                    .setMessage( "IP Address : " + App.user.getContactList().get(position).getIpAddress() +  "\n" +
                                                 "ID : " +  App.user.getContactList().get(position).getId()
                                            )

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.ok, null)
                                    .setIcon(R.drawable.decom)
                                    .show();
                        }
                    });

                    SwipeMenuCreator creator = getSwipeDelete();
                    contactsView.setAdapter(contactsAdapter);
                    contactsView.setMenuCreator(creator);

                    contactsView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(final int position, SwipeMenu menu, final int index) {
                            switch (index) {
                                case 0:
                                    new AlertDialog.Builder(self)
                                            .setTitle(App.user.getContactList().get(position).getName())
                                            .setMessage( "IP Address : " + App.user.getContactList().get(position).getIpAddress() +  "\n" +
                                                    "ID : " +  App.user.getContactList().get(position).getId() +  "\n" +
                                                    "Do you want to delete the contact ?"
                                            )

                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    deleteContact(App.user.getContactList().get(position));
                                                }
                                            })

                                            // A null listener allows the button to dismiss the dialog and take no further action.
                                            .setNegativeButton(android.R.string.no, null)
                                            .setIcon(R.drawable.decom)
                                            .show();
                                    break;

                            }
                            // false : close the menu; true : not close the menu
                            return false;
                        }
                    });
                }
                else if(tab.getText() == "Chats"){
                    // find the list view that will display contacts
                    SwipeMenuListView chatsView = findViewById(R.id.chat_list);
                    SwipeMenuCreator creator = getSwipeDelete();
                    chatsView.setMenuCreator(creator);

                    chatsView.setOnItemClickListener(new  AdapterView.OnItemClickListener() {                // when a list item clicked
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            chatScreen.putExtra(Constants.EXTRA_MESSAGE,position);                              // open chat screen activity with parameter
                            startActivity(chatScreen);
                        }
                    });

                    chatsView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                            switch (index){
                                case 0:
                                    new AlertDialog.Builder(self)
                                            .setTitle(App.user.getChatList().get(position).getChatName())
                                            .setMessage("Do you want to delete the chat !")

                                            .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    deleteChat(App.user.getChatList().get(position));
                                                }
                                            })

                                            // A null listener allows the button to dismiss the dialog and take no further action.
                                            .setNegativeButton(android.R.string.no, null)
                                            .setIcon(R.drawable.decom)
                                            .show();
                            }
                            return false;
                        }
                    });

                    chatsView.setAdapter(chatsAdapter);
                }
                else{}
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                System.out.println(tab.getText());
                if(tab.getText() == "Contacts"){
                    ListView contactsView = findViewById(R.id.contact_list);                                    // find the list view that will display contacts
                    contactsView.setAdapter(contactsAdapter);
                }
                else if(tab.getText() == "Chats"){
                    ListView chatsView = findViewById(R.id.chat_list);                                    // find the list view that will display contacts
                    chatsView.setAdapter(chatsAdapter);
                }
                else{}
            }
        });

//        addContactDumy();
//        addChatsDummy();

    }


    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        if(!exitFlag) {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            exitFlag = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        sleep(1500);
                        exitFlag = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        if (item.getItemId() == R.id.profile) {
            startActivity(new Intent(this, ProfileActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public TabLayout createTabLayout(){

        TabLayout tabLayout = findViewById(R.id.tab_layout2);

        tabLayout.addTab(tabLayout.newTab().setText("Chats"));
        tabLayout.addTab(tabLayout.newTab().setText("Contacts"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        return tabLayout;
    }

    public void startIpThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ipAddress.setText(Utils.getIPAddress(true));
                        }
                    });
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    private SwipeMenuCreator getSwipeDelete(){
        return new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "deleteMessage" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(175);
                // set a icon
                deleteItem.setIcon(R.drawable.delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
    }

    public void goAddFriend(View view){
        // add given contact
        Intent addContact = new Intent(this, AddContactActivity.class);
        startActivity(addContact);
    }
    public void goAddGroup(View view){
        Intent addGroup = new Intent(this, AddGroupActivity.class);
        startActivity(addGroup);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (contactsAdapter != null) {
            contactsAdapter.notifyDataSetChanged();
        }
        if (chatsAdapter != null) {
            chatsAdapter.notifyDataSetChanged();
        }

    }

    public static void saveChat(Chat chat){
        chatViewModel.insert(chat);
    }

    public static void saveContact(Contact contact){
        contactViewModel.insert(contact);
    }

    public void deleteContact(Contact... contact){
        contactViewModel.delete(contact);
    }

    public void deleteChat(Chat... chat){
        chatViewModel.delete(chat);
    }
}