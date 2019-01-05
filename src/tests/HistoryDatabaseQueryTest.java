package tests;

import database.Database;
import database.Transaction;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class HistoryDatabaseQueryTest {

    @Test
    public void getAllIncomingTransaction() {
        try {
            List<Transaction> transactions;
            Connection con = Database.connect();
            transactions = Database.getAllIncomingTransactions(con);
            for(Transaction t : transactions)
                System.out.println(t);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}