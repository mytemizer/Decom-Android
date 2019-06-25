package decom.android.sockets;

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

public class SendVideoTask implements Runnable {

    private Socket socket;
    private Chat chat;
    private byte[] videoBytes;
    private DataOutputStream dataOutputStream;

    public SendVideoTask(Chat chat, byte[] videoBytes){
        this.chat = chat;
        this.videoBytes = videoBytes;
    }

    @Override
    public void run() {

        for (Contact contact : chat.getGroupMembers() ) {
            socket = SocketBuilder.getSSLSocket(contact.getIpAddress(), Constants.PORT);
            setDataOutputStream();

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("request_type", ExternalRequest.VIDEO);

                dataOutputStream.write(jsonObject.toString().getBytes());

                String messageInfo = chat.getId() + Constants.DELIMITER + App.user.getName();

                dataOutputStream.writeInt(messageInfo.length());

                dataOutputStream.write(messageInfo.getBytes());

                dataOutputStream.write(videoBytes);

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
