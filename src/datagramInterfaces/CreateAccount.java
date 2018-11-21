package datagramInterfaces;

import account.*;
import database.Database;

import java.sql.Connection;
import java.sql.SQLException;

import static datagramInterfaces.ErrorCode.*;

public class CreateAccount extends WalletRequest {
    private Account account;
    private ReceiveBlock genesisBlock;

    public CreateAccount(Account account, ReceiveBlock genesisBlock) {
        this.account = account;
        this.genesisBlock = genesisBlock;
    }

    @Override
    public String toString() {
        return "CreateAccount{" +
                "account=" + account +
                ", genesisBlock=" + genesisBlock +
                '}';
    }

    public NodeRespond handle(Connection con) throws SQLException {
        boolean result = Database.createAccount(con, account.getAddress(), genesisBlock.getHash(), genesisBlock.getSignature());
        NodeRespond respond;
        if(result)
            respond = new NodeRespond(OK);
        else
            respond = new NodeRespond(CANNOT_CREATE_ACCOUNT);
        return respond;

    }

}
