package datagramInterfaces;

import database.Transaction;

import java.util.List;

public class TransactionRespond extends NodeRespond {
    private List<Transaction> transactions;
    private boolean end;
    public static final int MAX_IN_ONE_PACKET = 4;

    public TransactionRespond(ErrorCode errorCode, List<Transaction> transactions, boolean end) {
        super(errorCode);
        this.transactions = transactions;
        this.end = end;
    }

    public boolean isEnd() {
        return end;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
