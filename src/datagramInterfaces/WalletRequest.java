package datagramInterfaces;


import java.sql.Connection;
import java.sql.SQLException;

public abstract class WalletRequest extends DatagramMessage {

    public abstract NodeRespond handle(Connection con) throws SQLException;
}
