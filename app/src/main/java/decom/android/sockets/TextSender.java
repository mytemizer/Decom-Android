package decom.android.sockets;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import decom.android.core.App;
import decom.android.enums.ChatRespond;
import decom.android.models.Chat;
import decom.android.models.Contact;
import decom.android.models.Message;
import decom.android.utils.Constants;
import decom.android.utils.JsonBuilder;

public class TextSender implements Runnable {

    private String textToSend;
    private Chat chat;
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;


    public TextSender(String textToSend, Chat chat) {
        this.chat = chat;
        this.textToSend = textToSend;
    }

    @Override
    public void run() {
        Message message = new Message("M"+textToSend, App.user.getName(), chat.getId());
        Chat.saveMessage(message);

        for (Contact contact : chat.getGroupMembers()){
            socket = SocketBuilder.getSSLSocket(contact.getIpAddress(), Constants.PORT);
            setDataOutputStream();

            try {

                JSONObject jsonObject = JsonBuilder.buildChatRequest(textToSend.length(), chat.getId(), contact.getId());

                if (jsonObject != null) {
                    dataOutputStream.write(jsonObject.toString().getBytes());
                }

                byte[] response = new byte[4096];
                int read = dataInputStream.read(response);
                if (read != -1){
                    String responseString = new String(response);
                    JSONObject responseJson = new JSONObject(responseString);
                    String responseType = responseJson.getString("respond_type");
                    if (responseType.equals(ChatRespond.CHAT_OK.name())){
                        dataOutputStream.write(textToSend.getBytes());
                    }
                }


            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setDataOutputStream(){
        if (socket != null){
            try {
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
