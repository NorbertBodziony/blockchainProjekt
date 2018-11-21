package account;

import cryptography.SHA256Helper;

import java.io.Serializable;


public abstract class Block implements Serializable {

    private int amount;
    private String signature;
    private String prevBlock;
    private String hash;

    protected Block(){}

    protected Block(int amount, String signature, String prevBlock) {
        this.amount = amount;
        this.signature = signature;
        this.prevBlock = prevBlock;
    }

    protected void sha256(String contents) {
        this.hash = SHA256Helper.generateHash(contents);
    }

    void setHash(String hash) {
        this.hash = hash;
    }

    public int getAmount() {
        return amount;
    }

    public String getSignature() {
        return signature;
    }

    public String getPrevBlock() {
        return prevBlock;
    }

    public String getHash() {
        return hash;
    }

}
