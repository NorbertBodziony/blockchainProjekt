package cryptography;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SHA256Helper {
    private static final MessageDigest DIGEST;
    private static final Charset ASCII;
    static {
        try {
            DIGEST = MessageDigest.getInstance("SHA-256");
            ASCII = Charset.forName("ASCII");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        }
    }

    public static String generateHash(String data) {
        byte[] hash = DIGEST.digest(data.getBytes(ASCII));
        return CryptoConverter.bytesToHexString(hash);
    }
}

