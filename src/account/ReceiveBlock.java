package account;

import cryptography.CryptoConverter;

public class ReceiveBlock extends Block {
    private String source;

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
