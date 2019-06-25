package decom.android.activities;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import decom.android.R;
import decom.android.db.view.ChatViewModel;
import decom.android.db.view.ContactViewModel;
import decom.android.db.view.MessageViewModel;
import decom.android.db.view.MessageViewModelFactory;

public class SettingsActivity extends AppCompatActivity {

    public static ChatViewModel chatViewModel;
    public static ContactViewModel contactViewModel;
    public static MessageViewModel messageViewModel;
    private Context context ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        chatViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        context = this;
        contactViewModel =  ViewModelProviders.of(this).get(ContactViewModel.class);

        messageViewModel =  ViewModelProviders.of(this,
                new MessageViewModelFactory(this.getApplication(), ""))
                .get(MessageViewModel.class);

    }


    public void emptyMessages(View view){

        new AlertDialog.Builder(context)
                .setMessage( "Dou you want to delete all the messages !"  )

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        messageViewModel.emptyTable();
                        Toast.makeText(getApplicationContext(), "All messages are deleted!", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.decom)
                .show();

    }

    public void emptyContact(View view){

        new AlertDialog.Builder(context)
                .setMessage( "Dou you want to delete all the contacts !"  )

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        contactViewModel.emptyTable();
                        Toast.makeText(getApplicationContext(), "All contacts are deleted!", Toast.LENGTH_LONG).show();

                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.decom)
                .show();

    }

    public void emptyChat(View view){

        new AlertDialog.Builder(context)
                .setMessage( "Dou you want to delete all the chats !"  )

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        chatViewModel.emptyTable();
                        Toast.makeText(getApplicationContext(), "All chats are deleted!", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.decom)
                .show();

    }


}
