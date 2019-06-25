package decom.android.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.nio.charset.StandardCharsets;

import decom.android.R;
import decom.android.core.App;
import decom.android.utils.Utils;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "message_table",
    foreignKeys = @ForeignKey(entity = Chat.class,
                                parentColumns = "id",
                                childColumns = "chat_id",
                                onDelete = CASCADE))
public class Message{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "primary_id")
    private int key;

    @NonNull
    @ColumnInfo(name = "chat_id")
    private String chatId;

    @NonNull
    @ColumnInfo(name = "sender_name")
    private String senderName;

    @ColumnInfo(name = "content")
    @NonNull
    private String message;

    @ColumnInfo(name = "image_path")
    private String imagePath;

    @Ignore
    private ImageView imageView;

    @Ignore
    private Bitmap bitmap;

    @Ignore
    private Uri uri;

    public Message(@NonNull String message, @NonNull String senderName, @NonNull String chatId, String imagePath){
        this.chatId = chatId;
        this.senderName = senderName;
        this.message = message;
        this.imagePath = imagePath;
        this.bitmap = Utils.loadImageFromStorage(imagePath);
    }

    @Ignore
    public Message(@NonNull String message, @NonNull String senderName, @NonNull String chatId){
        this.chatId = chatId;
        this.senderName = senderName;
        this.message = message;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    public void setMessage(@NonNull String message) {
        this.message = message;
    }

    @NonNull
    public String getSenderName(){
        return senderName;
    }

    public void setSenderName(@NonNull String senderName){
        this.senderName = senderName;
    }

    byte[] getDataBytes() {
        return message.getBytes(StandardCharsets.UTF_8);
    }


    public View getListItem(Context context, ViewGroup parent){
        if (message.charAt(0) == 'M'){
            if(this.senderName.equals(App.user.getName())){
                View listItem = LayoutInflater.from(context).inflate(R.layout.my_message,parent,false);
                TextView messageBody = listItem.findViewById(R.id.message_body);
                messageBody.setText(message.substring(1));
                return listItem;
            }
            else{
                View listItem = LayoutInflater.from(context).inflate(R.layout.their_message,parent,false);
                TextView messageBody = listItem.findViewById(R.id.message_body);
                TextView senderName = listItem.findViewById(R.id.name);
                senderName.setText(this.senderName);
                messageBody.setText(message.substring(1));
                return listItem;
            }
        } else if (message.charAt(0) == 'P'){
            if(this.senderName.equals(App.user.getName())){
                View listItem = LayoutInflater.from(context).inflate(R.layout.my_image_message,parent,false);
                ImageView messageBody = listItem.findViewById(R.id.myImageMessage);
                messageBody.setImageBitmap(bitmap);
                return listItem;
            }
            else{
                View listItem = LayoutInflater.from(context).inflate(R.layout.their_image_message,parent,false);
                ImageView messageBody = listItem.findViewById(R.id.theirImageMessage);
                TextView senderName = listItem.findViewById(R.id.name);
                senderName.setText(this.senderName);
                messageBody.setImageBitmap(bitmap);
                return listItem;
            }
        } else if (message.charAt(0) == 'V'){

            if(this.senderName.equals(App.user.getName())){
                View listItem = LayoutInflater.from(context).inflate(R.layout.my_video_message,parent,false);
                VideoView videoView = listItem.findViewById(R.id.myVideoMessage);

                App.user.getChatList().get(getChatIndex()).setUri(uri);
                videoView.seekTo( 1 );
                return listItem;
            }
            else{
                View listItem = LayoutInflater.from(context).inflate(R.layout.their_video_message,parent,false);
                VideoView videoView = listItem.findViewById(R.id.theirVideoMessage);
                TextView senderName = listItem.findViewById(R.id.name);
                senderName.setText(this.senderName);

                App.user.getChatList().get(getChatIndex()).setUri(uri);
                videoView.setVideoURI(uri);
                videoView.seekTo(1);
                return listItem;
            }
        } else if (message.charAt(0) == 'A'){

            if(this.senderName.equals(App.user.getName())){

                View listItem = LayoutInflater.from(context).inflate(R.layout.my_video_message,parent,false);
                VideoView videoView = listItem.findViewById(R.id.myVideoMessage);

                videoView.setVideoURI(uri);
                videoView.seekTo( 1 );

                return listItem;
            }
            else{

                View listItem = LayoutInflater.from(context).inflate(R.layout.their_video_message,parent,false);
                VideoView videoView = listItem.findViewById(R.id.theirVideoMessage);

                TextView senderName = listItem.findViewById(R.id.name);
                senderName.setText(this.senderName);

                videoView.setVideoURI(uri);
                videoView.seekTo(1);

                return listItem;
            }
        } else if (message.charAt(0) == 'R'){

            if(this.senderName.equals(App.user.getName())){

                View listItem = LayoutInflater.from(context).inflate(R.layout.my_pdf_message,parent,false);
//                VideoView videoView = listItem.findViewById(R.id.myVideoMessage);
//
//                videoView.setVideoURI(uri);
//                videoView.seekTo( 1 );

                return listItem;
            }
            else{

                View listItem = LayoutInflater.from(context).inflate(R.layout.my_pdf_message,parent,false);
//                VideoView videoView = listItem.findViewById(R.id.theirVideoMessage);

                TextView senderName = listItem.findViewById(R.id.name);
                senderName.setText(this.senderName);

//                videoView.setVideoURI(uri);
//                videoView.seekTo(1);

                return listItem;
            }
        }  else if (message.charAt(0) == 'O'){

            if(this.senderName.equals(App.user.getName())){

                View listItem = LayoutInflater.from(context).inflate(R.layout.my_message,parent,false);
//                VideoView videoView = listItem.findViewById(R.id.myVideoMessage);

//                videoView.setVideoURI(uri);
//                videoView.seekTo( 1 );

                return listItem;
            }
            else{

                View listItem = LayoutInflater.from(context).inflate(R.layout.their_message,parent,false);
//                VideoView videoView = listItem.findViewById(R.id.theirVideoMessage);

                TextView senderName = listItem.findViewById(R.id.name);
                senderName.setText(this.senderName);

//                videoView.setVideoURI(uri);
//                videoView.seekTo(1);

                return listItem;
            }
        }


        else return null;

    }

    @NonNull
    public String getChatId() {
        return chatId;
    }

    public void setChatId(@NonNull String chatId) {
        this.chatId = chatId;
    }

    @Override
    public String toString(){
        return message;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        this.bitmap = Utils.loadImageFromStorage(this.imagePath);
    }

    public int getChatIndex(){
        for (int i=0; i < App.user.getChatList().size(); i++){
            if (App.user.getChatList().get(i).getId().equals(chatId))
                return i;
        }
        return -1;
    }
}
