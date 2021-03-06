package account;

import cryptography.CryptoConverter;

import java.io.Serializable;

public class ReceiveBlock extends Block implements Serializable {
    private String source;
    public static final long serialVersionUID = 11L;
    public ReceiveBlock(int amount, String signature, String prevBlock, String source) {
        super(amount, signature, prevBlock);
        this.source = source;
        String blockContents =  amount + source + prevBlock + signature;
        super.sha256(blockContents);
    }

    public ReceiveBlock(int amount, byte[] signature, String prevBlock, String source) {
        this(amount, CryptoConverter.bytesToHexString(signature), prevBlock, source);
    }

    public ReceiveBlock(int amount, String signature, String hash, String prevBlock, String source) {
        super(amount, signature, prevBlock);
        this.source = source;
        super.setHash(hash);
    }

    public String getSource() {
        return source;
    }

    private ReceiveBlock() {}

    @Override
    public String toString() {
        return "source: " + source + "\n" +
                "previousHashCode: " +  super.getPrevBlock() + "\n" +
                "hash: " + super.getHash() + "\n" +
                "amount: " + super.getAmount() + "\n" +
                "signature: " + super.getSignature();
    }



}
