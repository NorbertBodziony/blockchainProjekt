package datagramInterfaces;

public class PreviousHashesRespond extends NodeRespond {
    private String senderPreviousHash;
    private String recipientPreviousHash;

    public PreviousHashesRespond(ErrorCode errorCode, String senderPreviousHash, String recipientPreviousHash) {
        super(errorCode);
        this.senderPreviousHash = senderPreviousHash;
        this.recipientPreviousHash = recipientPreviousHash;
    }

    public String getSenderPreviousHash() {
        return senderPreviousHash;
    }

    public String getRecipientPreviousHash() {
        return recipientPreviousHash;
    }

    @Override
    public String toString() {
        return "senderPreviousHash: " + senderPreviousHash + "\n" +
                "recipientPreviousHash: " + recipientPreviousHash;
    }
}
