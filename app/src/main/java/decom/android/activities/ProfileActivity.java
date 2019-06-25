package decom.android.activities;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import decom.android.R;
import decom.android.core.App;
import decom.android.db.view.UserViewModel;

public class ProfileActivity extends AppCompatActivity {

    private TextView name, ip, id;
    private ImageView pp;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.profile_name);
        id = findViewById(R.id.profile_id);
        ip = findViewById(R.id.profile_ip);
        pp = findViewById(R.id.profile_profile_image);

        ip.setText(ContactListActivity2.ipAddress.getText());
        name.setText(App.user.getName());
        id.setText(App.user.getId());
        if (App.user.getProfileFoto() != null)
            pp.setImageBitmap(App.user.getProfileFoto());

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

    }


    public void changeUserName(View view) {
        Intent intent = new Intent(this, ChangeUserNameActivity.class);
        startActivity(intent);
    }

    public void changeProfilePhoto(View view) {
        System.out.println("File Send Operation");
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"), 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        name.setText(App.user.getName());
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri fileUri = data.getData();
                if (fileUri != null){
                    final InputStream fileStream = getContentResolver().openInputStream(fileUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(fileStream);
                    App.user.setProfileFoto(selectedImage);
                    pp.setImageBitmap(App.user.getProfileFoto());
                    userViewModel.updateUser(App.user);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "You haven't picked File", Toast.LENGTH_LONG).show();
        }

    }
}

