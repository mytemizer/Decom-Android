package decom.android.sockets;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import decom.android.utils.Constants;

public class SocketBuilder {

    public static Socket getSSLSocket(final String ipAddress, final int port){
        try {

//            SSLContext sslContext = SSLContext.getInstance("SSL");
//            sslContext.init(null,new TrustManager[]{new TrustAllManager()}, new SecureRandom());
//
//            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//            final SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket();
//            sslSocket.setEnabledCipherSuites(Constants.enabledCipherSuites);

            final Socket socket = new Socket();
            // Network operation, do it in a thread
            Thread con = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        socket.connect(new InetSocketAddress(ipAddress, port ) ,
                                Constants.TIME_OUT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            con.start();
            con.join();
            return socket;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ServerSocket getSSlServerSocket(int port) {

        try {
//
//            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            keyStore.load(App.resources.openRawResource(R.raw.keystore) ,"gultepe593.".toCharArray() );
//
//            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//            keyManagerFactory.init(keyStore,"gultepe593.".toCharArray());
//
//            SSLContext sslContext = SSLContext.getInstance("SSL");
//
//            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
//
//            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
//            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port);

            //            sslServerSocket.setEnabledCipherSuites(Constants.enabledCipherSuites);

            return new ServerSocket(port);
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
