package decom.android.sockets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import decom.android.core.App;
import decom.android.enums.ExternalRequest;
import decom.android.models.Chat;
import decom.android.models.Contact;
import decom.android.utils.Constants;
import decom.android.utils.JsonBuilder;
import decom.android.utils.Utils;

public class ListenerServer implements Runnable {

    @Override
    public void run() {
        try {

            ServerSocket sslServerSocket = SocketBuilder.getSSlServerSocket(Constants.PORT);

            if (sslServerSocket == null){
                return;
            }

            while (true){
                Socket socket = sslServerSocket.accept();
                byte[] request = new byte[4096];
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                int read = dataInputStream.read(request);

                if (read != -1 ){
                    String handShakeString = new String(request, StandardCharsets.UTF_8);
                    JSONObject jsonObject = new JSONObject(handShakeString);
                    System.out.println(jsonObject);
                    String requestType = jsonObject.getString("request_type");

                    if (requestType.equals(ExternalRequest.MEET.name())){
                        handleMeetRequest(jsonObject, socket);
                    } else if (requestType.equals(ExternalRequest.GROUP.name())){
                        handleGroupCreate(jsonObject, socket);
                    } else if (requestType.equals(ExternalRequest.CHAT.name())){
                        handleChatRequest(jsonObject, socket);
                    } else if(requestType.equals(ExternalRequest.IMAGE.name())){
                        handleImageRequest(socket);
                    } else if(requestType.equals(ExternalRequest.VIDEO.name())){
                        handleVideoRequest(socket);
                    }

//                    switch (requestType) {
//                        case Constants.ExternalRequest_VIDEO:
//                            handleVideoRequest(socket);
//                            break;
//                        case Constants.ExternalRequest_IMAGE:
//                            handleImageRequest(socket);
//                            break;
//                        case Constants.ExternalRequest_RAW:
//                            handleRawRequest(socket);
//                            break;
//                        case Constants.ExternalRequest_DISCOVER:
//                            handleDiscoverRequest();
//                            break;
//                    }
                }

            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleImageRequest(Socket socket){
        ReceiveImageTask receiveImageTask = new ReceiveImageTask(socket);
        new Thread(receiveImageTask).start();
    }

    private void handleVideoRequest(Socket socket) {
        ReceiveVideoTask receiveVideoTask = new ReceiveVideoTask(socket);
        new Thread(receiveVideoTask).start();
    }

    private void handleRawRequest(Socket socket) {
        ReceiveRawTask receiveRawTask = new ReceiveRawTask(socket);
        new Thread(receiveRawTask).start();
    }

    private void handleChatRequest(JSONObject jsonObject, Socket socket) {
        TextReceiver textReceiver = new TextReceiver(jsonObject, socket);
        new Thread(textReceiver).start();
    }

    private void handleGroupCreate(JSONObject jsonObject, Socket socket){
        DataOutputStream dataOutputStream ;
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            ArrayList<String> idList = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray("peers");
            for (int i = 0; i < jsonArray.length(); i++){
                idList.add(jsonArray.getString(i));
            }
            idList.remove(App.user.getId());

            Chat chat;
            if (idList.size() == 1){
                chat = new Chat(Utils.findContactNameFromId(jsonObject.getString("client_node_id")), jsonObject.getString("group_id"), idList);
            }
            else{
                chat = new Chat(jsonObject.getString("group_alias"), jsonObject.getString("group_id"), idList);
            }

            JSONObject response = JsonBuilder.buildGroupOkRespond();
            if (response != null) {
                dataOutputStream.write(response.toString().getBytes());
            }
            App.user.addChat(chat);

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


    private void handleMeetRequest(JSONObject jsonObject, Socket socket) throws IOException {
        String id = null;
        try {
            id = jsonObject.getString("client_node_id");
            int contactIndex = Utils.findContactIndexFromId(id);
            if(contactIndex == -1){
                Contact newContact = new Contact(jsonObject.getString("client_node_alias"), id, socket.getInetAddress().getHostAddress());
                App.user.addContact(newContact);
                JSONObject meetOkRespond = JsonBuilder.buildMeetOkRespond();

                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                if (meetOkRespond != null) {
                    dataOutputStream.write(meetOkRespond.toString().getBytes());
                }

            } else {
                App.user.getContactList().get(contactIndex).setIpAddress(socket.getInetAddress().getHostAddress());
                JSONObject meetOkRespond = JsonBuilder.buildMeetOkRespond();


                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                if (meetOkRespond != null) {
                    dataOutputStream.write(meetOkRespond.toString().getBytes());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }



    }


    private void handleDiscoverRequest(){
        // Discover implement later
    }





}