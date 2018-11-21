package cryptography;

import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class CryptoConverter {
    private static final KeyFactory KEY_FACTORY;
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    static {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            KEY_FACTORY = KeyFactory.getInstance("ECDSA", "BC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException();
        }
    }

    public static String bytesToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String bytesToHexString(byte[] bytes, int from, int to)
    {
        int length = to - from ;
        char[] hexChars = new char[length * 2];
        for (int j = 0 ; j < length; j++) {
            int v = bytes[j + from] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String keyToHexString(Key key) {
        byte[] bytes = key.getEncoded();
        return bytesToHexString(bytes);
    }

    public static PublicKey hexToPublicKey(String hex)
            throws InvalidKeySpecException {
        byte[] bytes = hexStringToByteArray(hex);
        EncodedKeySpec privateKeySpec = new X509EncodedKeySpec(bytes);
        return KEY_FACTORY.generatePublic(privateKeySpec);
    }

    public static PrivateKey hexToPrivateKey(String hex)
            throws InvalidKeySpecException {
        byte[] bytes = hexStringToByteArray(hex);
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytes);
        return KEY_FACTORY.generatePrivate(privateKeySpec);
    }
}
