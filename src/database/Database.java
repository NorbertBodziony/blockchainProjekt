package database;

import constants.Constants;

import java.sql.*;
import java.util.Formatter;
import java.util.Scanner;

public class Database {
    public static String driver = "oracle.jdbc.driver.OracleDriver";
    public static String hostname = "localhost";
    public static String dbName = "orcl";
    public static String url = "jdbc:oracle:thin:@" + hostname + ":1521:" + dbName;
    public static String user = "c##blockchain";
    public static String password = "blockchain";

    static {
        if(!loadDriver())
            throw new RuntimeException("cannot load driver");
    }

    public static void main(String[] args) throws SQLException {

        try {
            Connection connection = connect();
            Statement statement = createStatement(connection);
            Scanner in = new Scanner(System.in);
            System.out.println("[I]nit/[D]rop?");
            String decision = in.nextLine();
            if(decision.toLowerCase().equals("d")) {
                System.out.println("DROP");
                InitDatabase.dropTriggers(statement);
                InitDatabase.dropSchema(statement);
                InitDatabase.dropSequences(statement);
                InitDatabase.dropProcedures(statement);
                InitDatabase.dropFunctions(statement);
            }else {
                System.out.println("INIT");
                InitDatabase.createSchema(statement);
                InitDatabase.createSequences(statement);
                InitDatabase.createTriggers(statement);
                InitDatabase.createProcedures(statement);
                InitDatabase.createFuntions(statement);
            }
            closeConnection(connection, statement);

        }catch (SQLException e) {
            e.printStackTrace();
            System.out.println("error");
        }
    }

    public static boolean loadDriver() {
        try {
            Class.forName(driver);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }


    public static Connection connect() {
        try {
            return DriverManager.getConnection(url, user, password);
        }catch (SQLException e) {
            return null;
        }
    }

    public static void closeConnection(Connection connection, Statement s){
        try {
            if(!s.isClosed())
                s.close();
            if(!connection.isClosed())
                connection.close();
        }catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public static Statement createStatement(Connection connection) {
        try {
            return  connection.createStatement();
        } catch (SQLException e) {
            return null;
        }
    }

    public static boolean createAccount(Connection con, String publicKey, String hash,
                                 String signature) throws SQLException {
        String sql = "{? = call CREATE_ACCOUNT(?, ?, ?, ?, ?)}";
        CallableStatement cs = con.prepareCall(sql);

        cs.registerOutParameter(1, Types.INTEGER);
        cs.setString(2, publicKey);
        cs.setInt(3, Constants.INITIAL_BALANCE);
        cs.setString(4, hash);
        cs.setString(5, Constants.GENESIS_PREV_HASH);
        cs.setString(6, signature);
        cs.execute();
        int result = cs.getInt(1);

        cs.close();
        return result == 1;
    }

    public static int getBalance(Connection con, String publicKey) throws SQLException {
        System.out.println(publicKey);
        String sql = "{? = call GET_BALANCE(?)}";
        CallableStatement cs = con.prepareCall(sql);

        cs.registerOutParameter(1, Types.INTEGER);
        cs.setString(2, publicKey);
        cs.execute();
        int result = cs.getInt(1);

        cs.close();
        return result;
    }

    public static String getPreviousHash(Connection con, String publicKey) throws SQLException {
        String sql = "{? = call GET_PREVIOUS_HASH(?)}";
        CallableStatement cs = con.prepareCall(sql);

        cs.registerOutParameter(1, Types.VARCHAR);
        cs.setString(2, publicKey);
        cs.execute();

        String previousHash = cs.getString(1);
        cs.close();
        return previousHash;
    }

    public static void performTransaction(Connection con, String sender, String recipient, int amount, String signature,
                                             String senderHash, String recipientHash) throws SQLException {
        String sql = "{call PERFORM_TRANSACTION(?, ?, ?, ?, ?, ?, ?)}";
        CallableStatement cs = con.prepareCall(sql);

        cs.setString(1, sender);
        cs.setString(2, recipient);
        cs.setInt(3, amount);
        cs.setString(4, signature);
        cs.setString(5, signature);
        cs.setString(6, senderHash);
        cs.setString(7, recipientHash);
        cs.execute();

        cs.close();
    }

    public static boolean accountExists(Connection con, String address) {
        try {
            String sql = "{? = call ACCOUNT_VERIFY(?)}";
            CallableStatement cStatement = con.prepareCall(sql);
            cStatement.registerOutParameter(1, Types.INTEGER);
            cStatement.setString(2, address);
            cStatement.execute();
            int result = cStatement.getInt(1);

            cStatement.close();
            return result == 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


}
