package database;

import java.io.Serializable;
import java.sql.Timestamp;

public class Transaction implements Serializable {
    private String sender;
    private String recipient;
    private int amount;
    private Timestamp transactionTime;

    public Transaction(String sender, String recipient, int amount, Timestamp transactionTime) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.transactionTime = transactionTime;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", amount=" + amount +
                ", transactionTime=" + transactionTime +
                '}';
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getAmount() {
        return amount;
    }
}
