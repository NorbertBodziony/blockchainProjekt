package datagramInterfaces;

import database.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static datagramInterfaces.ErrorCode.OK;

public class FindPublicKey extends WalletRequest {
    private String firstName;
    private String lastName;

    public FindPublicKey(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public NodeRespond handle(Connection con) throws SQLException, IOException {
        List<String> publicKeys = Database.findPublicKey(con, firstName, lastName);
        return new FindPublicKeyRespond(OK, publicKeys);
    }
}
