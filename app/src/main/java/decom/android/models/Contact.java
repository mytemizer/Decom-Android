package decom.android.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import java.util.ArrayList;

import decom.android.R;
import decom.android.core.App;
import decom.android.utils.Utils;

@Entity(tableName = "contact_table" )
public class Contact {

    @Ignore
    private transient Bitmap profileFoto;

    @Ignore
    private byte[] profileFotoByteArray;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @Ignore
    private ArrayList<Message> messageArrayList;

    @NonNull
    @ColumnInfo(name = "ip_address")
    private String ipAddress;

    @Ignore
    private boolean checked = false;


    public Contact(@NonNull String name, @NonNull String id, @NonNull String ipAddress) {
        this.name = name;
        this.id = id;
        this.ipAddress = ipAddress;
        messageArrayList = new ArrayList<>();
        this.profileFoto = BitmapFactory.decodeResource(App.resources, R.drawable.decom);
        this.profileFotoByteArray = Utils.bitmapToByteArray(this.profileFoto);                           // create byte array from image
    }

    public Contact(@NonNull String name, Bitmap profileFoto) {
        this.profileFotoByteArray = Utils.bitmapToByteArray(profileFoto);                           // create byte array from image
        this.name = name;
        this.ipAddress = "notValidIp";
        System.out.print("myPrints " + Utils.getMd5String(name));
        this.id = Utils.getMd5String(name);
        this.profileFoto = profileFoto;
        messageArrayList = new ArrayList<>();
    }

    public void addMessage(Message message){
        if(messageArrayList != null)
            messageArrayList.add(message);

    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public Bitmap getProfileFoto() {
        if(profileFoto == null){
            profileFoto = Utils.byteArrayToBitmap(profileFotoByteArray);
        }
        return profileFoto;
    }

    public void setProfileFoto(Bitmap profileFoto) {
        this.profileFotoByteArray = Utils.bitmapToByteArray(profileFoto);                           // create byte array from image
        this.profileFoto = profileFoto;
    }

    public ArrayList<Message> getMessageArrayList() {
        return messageArrayList;
    }

    public void setMessageArrayList(ArrayList<Message> messageArrayList) {
        this.messageArrayList = messageArrayList;
    }


    @NonNull
    public String getIpAddress() {
        return ipAddress;
    }


    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setProfileFotoByteArray(byte[] profileFotoByteArray) {
        this.profileFoto = Utils.byteArrayToBitmap(profileFotoByteArray);
        this.profileFotoByteArray = profileFotoByteArray;
    }

    public void setIpAddress(@NonNull String ipAddress) {
        this.ipAddress = ipAddress;
    }

}

