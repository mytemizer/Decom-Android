package decom.android.sockets;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import decom.android.core.App;
import decom.android.enums.ExternalRequest;
import decom.android.models.Chat;
import decom.android.models.Contact;
import decom.android.utils.Constants;
import decom.android.utils.Utils;

public class SendImageTask implements Runnable {

    private Socket socket;
    private Chat chat;
    private byte[] imageByteArray;
    private DataOutputStream dataOutputStream;

    public SendImageTask(Chat chat, Bitmap bitmap){
        this.chat = chat;
        imageByteArray = Utils.bitmapToByteArray(bitmap);

    }

    @Override
    public void run() {
        int size = imageByteArray.length;

        for (Contact contact : chat.getGroupMembers() ) {
            socket = SocketBuilder.getSSLSocket(contact.getIpAddress(), Constants.PORT);
            setDataOutputStream();

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("request_type", ExternalRequest.IMAGE);

                dataOutputStream.write(jsonObject.toString().getBytes());

                dataOutputStream.writeInt(size);

                String messageInfo = chat.getId() + Constants.DELIMITER + App.user.getName();

                dataOutputStream.writeInt(messageInfo.length());

                dataOutputStream.write(messageInfo.getBytes());

                dataOutputStream.write(imageByteArray);

                socket.close();
                dataOutputStream.close();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private void setDataOutputStream(){
        if (socket != null){
            try {
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
