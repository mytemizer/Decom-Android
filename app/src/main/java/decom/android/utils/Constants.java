package decom.android.utils;

public class Constants {
    /**
     * current "keystore.bks" has public key RSA , signature algorithm SHA256with:RSA
     */
    public static final String[] enabledCipherSuites = { "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA" }; // supported API levels : 20+

    public static final String EXTRA_MESSAGE = "decom.MESSAGE";

    public static final int PORT = 2470;

    public static final int TIME_OUT = 3000; // in milliseconds

    public static final String DELIMITER = "\n";


    public static final String ExternalRequest_CHAT_REQUEST = "2";

    public static final String ExternalRequest_IMAGE= "4";

    public static final String ExternalRequest_VIDEO= "5";

    public static final String ExternalRequest_RAW= "6";
}
