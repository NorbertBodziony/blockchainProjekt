package datagramInterfaces;
import database.Database;
import account.*;
import node.ClientTCP;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static datagramInterfaces.ErrorCode.*;

public class CreateAccount extends WalletRequest {
    private Account account;
    private ReceiveBlock genesisBlock;
    private List<ClientTCP> clientTCP;
    public CreateAccount(Account account, ReceiveBlock genesisBlock) {
        this.account = account;
        this.genesisBlock = genesisBlock;
    }
    public CreateAccount(Account account, ReceiveBlock genesisBlock,List<ClientTCP> clientTCP) {
        this.account = account;
        this.genesisBlock = genesisBlock;
        this.clientTCP=clientTCP;
    }

    public void setClientTCP(List<ClientTCP> clientTCP) {
        this.clientTCP = clientTCP;
    }

    @Override
    public String toString() {
        return "CreateAccount{" +
                "account=" + account +
                ", genesisBlock=" + genesisBlock +
                '}';
    }

    public NodeRespond handle(Connection con) throws SQLException, IOException {
        boolean result = Database.createAccount(con, account.getAddress(), genesisBlock.getHash(), genesisBlock.getSignature());
        NodeRespond respond;
        if(result) {
            respond = new NodeRespond(OK);
            System.out.println("sending new account");
             if(clientTCP!=null)
             {
                 for(int i=0;i<clientTCP.size();i++)
                 {
                     clientTCP.get(i).SendNewAccount(account,genesisBlock);
                 }
             }
             }
        else
            respond = new NodeRespond(CANNOT_CREATE_ACCOUNT);
        return respond;

    }

}
