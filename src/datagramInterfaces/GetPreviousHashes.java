package datagramInterfaces;

import database.Database;

import java.sql.Connection;
import java.sql.SQLException;

import static datagramInterfaces.ErrorCode.*;

public class GetPreviousHashes extends WalletRequest {
    private String sender;
    private String recipient;

    public GetPreviousHashes(String sender, String recipient) {
        this.sender = sender;
        this.recipient = recipient;
    }

    @Override
    public NodeRespond handle(Connection con) throws SQLException {
        if(!Database.accountExists(con, sender))
            return new NodeRespond(UNKNOWN_SENDER);
        if(!Database.accountExists(con, recipient))
            return new NodeRespond(UNKNOWN_RECIPIENT);

        String senderHash =  Database.getPreviousHash(con, sender);
        String recipientHash = Database.getPreviousHash(con, recipient);
        return new PreviousHashesRespond(OK, senderHash, recipientHash);
    }

}
