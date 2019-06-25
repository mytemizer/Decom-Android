package decom.android.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import decom.android.R;
import decom.android.activities.ContactListActivity2;
import decom.android.core.App;
import decom.android.db.TypeConverter.ContactIdConverter;
import decom.android.utils.Utils;

@Entity(tableName = "chat_table")
public class Chat {

    @NonNull
    @ColumnInfo(name = "chat_name")
    private String chatName;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "contact_id")
    @NonNull
    @TypeConverters(ContactIdConverter.class)
    private ArrayList<String> contactIds;

    @Ignore
    private ArrayList<Contact> groupMembers;

    @Ignore
    private byte[] profileFotoByteArray;

    @Ignore
    private ArrayList<Message> messageArrayList;

    @Ignore
    private transient Bitmap profileFoto;

    @Ignore
    private Uri uri;

    public Chat(@NonNull String chatName, @NonNull String id, @NonNull ArrayList<String> contactIds){
        this.contactIds = contactIds;
        groupMembers = new ArrayList<>();
        for(int i = 0; i < App.user.contactCount(); i++){
            if (contactIds.contains(App.user.getContactList().get(i).getId()) ){
                groupMembers.add(App.user.getContactList().get(i));

            }
        }
        this.messageArrayList = new ArrayList<>();
        this.profileFoto = BitmapFactory.decodeResource(App.resources, R.drawable.decom);
        this.profileFotoByteArray = Utils.bitmapToByteArray(this.profileFoto);
        this.chatName = chatName;
        this.id = id;
    }

    public Chat(String name){
        groupMembers = new ArrayList<>();
        setMessageArrayList(new ArrayList<Message>());
        setChatName(name);
        id = Utils.getMd5String(name);
    }

    public Chat(String name, Bitmap profileFoto){
        groupMembers = new ArrayList<>();
        contactIds = new ArrayList<>();
        id = Utils.getMd5String(name);
        setProfileFotoByteArray(Utils.bitmapToByteArray(profileFoto));                           // create byte array from image
        setMessageArrayList(new ArrayList<Message>());
        setChatName(name);
        setProfileFoto(profileFoto);
    }

    public void sendFileByteVideo(final byte [] messageByteArray, final int chatIndex) {

    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public static void saveMessage(Message message){
        ContactListActivity2.messageViewModel.insert(message);
    }

    public void setProfileFotoByteArray(byte[] profileFotoByteArray) {
        this.profileFotoByteArray = profileFotoByteArray;
    }

    public byte[] getProfileFotoByteArray() {
        return profileFotoByteArray;
    }

    @NonNull
    public ArrayList<Contact> getGroupMembers() {
        return groupMembers;
    }

    public Bitmap getProfileFoto() {
        return profileFoto;
    }

    @NonNull
    public String getChatName() {
        return chatName;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setGroupMembers(@NonNull ArrayList<Contact> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public Contact getGroupMember(int index){
        return groupMembers.get(index);
    }

    public int getMemberCount(){
        return groupMembers.size();
    }
    private void setChatName(@NonNull String chatName) {
        this.chatName = chatName;
    }

    private void setMessageArrayList(ArrayList<Message> messageArrayList) {
        this.messageArrayList = messageArrayList;
    }

    public ArrayList<Message> getMessageArrayList() {
        return messageArrayList;
    }


    private void setProfileFoto(Bitmap profileFoto) {
        this.profileFoto = profileFoto;
    }

    @NonNull
    public ArrayList<String> getContactIds() {
        return contactIds;
    }
}
