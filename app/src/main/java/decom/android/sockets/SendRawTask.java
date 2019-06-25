package decom.android.sockets;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import decom.android.core.App;
import decom.android.models.Chat;
import decom.android.models.Contact;
import decom.android.utils.Constants;

public class SendRawTask implements Runnable {

    private Socket socket;
    private Chat chat;
    private Uri uri;
    private DataOutputStream dataOutputStream;
    private String fileName;

    public SendRawTask(Chat chat, Uri uri, String fileName){
        this.chat = chat;
        this.uri = uri;
        this.fileName = fileName;
    }

    @Override
    public void run() {

        for (Contact contact : chat.getGroupMembers() ) {
            socket = SocketBuilder.getSSLSocket(contact.getIpAddress(), Constants.PORT);
            setDataOutputStream();

            try {
                String protocolString = Constants.ExternalRequest_RAW;

                dataOutputStream.write(protocolString.getBytes());

                String messageInfo = chat.getId() + Constants.DELIMITER + App.user.getName();

                dataOutputStream.writeInt(messageInfo.length());

                dataOutputStream.writeInt(fileName.length());

                dataOutputStream.write(messageInfo.getBytes());

                dataOutputStream.write(fileName.getBytes());

                final InputStream fileStream = new DataInputStream( new FileInputStream(new File(uri.toString())));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] videoBytes;
                byte[] buf = new byte[1024];
                int n;
                assert fileStream != null;
                while (-1 != (n = fileStream.read(buf)))
                    baos.write(buf, 0, n);
                videoBytes = baos.toByteArray();

                dataOutputStream.write(videoBytes);

                socket.close();
                dataOutputStream.close();

            } catch (IOException e) {
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
