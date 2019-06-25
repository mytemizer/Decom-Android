package decom.android.activities;


import android.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;


import decom.android.R;
import decom.android.core.App;
import decom.android.db.view.UserViewModel;
import decom.android.models.User;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "myPrints   ";
    private Intent contactList;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App.user  = new User();
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);


    }

    public void setUserName(View view){
        TextView username = findViewById(R.id.userName);
        Intent contactListActivity2 =  new Intent(this,ContactListActivity2.class);
        App.user.setName(username.getText().toString());

        if (!App.user.getName().equals("")){
            userViewModel.insert(App.user);
            startActivity(contactListActivity2);
        }
        else {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Invalid Operation")
                    .setMessage("Please set UserName!" )
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Whatever...
                        }
                    }).show();
        }
    }





}
