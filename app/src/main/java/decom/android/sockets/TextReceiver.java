package decom.android.sockets;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import decom.android.core.App;
import decom.android.models.Chat;
import decom.android.models.Message;
import decom.android.utils.JsonBuilder;
import decom.android.utils.Utils;

public class TextReceiver implements Runnable {

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private JSONObject jsonObject;


    public TextReceiver(JSONObject jsonObject, Socket socket) {
        this.socket = socket;
        this.jsonObject = jsonObject;
        setInputStream();
    }

    @Override
    public void run() {
        try {
            int contentSize = jsonObject.getInt("content_length");
            String groupId = jsonObject.getString("group_id");
            String clientId = jsonObject.getString("client_node_id");
            String hostId = jsonObject.getString("host_node_id");
            if(hostId.equals(App.user.getId())){
                String clientName = Utils.findContactNameFromId(clientId);
                JSONObject chatOk = JsonBuilder.buildChatOkRespond();
                if (chatOk != null) {
                    dataOutputStream.write(chatOk.toString().getBytes());
                }
                byte[] messageByte = new byte[contentSize];
                dataInputStream.readFully(messageByte);
                String messageStr = new String(messageByte, StandardCharsets.UTF_8);

                Message message = new Message("M"+messageStr, clientName, groupId);
                Chat.saveMessage(message);
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

    private void setInputStream(){
        if (socket != null){
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
