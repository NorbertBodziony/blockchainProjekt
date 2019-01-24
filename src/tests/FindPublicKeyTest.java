package tests;

import constants.Constants;
import database.Database;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertFalse;

public class FindPublicKeyTest {

    @Test
    public void simpleTest() throws SQLException {
        Connection con = Database.connect();
        List<String> result =
                Database.findPublicKey(con,"first", "last");

        for(String publicKey : result) {
            System.out.println(publicKey);
            assertFalse(publicKey.length() != Constants.PUBLIC_KEY_LENGTH);
        }

    }

}