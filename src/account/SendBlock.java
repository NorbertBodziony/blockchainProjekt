package account;

import cryptography.CryptoConverter;

public class SendBlock extends Block {
    private String recipient;

    private SendBlock() { }

    public SendBlock(int amount, String signature, String prevBlock, String recipient){
        super(amount, signature, prevBlock);
        this.recipient = recipient;
        String contents =  amount + recipient + prevBlock + signature;
        super.sha256(contents);
    }

    public SendBlock(int amount, byte[] signature, String prevBlock, String source) {
        this(amount, CryptoConverter.bytesToHexString(signature), prevBlock, source);
    }

    public SendBlock(int amount, String signature, String hash, String prevBlock, String recipient){
        super(amount, signature, prevBlock);
        this.recipient = recipient;
        super.setHash(hash);
    }

    public String getRecipient() {
        return recipient;
    }

    @Override
    public String toString() {
        return recipient + super.getPrevBlock() + super.getSignature() +  super.getAmount()+ super.getHash();
    }

}
