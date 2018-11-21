package account;

import cryptography.CryptoConverter;
import cryptography.EllipticCurveHelper;

import java.io.Serializable;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

public class Account implements Serializable {
    transient private PrivateKey privateKey;
    private PublicKey publicKey;
    transient private int balance;
    transient private Blockchain blockchain;


    public Account(String privateKey, String publicKey) throws InvalidKeySpecException {
        this.privateKey = CryptoConverter.hexToPrivateKey(privateKey);
        this.publicKey = CryptoConverter.hexToPublicKey(publicKey);
    }


    public Account(String publicKey) throws InvalidKeySpecException {
        this.publicKey = CryptoConverter.hexToPublicKey(publicKey);
    }


    public byte[] sign(String data) throws SignatureException, InvalidKeyException {
        byte[] signature = EllipticCurveHelper.sign(privateKey, data);
        System.out.println("signature length: " + signature.length);
        return signature;
    }

    public Account() {
        KeyPair keyPair = EllipticCurveHelper.generateKeys();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getAddress() {
        return CryptoConverter.keyToHexString(publicKey);
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    @Override
    public String toString() {
        return  "publicKey = " + getAddress() + "\n" +
                "balance = " + balance;
    }
}
