package cryptography;

import java.security.*;
import java.security.spec.ECGenParameterSpec;


public class EllipticCurveHelper {
    private static final KeyPairGenerator GENERATOR;
    private static final SecureRandom SECURE_RANDOM;
    private static final ECGenParameterSpec PARAMETERS;
    private static final Signature SIGNATURE;

    static {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            GENERATOR = KeyPairGenerator.getInstance("ECDSA", "BC");
            SECURE_RANDOM = SecureRandom.getInstance("SHA1PRNG");
            PARAMETERS = new ECGenParameterSpec("secp192k1");
            GENERATOR.initialize(PARAMETERS, SECURE_RANDOM);
            SIGNATURE = Signature.getInstance("ECDSA", "BC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException();
        }
    }

    public static KeyPair generateKeys() {
        return GENERATOR.generateKeyPair();
    }

    public static byte[] sign(PrivateKey privateKey, String input) throws InvalidKeyException, SignatureException {
       SIGNATURE.initSign(privateKey);
       byte[] strByte = input.getBytes();
       SIGNATURE.update(strByte);
       return SIGNATURE.sign();
    }

    public static boolean verifySignature(PublicKey publicKey, String data, byte[] signature) throws InvalidKeyException, SignatureException {
        SIGNATURE.initVerify(publicKey);
        SIGNATURE.update(data.getBytes());
        return SIGNATURE.verify(signature);
    }

}
