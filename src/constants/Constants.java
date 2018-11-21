package constants;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

public class Constants {
    public static final int NODE_PORT = 6666;
    public static final InetAddress NODE_IP;

    public static final int PRIVATE_KEY_LENGTH = 238;
    public static final int PUBLIC_KEY_LENGTH = 144;
    public static final int HASH_LENGTH = 64;
    public static final int SIGNATURE_LENGTH = 112;

    public static final String GENESIS_PREV_HASH = "0000000000000000000000000000000000000000000000000000000000000000";
    public static final int INITIAL_BALANCE = 100;

    public static final Charset CHARSET;

    static {
        try {
            NODE_IP = InetAddress.getLocalHost();
            CHARSET = Charset.forName("UTF-8");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
