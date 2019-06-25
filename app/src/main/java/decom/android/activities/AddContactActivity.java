package decom.android.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import decom.android.R;
import decom.android.core.App;
import decom.android.models.Contact;
import decom.android.sockets.SocketBuilder;
import decom.android.utils.Constants;
import decom.android.utils.JsonBuilder;
import decom.android.utils.Utils;

public class AddContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    public void addContactToContactList(View view){
        final TextView contactIpAddress = findViewById(R.id.contact_ip_address);
        final String protocolString = Objects.requireNonNull(JsonBuilder.buildMeetRequest()).toString();
//        Contact contact = new Contact(contactName.getText().toString(), contactIpAddress.getText().toString());
        final String contactIP = contactIpAddress.getText().toString();

        Thread getThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = SocketBuilder.getSSLSocket(contactIP, Constants.PORT);
                    if (socket == null || !socket.isConnected()){
                        System.out.println("Error while getting sender socket");
                        Utils.runOnUI(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddContactActivity.this, "Can not Connect to the IP", Toast.LENGTH_LONG).show();
                            }
                        });
                        return;
                    }
                    socket.setSoTimeout(5000);
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.write(protocolString.getBytes());
                    byte[] response = new byte[4096];
                    int read = dataInputStream.read(response);
                    if(read != -1 ){
                        String responseString = new String(response, StandardCharsets.UTF_8);
                        JSONObject responseJson = new JSONObject(responseString);
                        Contact contact = new Contact(responseJson.getString("host_node_alias"), responseJson.getString("host_node_id"), contactIP);
                        App.user.addContact(contact);
                    }else {
                        Utils.runOnUI(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddContactActivity.this, "Can not Connect to the IP", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (IOException | JSONException e) {
                    System.out.println("myPrints Error while getting streams");
                    Utils.runOnUI(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddContactActivity.this, "Can not Connect to the IP", Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
        getThread.start();

        try {
            getThread.join();
            onBackPressed();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
