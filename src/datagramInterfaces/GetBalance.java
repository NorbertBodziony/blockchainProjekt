package datagramInterfaces;

import account.Account;
import database.Database;

import java.sql.Connection;
import java.sql.SQLException;

import static datagramInterfaces.ErrorCode.OK;
import static datagramInterfaces.ErrorCode.UNKNOWN_ADDRESS;

public class GetBalance extends WalletRequest {
    private Account account;

    public GetBalance(Account account) {
        this.account = account;
    }

    @Override
    public NodeRespond handle(Connection con) throws SQLException {
        NodeRespond respond;
        System.out.println("Get balance: " + account.getAddress());
        if(!Database.accountExists(con, account.getAddress()))
            respond = new NodeRespond(UNKNOWN_ADDRESS);
        else {
            int balance = Database.getBalance(con, account.getAddress());
            account.setBalance(balance);
            respond = new GetBalanceRespond(OK, balance);
        }
        return respond;
    }
}
