package decom.android.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import java.util.ArrayList;

import decom.android.activities.ContactListActivity2;
import decom.android.utils.Utils;

/**
 * Only one instance will be present in App class
 * instance will represent the authenticated user
 */

@Entity(tableName = "user_table")
public class User {
    @Ignore
    private transient Bitmap profileFoto;

    @ColumnInfo(name = "image_byte")
    private byte[] profileFotoByteArray;

    @ColumnInfo(name = "user_name")
    @NonNull
    private String name;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @Ignore
    // Do not make public
    private ArrayList<Contact> contactList ;

    @Ignore
    // Do not make public
    private ArrayList<Chat> chatList;


    public User(@NonNull String name, @NonNull String id, byte[] profileFotoByteArray){
        this.name = name;
        this.id = id;
        this.contactList = new ArrayList<>();
        this.chatList = new ArrayList<>();
        this.profileFotoByteArray = profileFotoByteArray;
        this.profileFoto = Utils.byteArrayToBitmap(this.profileFotoByteArray);
    }

    public User() {
        this.name = "";
        this.id = Utils.getMd5String(name);
        this.contactList = new ArrayList<>();
        this.chatList = new ArrayList<>();
    }

    public void deepCopy(User user){
        this.name = user.getName();
        this.id = user.getId();
        this.profileFoto = user.getProfileFoto();
        this.profileFotoByteArray = user.profileFotoByteArray;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.id = Utils.getMd5String(name);
    }

    public void setOnlyName(String name) {
        this.name = name;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void addContact(Contact contact){
        ContactListActivity2.saveContact(contact);
    }

    public Bitmap getProfileFoto() {
        if(profileFoto == null){
            profileFoto = Utils.byteArrayToBitmap(profileFotoByteArray);
        }
        return profileFoto;
    }

    public byte[] getProfileFotoByteArray() {
        return profileFotoByteArray;
    }

    public void setProfileFotoByteArray(byte[] profileFotoByteArray) {
        this.profileFotoByteArray = profileFotoByteArray;
        this.profileFoto = Utils.byteArrayToBitmap(this.profileFotoByteArray);
    }

    public void setProfileFoto(Bitmap profileFoto) {
        this.profileFotoByteArray = Utils.bitmapToByteArray(profileFoto);
        this.profileFoto = profileFoto;
    }

    public ArrayList<Chat> getChatList() {
        return chatList;
    }

    public ArrayList<Contact> getContactList() {
        return contactList;
    }

    public void addChat(Chat chat){
        ContactListActivity2.saveChat(chat);
    }

    public int contactCount(){
        return contactList.size();
    }
}
