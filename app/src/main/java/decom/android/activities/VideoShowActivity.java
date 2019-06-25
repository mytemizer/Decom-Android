package decom.android.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import decom.android.R;

public class VideoShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        String uri = intent.getStringExtra("videoUri");
        Uri fileUri = Uri.parse(uri);

        setContentView(R.layout.activity_video_show);
        VideoView videoView = findViewById(R.id.myVideoMessage);

        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(videoView);
        vidControl.setMediaPlayer(videoView);
        videoView.setMediaController(vidControl);
        videoView.setVideoURI(fileUri);
        videoView.start();
    }


}
