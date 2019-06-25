package decom.android.sockets;


import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.SSLServerSocket;

import decom.android.core.App;
import decom.android.models.Message;
import decom.android.utils.Utils;

import static decom.android.models.Chat.saveMessage;

public class ReceiveRawTask implements Runnable {

    private SSLServerSocket sslServerSocket ;
    private Socket socket;
    private DataInputStream dataInputStream;

    public ReceiveRawTask(Socket socket){
        this.socket = socket;
        setInputStream();
    }

    @Override
    public void run() {
        try {
            int strSize = dataInputStream.readInt();
            int fileNameSize = dataInputStream.readInt();

            byte[] messageData = new byte[16384];
            byte[] messageInfo = new byte[strSize];
            byte[] fileNameByte = new byte[fileNameSize];
            StringBuffer sb = new StringBuffer();
            String messageString;
            int dataReadAmount;

            dataInputStream.readFully(messageInfo);

            String fileName = dataInputStream.readUTF();

            dataInputStream.readFully(fileNameByte);

            String messageInfoStr = new String(messageInfo, StandardCharsets.UTF_8);
            String[] parsedInfo = Utils.parseWithDelimiter(messageInfoStr);


            Message message = new Message("R", parsedInfo[1], parsedInfo[0]);

            messageString = new String(messageData, StandardCharsets.UTF_8);
            sb.append(messageString);

            byte [] result;
            byte [] tempResult = new byte[0];

            dataReadAmount = dataInputStream.read(messageData);

            while (dataReadAmount == 16384){
                result = new byte[messageData.length + tempResult.length];
                System.arraycopy(messageData, 0, result, 0, messageData.length);
                System.arraycopy(tempResult, 0, result, messageData.length, tempResult.length);
                tempResult = result;

                dataReadAmount = dataInputStream.read(messageData);
            }

            result = new byte[messageData.length + tempResult.length];
            System.arraycopy(messageData, 0, result, 0, messageData.length);
            System.arraycopy(tempResult, 0, result, messageData.length, tempResult.length);
            tempResult = result;

            ///////////////////

            String outPath = Utils.saveToStorageRaw(tempResult, fileName);

            App.user.getChatList().get(Utils.findChatIndexFromId(parsedInfo[0])).getMessageArrayList().add(message);
            saveMessage(message);

            socket.close();
            dataInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setInputStream(){
        if (socket != null){
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
