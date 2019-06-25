package decom.android.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

import decom.android.R;
import decom.android.activities.ContactListActivity2;
import decom.android.core.App;
import decom.android.models.Chat;
import decom.android.models.Contact;
//import org.apache.http.conn.util.InetAddressUtils;

public class Utils {

    /**
     * Get IP address from first non-localhost interface
     * @param useIPv4   true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
    }

    public static String getMd5String(String data){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md.digest(data.getBytes());
            BigInteger bigInt = new BigInteger(1,md5Bytes);
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

    }

    public static Bitmap byteArrayToBitmap(byte[] bytes){
        if(bytes != null)
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        else
            return null;
    }

    public static String[] parseWithDelimiter(String data){
        String[] result =  data.split(Constants.DELIMITER);
        result[result.length-1] = result[result.length-1].replaceAll("\0","");
        return result;
    }

    public static  String[] parseWithDesiredDelimiter(String string, String delimeter ){
        String[] result =  string.split(delimeter);
        result[result.length-1] = result[result.length-1].replaceAll("\0","");
        return result;
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap){
        if (bitmap == null)
            return null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(ContactListActivity2.context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("Decom", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"image.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public static String saveToStorageVideo(byte[] byteArray){
        ContextWrapper cw = new ContextWrapper(ContactListActivity2.context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("Decom", Context.MODE_PRIVATE);
        // Create imageDir
        String fileName = "save_"+ System.currentTimeMillis()+".mp4";
        File mypath = new File(directory, fileName);



        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            fos.write(byteArray);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    public static String saveToStorageRaw(byte[] byteArray, String fileName){
        ContextWrapper cw = new ContextWrapper(ContactListActivity2.context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("Decom", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "save_"+ System.currentTimeMillis()+"_" + fileName);



        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            fos.write(byteArray);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


    public static Bitmap loadImageFromStorage(String path) {

        try {
            File f=new File(path, "image.jpg");
            return BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;

    }



    public static void runOnUI(Runnable r){
        new Handler(Looper.getMainLooper()).post(r);
    }

    public static int findContactIndexFromHostAddress(String address){
        for (int i =0;i< App.user.contactCount(); i++){
            if(App.user.getContactList().get(i).getIpAddress().equals(address)){
                return i;
            }
        }
        return -1;
    }

    public static int findContactIndexFromName(String name){
        for (int i =0;i< App.user.contactCount(); i++){
            if(App.user.getContactList().get(i).getName().equals(name)){
                return i;
            }
        }
        return -1;
    }

    public static void addContactDumy(){
        if(App.user.getContactList() != null ){
            App.user.addContact(new Contact("Yusuf", BitmapFactory.decodeResource(App.resources, R.drawable.decom)));
            App.user.addContact(new Contact("Halit", BitmapFactory.decodeResource(App.resources, R.drawable.decom)));
            App.user.addContact(new Contact("Temizer", BitmapFactory.decodeResource(App.resources, R.drawable.decom)));
            App.user.addContact(new Contact("Irfan", BitmapFactory.decodeResource(App.resources, R.drawable.decom)));
        }
    }

    public static void addChatsDummy(){
        if(App.user.getChatList() != null ){
            App.user.addChat(new Chat("These", BitmapFactory.decodeResource(App.resources, R.drawable.smiley)));
            App.user.addChat(new Chat("Are", BitmapFactory.decodeResource(App.resources, R.drawable.smiley)));
            App.user.addChat(new Chat("Example", BitmapFactory.decodeResource(App.resources, R.drawable.smiley)));
            App.user.addChat(new Chat("Groups", BitmapFactory.decodeResource(App.resources, R.drawable.smiley)));
        }
    }

    public static int findContactIndexFromId(String id){
        for (int i =0;i< App.user.contactCount(); i++){
            if(App.user.getContactList().get(i).getId().equals(id)){
                return i;
            }
        }
        return -1;
    }

    public static String findContactNameFromId(String id){
        for (int i =0;i< App.user.contactCount(); i++){
            if(App.user.getContactList().get(i).getId().equals(id)){
                return App.user.getContactList().get(i).getName();
            }
        }
        return "";
    }

    public static int findChatIndexFromId(String id){
        int size = App.user.getChatList().size();
        for (int i =0;i< size; i++){
            if(App.user.getChatList().get(i).getId().equals(id)){
                return i;
            }
        }
        return -1;
    }

}