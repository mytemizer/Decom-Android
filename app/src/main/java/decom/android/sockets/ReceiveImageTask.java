package decom.android.sockets;

import android.graphics.Bitmap;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.SSLServerSocket;

import decom.android.models.Chat;
import decom.android.models.Message;
import decom.android.utils.Utils;

import static decom.android.utils.Utils.saveToInternalStorage;

public class ReceiveImageTask implements Runnable {

    private SSLServerSocket sslServerSocket ;
    private Socket socket;
    private DataInputStream dataInputStream;

    public ReceiveImageTask(Socket socket){
        this.socket = socket;
        setInputStream();
    }

    @Override
    public void run() {
        try {
            int imageSize = dataInputStream.readInt();
            int strSize = dataInputStream.readInt();

            byte[] imageByteArray = new byte[imageSize];
            byte[] messageInfo = new byte[strSize];

            dataInputStream.readFully(messageInfo);

            String messageInfoStr = new String(messageInfo, StandardCharsets.UTF_8);
            String[] parsedInfo = Utils.parseWithDelimiter(messageInfoStr);

            dataInputStream.readFully(imageByteArray);

            Bitmap image = Utils.byteArrayToBitmap(imageByteArray);

            Message message = new Message("P", parsedInfo[1], parsedInfo[0]);
            message.setBitmap(image);
            String path = saveToInternalStorage(image);
            message.setImagePath(path);

            Chat.saveMessage(message);

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
