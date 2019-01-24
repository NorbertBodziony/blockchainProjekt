package tests;

import database.Database;
import database.Transaction;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HistoryDatabaseQueryTest {

    @Test
    public void getAllIncomingTransaction() {
        try {
            List<Transaction> transactions;
            Connection con = Database.connect();
            transactions = Database.getAllIncomingTransactions(con);
            for(Transaction t : transactions){
                System.out.println(t);
                assertFalse(t == null);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllOutgoingTransaction() {
        try {
            List<Transaction> transactions;
            Connection con = Database.connect();
            transactions = Database.getAllOutgoingTransactions(con);
            for(Transaction t : transactions){
                assertFalse(t == null);
                System.out.println(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAccountOutgoingTransaction() {
        String publicKey =
                "3046301006072A8648CE3D020106052B8104001F033200043EBDE5869B407314403B0E16D0736" +
                        "99A93252F78B3428CF81A15694AE7DA0519C721E6584DE866E76517D8B247917C97";
        try {
            List<Transaction> transactions;
            Connection con = Database.connect();
            transactions = Database.getAllOutgoingTransactions(con, publicKey);
            for(Transaction t : transactions)
                System.out.println(t);

        } catch (SQLException e) {
            assertTrue(false);
            e.printStackTrace();
        }
    }

    @Test
    public void getAccountIncomingTransaction() {
        String publicKey =
                "3046301006072A8648CE3D020106052B8104001F033200043EBDE5869B407314403B0E16D0736" +
                        "99A93252F78B3428CF81A15694AE7DA0519C721E6584DE866E76517D8B247917C97";
        try {
            List<Transaction> transactions;
            Connection con = Database.connect();
            transactions = Database.getAllIncomingTransactions(con, publicKey);
            for(Transaction t : transactions)
                System.out.println(t);

        } catch (SQLException e) {
            assertTrue(false);
            e.printStackTrace();
        }
    }

    @Test
    public void getAllIncomingBetween() {
        try {
            List<Transaction> transactions;
            Connection con = Database.connect();
            transactions = Database.getAllIncomingTransactions(con, "01.01.2019 21:33:38", "20.01.2019 23:00:00");
            for(Transaction t : transactions)
                System.out.println(t);

        } catch (SQLException e) {
            assertTrue(false);
            e.printStackTrace();
        }
    }

    @Test
    public void getAllIncomingAccountBetween() {
        String publicKey =
                "3046301006072A8648CE3D020106052B8104001F033200043EBDE5869B407314403B0E16D0736" +
                        "99A93252F78B3428CF81A15694AE7DA0519C721E6584DE866E76517D8B247917C97";
        try {
            List<Transaction> transactions;
            Connection con = Database.connect();
            transactions = Database.getAllIncomingTransactions(con, publicKey, "01.01.2019 21:33:38", "20.01.2019 23:00:00");
            for(Transaction t : transactions)
                System.out.println(t);

        } catch (SQLException e) {
            assertTrue(false);
            e.printStackTrace();
        }
    }

    @Test
    public void getAllOutgoingBetween() {
        try {
            List<Transaction> transactions;
            Connection con = Database.connect();
            transactions = Database.getAllOutgoingTransactions(con, "01.01.2019 21:33:38", "20.01.2019 23:00:00");
            for(Transaction t : transactions)
                System.out.println(t);

        } catch (SQLException e) {
            assertTrue(false);
            e.printStackTrace();
        }
    }

    @Test
    public void getAllOutgoingAccountBetween() {
        String publicKey =
                "3046301006072A8648CE3D020106052B8104001F033200043EBDE5869B407314403B0E16D0736" +
                        "99A93252F78B3428CF81A15694AE7DA0519C721E6584DE866E76517D8B247917C97";
        try {
            List<Transaction> transactions;
            Connection con = Database.connect();
            transactions = Database.getAllOutgoingTransactions(con, publicKey, "01.01.2019 21:33:38", "20.01.2019 23:00:00");
            for(Transaction t : transactions)
                System.out.println(t);

        } catch (SQLException e) {
            assertTrue(false);
            e.printStackTrace();
        }
    }


}