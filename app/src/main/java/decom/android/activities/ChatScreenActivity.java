package decom.android.activities;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import decom.android.R;
import decom.android.adapters.MessageAdapter;
import decom.android.core.App;
import decom.android.db.view.MessageViewModel;
import decom.android.db.view.MessageViewModelFactory;
import decom.android.models.Chat;
import decom.android.models.Message;
import decom.android.sockets.SendImageTask;
import decom.android.sockets.SendRawTask;
import decom.android.sockets.SendVideoTask;
import decom.android.sockets.TextSender;
import decom.android.utils.Constants;
import decom.android.utils.Utils;


public class ChatScreenActivity extends AppCompatActivity {

    private int chatIndex;
    private EditText sendMessage ;
    public static MessageViewModel messageViewModel;
    public static MessageAdapter messageAdapter;
    final ArrayList<Integer> selectedMessages = new ArrayList<>();
    ArrayList<View> selectedViews = new ArrayList<>();
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        sendMessage = findViewById(R.id.sendMessage);

        Intent intent = getIntent();
        chatIndex = intent.getIntExtra(Constants.EXTRA_MESSAGE,0);

        String chatName = App.user.getChatList().get(chatIndex).getChatName();

        messageAdapter = new MessageAdapter(this, App.user.getChatList().get(chatIndex)
                .getMessageArrayList());

        final ListView messagesView = findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);

        messagesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (selectedMessages.isEmpty()){
                    showDeleteButton();
                }
                if(selectedMessages.contains(i)){
                    selectedViews.remove(view);
                    view.setBackgroundColor(Color.WHITE );
                    selectedMessages.remove(Integer.valueOf(i));
                    if(selectedMessages.isEmpty()){
                        removeDeleteButton();
                    }
                }
                else {
                    selectedViews.add(view);
                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    selectedMessages.add(i);
                }
                return true;
            }
        });

        messagesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(! selectedMessages.isEmpty()){
                    if(selectedMessages.contains(i)){
                        selectedViews.remove(view);
                        view.setBackgroundColor(Color.WHITE );
                        selectedMessages.remove(Integer.valueOf(i));
                        if(selectedMessages.isEmpty()){
                            removeDeleteButton();
                        }
                    }
                    else {
                        selectedViews.add(view);
                        view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        selectedMessages.add(i);
                    }
                }
            }
        });

        messageViewModel = ViewModelProviders.of(this,
                new MessageViewModelFactory(this.getApplication(), App.user.getChatList().get(chatIndex).getId()))
                .get(MessageViewModel.class);

        messageViewModel.getAllMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(@Nullable List<Message> messageEntities) {
                if (messageEntities != null ){
                    messageAdapter.setMessages(messageEntities);
//                    System.out.println(messageEntities);
                }
            }
        });


        TextView displayName = findViewById(R.id.chat_name);
        displayName.setText(chatName);

    }


    private void removeDeleteButton(){
        ImageButton deleteButton = findViewById(R.id.message_delete_button);
        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
    }

    private void showDeleteButton(){
        ImageButton deleteButton = findViewById(R.id.message_delete_button);
        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
    }

    public void sendMassage(View view){
        TextSender textSender = new TextSender(sendMessage.getText().toString(), App.user.getChatList().get(chatIndex));
        new Thread(textSender).start();
        sendMessage.getText().clear();

    }


    public void deleteSelectedMessages(View view){
        ArrayList<Message> messages = new ArrayList<>();
        for (int i : selectedMessages){
            messages.add(App.user.getChatList().get(chatIndex)
                    .getMessageArrayList().get(i));
        }
        deleteMessage(messages.toArray(new Message[0]));
        selectedMessages.clear();
        selectedViews.clear();
        removeDeleteButton();
    }

    public static void deleteMessage(Message... message){
        messageViewModel.delete(message);
    }


    public void sendFile(View view){
        System.out.println("File Send Operation");
        Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        photoPickerIntent.setType("*/*");
        startActivityForResult(Intent.createChooser(photoPickerIntent, "Select File"), 1);    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri fileUri = data.getData();
                final InputStream fileStream = getContentResolver().openInputStream(fileUri);

                System.out.println("--------------" + fileUri);

                String[] firstParsedUri = Utils.parseWithDesiredDelimiter(fileUri.toString(),"/");
                String[] parsedUri = Utils.parseWithDesiredDelimiter(firstParsedUri[4],"%");

                String fileNameRaw;
                String fileType= parsedUri[0];
                for(int i = 0; i < firstParsedUri.length; i++)
                    System.out.println(firstParsedUri[i]);
                if (fileType.equals("image") || fileType.equals("1")){
                    final Bitmap selectedFile = BitmapFactory.decodeStream(fileStream);

                    String path = Utils.saveToInternalStorage(selectedFile);

                    Message message = new Message("P",App.user.getName(), App.user.getChatList().get(chatIndex).getId());
                    message.setImagePath(path);

                    new Thread(new SendImageTask(App.user.getChatList().get(chatIndex), selectedFile)).start();

                    Chat.saveMessage(message);

                } else if (fileType.equals("video") || fileType.equals("audio")){
                    uri = fileUri;

                    Message message = new Message("V",App.user.getName(),App.user.getChatList().get(chatIndex).getId());
                    message.setUri(uri);

                    final InputStream fileStream2;
                    byte[] videoBytes = null;
                    try {
                        assert fileStream != null;
                        fileStream2 = new DataInputStream( fileStream);


                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buf = new byte[1024];
                        int n;
                        while (-1 != (n = fileStream2.read(buf)))
                            baos.write(buf, 0, n);
                        videoBytes = baos.toByteArray();

                        new Thread(new SendVideoTask(App.user.getChatList().get(chatIndex), videoBytes)).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Chat.saveMessage(message);

                }  else{
                    fileNameRaw = parsedUri[parsedUri.length-1].substring(2);
                    uri = fileUri;

                    Message message = new Message("R",App.user.getName(),App.user.getChatList().get(chatIndex).getId());
                    message.setUri(fileUri);

                    new Thread(new SendRawTask(App.user.getChatList().get(chatIndex), uri, fileNameRaw)).start();

                    Chat.saveMessage(message);

                }




                /**
                 * TODO: Implement file sending to each group member
                 */
//                for(int i =0; i< size;i++){
//                    App.user.getChatList().get(chatIndex).getGroupMembers().get(i).sendFile(selectedFile);
//                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(ChatScreenActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(ChatScreenActivity.this, "You haven't picked File", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed(){
        if(selectedMessages.isEmpty()){
            super.onBackPressed();
        } else {
            for (View view : selectedViews){
                view.setBackgroundColor(Color.WHITE);
            }
            selectedViews.clear();
            selectedMessages.clear();
            removeDeleteButton();
        }
    }

    public void playVideo(View view){

        final Intent videShowActivity = new Intent(this, VideoShowActivity.class);
        VideoView videoView = findViewById(R.id.myVideoMessage);
        videoView.getCurrentPosition();
        videShowActivity.putExtra("videoUri", App.user.getChatList().get(chatIndex).getUri().toString());

        startActivity(videShowActivity);
    }

    public void playTheirVideo(View view){

        final Intent videShowActivity = new Intent(this, VideoShowActivity.class);
        VideoView videoView = findViewById(R.id.theirVideoMessage);
        videoView.getCurrentPosition();
        videShowActivity.putExtra("videoUri", App.user.getChatList().get(chatIndex).getUri().toString());

        startActivity(videShowActivity);
    }


    public void openPDF(View view) {
//        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+ filename);
//        Intent target = new Intent(Intent.ACTION_VIEW);
//        target.setDataAndType(Uri.fromFile(file),"application/pdf");
//        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//
//        Intent intent = Intent.createChooser(target, "Open File");
//        try {
//            startActivity(intent);
//        } catch (ActivityNotFoundException e) {
//            // Instruct the user to install a PDF reader here, or something
//        }
    }
}
