package decom.android.sockets;

import android.annotation.SuppressLint;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class TrustAllManager implements X509TrustManager {

    @SuppressLint("TrustAllX509TrustManager")
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
        // Intentionally left blank
    }

    @SuppressLint("TrustAllX509TrustManager")
    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
        // Intentionally left blank
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

}
