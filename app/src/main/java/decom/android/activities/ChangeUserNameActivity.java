package decom.android.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import decom.android.R;
import decom.android.core.App;
import decom.android.db.view.UserViewModel;

public class ChangeUserNameActivity extends AppCompatActivity {
    private TextView newName;
    private UserViewModel userViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_name);

        newName = findViewById(R.id.new_name);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

    }



    public void changeUserName(View view){
        String newNameStr = newName.getText().toString();
        App.user.setOnlyName(newNameStr);
        userViewModel.updateUser(App.user);
        Toast.makeText(getApplicationContext(), "User name changed", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }
}
