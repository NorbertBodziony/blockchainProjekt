package datagramInterfaces;

import database.Database;
import database.Transaction;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static datagramInterfaces.ErrorCode.OK;
import static datagramInterfaces.TransactionRespond.MAX_IN_ONE_PACKET;

public class GetTransactionHistory extends WalletRequest{
    private String publicKey;
    private boolean direction;  // 1-incoming, 0-outgoing
    private String startTime;
    private String stopTime;

    /*public GetTransactionHistory(boolean direction) {
        this.direction = direction;
    }

    public GetTransactionHistory(boolean direction, String publicKey) {
        this.publicKey = publicKey;
        this.direction = direction;
    }*/

    public GetTransactionHistory(boolean direction, String publicKey, String startTime, String stopTime) {
        this.publicKey = publicKey;
        this.direction = direction;
        this.startTime = startTime;
        this.stopTime = stopTime;
    }

    public List<NodeRespond> handleHistory(Connection con) throws SQLException {
        List<NodeRespond> responds = new LinkedList<>();
        List<Transaction> transactions = getTransaction(con, this);
        List<Transaction> inOnePacket = new LinkedList<>();

        if(transactions.size() == 0)
            responds.add(new TransactionRespond(OK, new LinkedList<>(), true));
        int i = 0;
        boolean end;
        while (i < transactions.size()) {
            inOnePacket.add(transactions.get(i++));
            if(inOnePacket.size() >= MAX_IN_ONE_PACKET || i == transactions.size()) {
                if(i == transactions.size())
                    end = true;
                else
                    end = false;
                responds.add(new TransactionRespond(OK, new LinkedList<>(inOnePacket), end));
                inOnePacket.clear();
            }
        }
        return responds;
    }

    @Override
    public NodeRespond handle(Connection con) throws SQLException, IOException {
        List<Transaction> transactions = getTransaction(con, this);
        List<Transaction> last4 = new LinkedList<>();
        for(int i = 0 ; i < MAX_IN_ONE_PACKET; ++i){
            if(transactions.size() - 1 <= i)
                break;
            last4.add(transactions.get(i));
        }
        return new TransactionRespond(OK, last4, true);
    }

    private static List<Transaction> getTransaction(Connection con, GetTransactionHistory r) throws SQLException {
        // incoming
        if(r.direction) {
            if(r.publicKey != null) {
                if(r.startTime != null && r.stopTime != null)
                    return Database.getAllIncomingTransactions(con, r.publicKey, r.startTime, r.stopTime);

                return Database.getAllIncomingTransactions(con, r.publicKey);
            }
            if(r.startTime != null && r.stopTime != null)
                return Database.getAllIncomingTransactions(con, r.startTime, r.stopTime);

            return Database.getAllIncomingTransactions(con);
        }
        // outgoing
        if(r.publicKey != null) {
            if(r.startTime != null && r.stopTime != null)
                return Database.getAllOutgoingTransactions(con, r.publicKey, r.startTime, r.stopTime);

            return Database.getAllOutgoingTransactions(con, r.publicKey);
        }
        if(r.startTime != null && r.stopTime != null)
            return Database.getAllOutgoingTransactions(con, r.startTime, r.stopTime);

        return Database.getAllOutgoingTransactions(con);
    }
}


