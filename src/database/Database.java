package database;

import account.Account;
import account.ReceiveBlock;
import constants.Constants;
import datagramInterfaces.AddCompany;
import datagramInterfaces.SetPersonalData;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Database {
    public static String driver = "oracle.jdbc.driver.OracleDriver";
    public static String hostname = "localhost";
    public static String dbName = "orcl";
    public static String url = "jdbc:oracle:thin:@" + hostname + ":1521:" + dbName;
    public static String user = "BLOCK";
    public static String password = "admin";

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
                //InitDatabase.dropConstraints(statement);
                InitDatabase.dropSchema(statement);
                InitDatabase.dropSequences(statement);
                InitDatabase.dropProcedures(statement);
                InitDatabase.dropFunctions(statement);
            }else {
                System.out.println("INIT");
                InitDatabase.createSchema(statement);
                //InitDatabase.createConstraints(statement);
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
    public static List<AccountList> GetAccounts(Connection con) throws SQLException {
        List<AccountList> AccountData=new ArrayList<>();
        Statement st = con.createStatement();
        System.out.println("getAccountlist");
        String sql = ("SELECT * FROM ACCOUNT");
        ResultSet rs = st.executeQuery(sql);
        while(rs.next()) {
            String str1 = rs.getString("PUBLIC_KEY");
            int id = rs.getInt("BLOCKCHAIN");
            int Company_Type =rs.getInt("COMPANY_TYPE");
            int Customer_Type= rs.getInt("CUSTOMER_TYPE");
            AccountData.add(new AccountList(str1,id,Company_Type,Customer_Type));
            System.out.println(id);
        }

        return AccountData;
    }
    public static List<ReceiveBlockData> GetReciveBlocks(Connection con) throws SQLException {
        List<ReceiveBlockData> AccountData=new ArrayList<>();
        Statement st = con.createStatement();
        System.out.println("GetReceiveBlocks");
        String sql = ("SELECT * FROM RECEIVE_BLOCK");
        ResultSet rs = st.executeQuery(sql);

        while(rs.next()) {
            String str1 = rs.getString("SENDER");
            int id = rs.getInt("ID");

            AccountData.add(new ReceiveBlockData(id,str1));
            System.out.println(id+"  "+str1);
        }

        return AccountData;
    }
    public static List<SendBlockData> GetSendBlocks(Connection con) throws SQLException {
        List<SendBlockData> AccountData=new ArrayList<>();
        Statement st = con.createStatement();
        System.out.println("SendBlockData");
        String sql = ("SELECT * FROM SEND_BLOCK");
        ResultSet rs = st.executeQuery(sql);

        while(rs.next()) {
            String str1 = rs.getString("RECIPIENT");
            int id = rs.getInt("ID");

            AccountData.add(new SendBlockData(id,str1));
            System.out.println(id+"  "+str1);
        }

        return AccountData;
    }

    public static List<BlockchainData> GetBlockchain(Connection con) throws SQLException {
        List<BlockchainData> AccountData=new ArrayList<>();
        Statement st = con.createStatement();
        System.out.println("GetBlockchain");
        String sql = ("SELECT * FROM BLOCKCHAIN");
        ResultSet rs = st.executeQuery(sql);

        while(rs.next()) {

            int id = rs.getInt("BLOCKCHAIN_ID");

            AccountData.add(new BlockchainData(id));
            System.out.println(id);
        }

        return AccountData;
    }
    public static List<AddressData> GetAddressData(Connection con) throws SQLException {
        List<AddressData> AddressData=new ArrayList<>();
        Statement st = con.createStatement();
        System.out.println("GetAddressData");
        String sql = ("SELECT * FROM Address");
        ResultSet rs = st.executeQuery(sql);

        while(rs.next()) {

            int Address_ID= rs.getInt("ADDRESS_ID");
            String Country=rs.getString("COUNTRY");
            String Postal_code=rs.getString("POSTAL_CODE");
            String City=rs.getString("CITY");
            String Street=rs.getString("STREET");
            String Apartament_number=rs.getString("APARTMENT_NUMBER");

            AddressData.add(new AddressData(Address_ID,Country,Postal_code,City,Street,Apartament_number));
        }

        return AddressData;
    }
    public static List<Company> GetCompany(Connection con) throws SQLException {
        List<Company> Company=new ArrayList<>();
        Statement st = con.createStatement();
        System.out.println("GetAddressData");
        String sql = ("SELECT * FROM COMPANY");
        ResultSet rs = st.executeQuery(sql);

        while(rs.next()) {

            int Company_id=rs.getInt("COMPANY_ID");
            String Company_name=rs.getString("COMPANY_NAME");
            String Sector=rs.getString("SECTOR");
            String Contact_tel=rs.getString("CONTACT_TEL");
            int Address_id=rs.getInt("ADDRESS_ID");
            String Contact_email=rs.getString("CONTACT_EMAIL");

            Company.add(new Company(Company_id,Company_name,Sector,Contact_tel,Address_id,Contact_email));
        }

        return Company;
    }
    public static List<Customer> GetCustomer(Connection con) throws SQLException {
        List<Customer> Customer=new ArrayList<>();
        Statement st = con.createStatement();
        System.out.println("GetAddressData");
        String sql = ("SELECT * FROM CUSTOMER");
        ResultSet rs = st.executeQuery(sql);

        while(rs.next()) {

            int Customer_id=rs.getInt("CUSTOMER_ID");
            int Company_id=rs.getInt("COMPANY_ID");
            int Address_id=rs.getInt("ADDRESS_ID");
            String First_name=rs.getString("FIRST_NAME");
            String Last_name=rs.getString("LAST_NAME");
            String Contact_email=rs.getString("CONTACT_EMAIL");

            Customer.add(new Customer(Customer_id,Company_id,Address_id,First_name,Last_name,Contact_email));
        }

        return Customer;
    }
    public static List<BlockData> GetBlocks(Connection con) throws SQLException {
        List<BlockData> AccountData=new ArrayList<>();
        Statement st = con.createStatement();
        System.out.println("GetBlockchain");
        String sql = ("SELECT * FROM BLOCK ORDER BY BLOCK_ID");
        ResultSet rs = st.executeQuery(sql);

        while(rs.next()) {
            int Block_ID = rs.getInt("BLOCK_ID");
           int  Previous_Block = rs.getInt("PREVIOUS_BLOCK");
            int  Blockchain_ID = rs.getInt("BLOCKCHAIN_ID");
            String  Signature =rs.getString("SIGNATURE");
            String Hash_Code = rs.getString("HASH_CODE");
            String  Previous_Hash_Code =rs.getString("PREVIOUS_HASH_CODE");
          int  Amount = rs.getInt("AMOUNT");
         int   Receive_Type = rs.getInt("RECEIVE_TYPE");
          int  Send_Type = rs.getInt("SEND_TYPE");
            Date  Transaction_time = rs.getDate("TRANSACTION_TIME");


            AccountData.add(new BlockData(Block_ID,Blockchain_ID,Previous_Block,Signature,Hash_Code,Previous_Hash_Code,Amount,Receive_Type,Send_Type,Transaction_time));

        }

        return AccountData;
    }
    public static void InsertBlocks(Connection con,BlockData Account) throws SQLException {

        String sql = ("INSERT INTO BLOCK(Block_ID,BLOCKCHAIN_ID,PREVIOUS_BLOCK,SIGNATURE,HASH_CODE,PREVIOUS_HASH_CODE,AMOUNT,RECEIVE_TYPE,SEND_TYPE,TRANSACTION_TIME) VALUES(?,?,?,?,?,?,?,?,?,?)");
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(4,Account.Signature);
        pstmt.setString(5,Account.Hash_Code);
        if(Account.Previous_Hash_Code!=null){
            pstmt.setString(6,Account.Previous_Hash_Code);}
        else
        {
            pstmt.setString(6,null);
        }
        pstmt.setInt(1,Account.Block_ID);
        pstmt.setInt(2,Account.Blockchain_ID);
        if(Account.Previous_Block!=0){
            pstmt.setInt(3,Account.Previous_Block);}
        else
        {
            pstmt.setObject(3,null);
        }
        pstmt.setInt(7,Account.Amount);
        if(Account.Receive_Type!=0) {
            pstmt.setInt(8, Account.Receive_Type);
        }
        else
        {
            pstmt.setObject(8,null);
        }
        if(Account.Send_Type!=0){
            pstmt.setInt(9,Account.Send_Type);}
        else {

            pstmt.setObject(9,null);
        }
        if (Account.Transaction_time!=null)
        {
            pstmt.setDate(10,Account.Transaction_time);
        }else

        {
            pstmt.setObject(10,null);
        }


        pstmt.executeUpdate();


    }
    public static void InsertBlockchain(Connection con,BlockchainData Account) throws SQLException {

        String sql = ("INSERT INTO BLOCKCHAIN (BLOCKCHAIN_ID,LAST_BLOCK) VALUES(?,?)");
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(2,null);
        pstmt.setInt(1,Account.id);
        pstmt.executeUpdate();


    }
    public static void InsertAddress(Connection con,AddressData Account) throws SQLException {

        String sql = ("INSERT INTO ADDRESS (ADDRESS_ID,COUNTRY,POSTAL_CODE,CITY,STREET,APARTMENT_NUMBER) VALUES(?,?,?,?,?,?)");
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1,Account.Address_ID);
        pstmt.setString(2,Account.Country);
        pstmt.setString(3,Account.Postal_code);
        pstmt.setString(4,Account.City);
        pstmt.setString(5,Account.Street);
        pstmt.setString(6,Account.Apartament_number);

        pstmt.executeUpdate();


    }
    public static void InsertCompany(Connection con,Company Account) throws SQLException {

        String sql = ("INSERT INTO COMPANY (COMPANY_ID,COMPANY_NAME,SECTOR,CONTACT_TEL,ADDRESS_ID,CONTACT_EMAIL) VALUES(?,?,?,?,?,?)");
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1,Account.Company_id);
        pstmt.setString(2,Account.Company_name);
        pstmt.setString(3,Account.Sector);
        pstmt.setString(4,Account.Contact_tel);
        pstmt.setInt(5,Account.Address_id);
        pstmt.setString(6,Account.Contact_email);

        pstmt.executeUpdate();


    }
    public static void InsertCustomer(Connection con,Customer Account) throws SQLException {

        String sql = ("INSERT INTO Customer (CUSTOMER_ID,COMPANY_ID,ADDRESS_ID,FIRST_NAME,LAST_NAME,CONTACT_EMAIL) VALUES(?,?,?,?,?,?)");
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1,Account.Customer_id);
        pstmt.setInt(2,Account.Company_id);
        pstmt.setInt(3,Account.Address_id);
        pstmt.setString(4,Account.First_name);
        pstmt.setString(5,Account.Last_name);
        pstmt.setString(6,Account.Contact_email);

        pstmt.executeUpdate();


    }
    public static void InsertSendBlock(Connection con,SendBlockData Account) throws SQLException {

        String sql = ("INSERT INTO SEND_BLOCK (ID,RECIPIENT) VALUES(?,?)");
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(2,Account.Recipient);
        pstmt.setInt(1,Account.ID);
        pstmt.executeUpdate();


    }
    public static void InsertAccounts(Connection con,AccountList Account) throws SQLException {

        String sql = ("INSERT INTO ACCOUNT (PUBLIC_KEY,BLOCKCHAIN,COMPANY_TYPE,CUSTOMER_TYPE) VALUES(?,?,?,?)");
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1,Account.PublicKey);
        pstmt.setInt(2,Account.id);
        pstmt.setObject(3,Account.Company_Type);
        pstmt.setObject(4,Account.Customer_Type);
        pstmt.executeUpdate();


    }

    public static void InsertReceiveBlock(Connection con,ReceiveBlockData Account) throws SQLException {

        String sql = ("INSERT INTO RECEIVE_BLOCK (ID,SENDER) VALUES(?,?)");
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(2,Account.Sender);
        pstmt.setInt(1,Account.id);
        pstmt.executeUpdate();


    }
    public static String GetLastHash(Connection con, ReceiveBlock block)throws  SQLException{
        Statement st = con.createStatement();
        System.out.println("GetLastHash");
        String sql = ("SELECT HASH_CODE FROM BLOCK WHERE BLOCKCHAIN_ID=(SELECT BLOCKCHAIN FROM ACCOUNT WHERE PUBLIC_KEY=?) AND BLOCK_ID=(SELECT LAST_BLOCK FROM BLOCKCHAIN WHERE BLOCKCHAIN_ID=(SELECT BLOCKCHAIN FROM ACCOUNT WHERE PUBLIC_KEY=?)) ");
        PreparedStatement pstmt = con.prepareStatement(sql);
        System.out.println(block.toString());
        pstmt.setString(1,block.getSource());
        pstmt.setString(2,block.getSource());
        ResultSet rs =pstmt.executeQuery();
        rs.next();
        System.out.println(rs.getString(1));
        return rs.getString(1);
    }
    public static void InsertLastBlocks(Connection con) throws  SQLException
    {
        Statement st = con.createStatement();
        String sql = ("SELECT max(BLOCK_ID) FROM block group by BLOCKCHAIN_ID order by Blockchain_id");
        ResultSet rs = st.executeQuery(sql);
        int i=1;
        while(rs.next())
        {
            sql = ("Update blockchain set last_block =? where Blockchain_id =?");
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1,rs.getInt(1));
            pstmt.setInt(2,i);
            pstmt.executeUpdate();
            i++;
        }
    }
    public static void UpdateSeq(Connection con) throws SQLException {
        Statement st = con.createStatement();
        String sql = ("SELECT count(*) FROM Account");
        ResultSet rs = st.executeQuery(sql);
        rs.next();
        int i=20+rs.getInt(1);
        sql = ("alter SEQUENCE Blockchain_seq restart start with ");
        sql=sql+i;
        PreparedStatement pstmt = con.prepareStatement(sql);
        System.out.println(sql);
        pstmt.executeUpdate();
    }
    public static  boolean AccountExist(Connection con, Account account) throws SQLException {
        String sql = ("Select * from account where public_key=? ");

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1,account.getAddress());
        ResultSet rs =pstmt.executeQuery();
        if(rs.next())
        {
            return true;
        }else
        {
            return false;
        }
    }

    private static List<Transaction> getTransaction(ResultSet result) throws SQLException {
        List<Transaction> transactions = new LinkedList<>();
        while (result.next()) {
            Transaction transaction = new Transaction(
                    result.getString("SENDER"),
                    result.getString("RECIPIENT"),
                    result.getInt("AMOUNT"),
                    result.getTimestamp("TRANSACTION_TIME")
            );
            transactions.add(transaction);
        }
        result.close();
        return transactions;
    }

    public static List<Transaction> getAllOutgoingTransactions(Connection con) throws SQLException {
        String sql =    "SELECT SEND_BLOCK.RECIPIENT, ACCOUNT.PUBLIC_KEY SENDER, BLOCK.AMOUNT, TRANSACTION_TIME" +
                " FROM BLOCK JOIN SEND_BLOCK ON BLOCK.SEND_TYPE = SEND_BLOCK.ID JOIN BLOCKCHAIN ON" +
                " BLOCK.BLOCKCHAIN_ID = BLOCKCHAIN.BLOCKCHAIN_ID JOIN ACCOUNT ON" +
                " BLOCKCHAIN.BLOCKCHAIN_ID = ACCOUNT.BLOCKCHAIN ORDER BY TRANSACTION_TIME";

        Statement s = con.createStatement();
        ResultSet result = s.executeQuery(sql);

        List<Transaction> transactions = getTransaction(result);
        s.close();
        return transactions;
    }

    public static List<Transaction> getAllOutgoingTransactions(Connection con, String publicKey) throws SQLException {
        String sql =    "SELECT SEND_BLOCK.RECIPIENT, ACCOUNT.PUBLIC_KEY SENDER, BLOCK.AMOUNT, TRANSACTION_TIME" +
                " FROM BLOCK JOIN SEND_BLOCK ON BLOCK.SEND_TYPE = SEND_BLOCK.ID JOIN BLOCKCHAIN ON" +
                " BLOCK.BLOCKCHAIN_ID = BLOCKCHAIN.BLOCKCHAIN_ID JOIN ACCOUNT ON" +
                " BLOCKCHAIN.BLOCKCHAIN_ID = ACCOUNT.BLOCKCHAIN" +
                " WHERE ACCOUNT.PUBLIC_KEY = '" + publicKey + "' ORDER BY TRANSACTION_TIME";

        Statement s = con.createStatement();
        ResultSet result = s.executeQuery(sql);

        List<Transaction> transactions = getTransaction(result);
        s.close();
        return transactions;
    }

    public static List<Transaction> getAllIncomingTransactions(Connection con) throws SQLException {
        String sql =    "SELECT RECEIVE_BLOCK.SENDER, ACCOUNT.PUBLIC_KEY RECIPIENT, BLOCK.AMOUNT, TRANSACTION_TIME " +
                "FROM BLOCK JOIN RECEIVE_BLOCK ON BLOCK.RECEIVE_TYPE = RECEIVE_BLOCK.ID JOIN BLOCKCHAIN ON" +
                "   BLOCK.BLOCKCHAIN_ID = BLOCKCHAIN.BLOCKCHAIN_ID " +
                "JOIN ACCOUNT ON BLOCKCHAIN.BLOCKCHAIN_ID = ACCOUNT.BLOCKCHAIN ORDER BY TRANSACTION_TIME";

        Statement s = con.createStatement();
        ResultSet result = s.executeQuery(sql);

        List<Transaction> transactions = getTransaction(result);
        s.close();
        return transactions;
    }

    public static List<Transaction> getAllIncomingTransactions(Connection con, String publicKey) throws SQLException {
        String sql =    "SELECT RECEIVE_BLOCK.SENDER, ACCOUNT.PUBLIC_KEY RECIPIENT, BLOCK.AMOUNT, TRANSACTION_TIME " +
                "FROM BLOCK JOIN RECEIVE_BLOCK ON BLOCK.RECEIVE_TYPE = RECEIVE_BLOCK.ID JOIN BLOCKCHAIN ON" +
                "   BLOCK.BLOCKCHAIN_ID = BLOCKCHAIN.BLOCKCHAIN_ID " +
                "JOIN ACCOUNT ON BLOCKCHAIN.BLOCKCHAIN_ID = ACCOUNT.BLOCKCHAIN " +
                "WHERE ACCOUNT.PUBLIC_KEY = '" + publicKey + "' ORDER BY TRANSACTION_TIME";

        Statement s = con.createStatement();
        ResultSet result = s.executeQuery(sql);
        List<Transaction> transactions = getTransaction(result);
        s.close();
        return transactions;
    }

    public static List<Transaction> getAllIncomingTransactions(Connection con, String startTime, String stopTime) throws SQLException {
        // data format = '04.01.2019 21:33:38'
        System.out.println(startTime + " " + stopTime);
        String sql =    "SELECT RECEIVE_BLOCK.SENDER, ACCOUNT.PUBLIC_KEY RECIPIENT, BLOCK.AMOUNT, TRANSACTION_TIME " +
                "FROM BLOCK JOIN RECEIVE_BLOCK ON BLOCK.RECEIVE_TYPE = RECEIVE_BLOCK.ID " +
                "JOIN BLOCKCHAIN ON BLOCK.BLOCKCHAIN_ID = BLOCKCHAIN.BLOCKCHAIN_ID JOIN ACCOUNT ON BLOCKCHAIN.BLOCKCHAIN_ID = ACCOUNT.BLOCKCHAIN " +
                "WHERE TRANSACTION_TIME >= to_date('"+ startTime +"', 'DD.MM.YYYY HH24:MI:SS') " +
                "AND TRANSACTION_TIME <= to_date('"+ stopTime +"', 'DD.MM.YYYY HH24:MI:SS') ORDER BY TRANSACTION_TIME";

        Statement s = con.createStatement();
        ResultSet result = s.executeQuery(sql);

        List<Transaction> transactions = getTransaction(result);
        s.close();
        return transactions;
    }

    public static List<Transaction> getAllIncomingTransactions(Connection con, String publicKey, String startTime, String stopTime) throws SQLException {
        String sql =    "SELECT RECEIVE_BLOCK.SENDER, ACCOUNT.PUBLIC_KEY RECIPIENT, BLOCK.AMOUNT, TRANSACTION_TIME " +
                "FROM BLOCK JOIN RECEIVE_BLOCK ON BLOCK.RECEIVE_TYPE = RECEIVE_BLOCK.ID " +
                "JOIN BLOCKCHAIN ON BLOCK.BLOCKCHAIN_ID = BLOCKCHAIN.BLOCKCHAIN_ID JOIN ACCOUNT ON BLOCKCHAIN.BLOCKCHAIN_ID = ACCOUNT.BLOCKCHAIN " +
                "WHERE ACCOUNT.PUBLIC_KEY = '"+ publicKey +"' AND TRANSACTION_TIME >= to_date('"+ startTime +"', 'DD.MM.YYYY HH24:MI:SS') " +
                "AND TRANSACTION_TIME <= to_date('"+ stopTime +"', 'DD.MM.YYYY HH24:MI:SS') ORDER BY TRANSACTION_TIME";

        Statement s = con.createStatement();
        ResultSet result = s.executeQuery(sql);

        List<Transaction> transactions = getTransaction(result);
        s.close();
        return transactions;
    }

    public static List<Transaction> getAllOutgoingTransactions(Connection con, String startTime, String stopTime) throws SQLException {
        // data format = '04.01.2019 21:33:38'
        System.out.println(startTime + " " + stopTime);
        String sql =     "SELECT SEND_BLOCK.RECIPIENT, ACCOUNT.PUBLIC_KEY SENDER, BLOCK.AMOUNT, TRANSACTION_TIME " +
                "FROM BLOCK JOIN SEND_BLOCK ON BLOCK.SEND_TYPE = SEND_BLOCK.ID JOIN BLOCKCHAIN ON" +
                " BLOCK.BLOCKCHAIN_ID = BLOCKCHAIN.BLOCKCHAIN_ID JOIN ACCOUNT ON BLOCKCHAIN.BLOCKCHAIN_ID = ACCOUNT.BLOCKCHAIN " +
                "WHERE TRANSACTION_TIME >= to_date('"+ startTime +"', 'DD.MM.YYYY HH24:MI:SS') " +
                "AND TRANSACTION_TIME <= to_date('"+ stopTime +"', 'DD.MM.YYYY HH24:MI:SS') ORDER BY TRANSACTION_TIME";

        Statement s = con.createStatement();
        ResultSet result = s.executeQuery(sql);

        List<Transaction> transactions = getTransaction(result);
        s.close();
        return transactions;
    }

    public static List<Transaction> getAllOutgoingTransactions(Connection con, String publicKey, String startTime, String stopTime) throws SQLException {
        String sql =     "SELECT SEND_BLOCK.RECIPIENT, ACCOUNT.PUBLIC_KEY SENDER, BLOCK.AMOUNT, TRANSACTION_TIME " +
                "FROM BLOCK JOIN SEND_BLOCK ON BLOCK.SEND_TYPE = SEND_BLOCK.ID JOIN BLOCKCHAIN ON" +
                " BLOCK.BLOCKCHAIN_ID = BLOCKCHAIN.BLOCKCHAIN_ID JOIN ACCOUNT ON BLOCKCHAIN.BLOCKCHAIN_ID = ACCOUNT.BLOCKCHAIN " +
                "WHERE ACCOUNT.PUBLIC_KEY = '"+ publicKey +"' AND TRANSACTION_TIME >= to_date('"+ startTime +"', 'DD.MM.YYYY HH24:MI:SS') " +
                "AND TRANSACTION_TIME <= to_date('"+ stopTime +"', 'DD.MM.YYYY HH24:MI:SS') ORDER BY TRANSACTION_TIME";

        Statement s = con.createStatement();
        ResultSet result = s.executeQuery(sql);

        List<Transaction> transactions = getTransaction(result);
        s.close();
        return transactions;
    }

    public static int addCompany(Connection con, AddCompany company) throws SQLException {
        String sql = "{? = call ADD_COMPANY(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        CallableStatement cs = con.prepareCall(sql);
        cs.setString(2, company.getCompanyName());
        cs.setString(3, company.getSector());
        cs.setString(4, company.getContactTel());
        cs.setString(5, company.getContactEmail());
        cs.setString(6, company.getCountry());
        cs.setString(7, company.getPostalCode());
        cs.setString(8, company.getCity());
        cs.setString(9, company.getStreet());
        cs.setString(10, company.getApartmentNumber());

        cs.registerOutParameter(1, Types.INTEGER);
        cs.execute();

        int companyId = cs.getInt(1);
        cs.close();
        return companyId;
    }

    public static void setPersonalData(Connection con, SetPersonalData data) throws SQLException {
        String sql = "{call SET_PERSONAL_DATA(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, data.getPublicKey());
        ps.setInt(2, data.getCompanyId());
        ps.setString(3, data.getFirstName());
        ps.setString(4, data.getLastName());
        ps.setString(5, data.getContactEmail());
        ps.setString(6, data.getCountry());
        ps.setString(7, data.getPostalCode());
        ps.setString(8, data.getCity());
        ps.setString(9, data.getStreet());
        ps.setString(10, data.getApartmentNumber());

        ps.executeUpdate();
    }

    public static List<String> findPublicKey(Connection con, String firstName, String secondName)
            throws SQLException {

        String sql = "SELECT PUBLIC_KEY FROM ACCOUNT JOIN CUSTOMER ON " +
                "CUSTOMER_TYPE = CUSTOMER_ID " +
                "WHERE FIRST_NAME LIKE '" + firstName + "%' " +
                "AND LAST_NAME LIKE '" + secondName + "%'";

        PreparedStatement ps = con.prepareStatement(sql);

        ResultSet result = ps.executeQuery();
        List<String> publicKey = new LinkedList<>();

        while (result.next() && publicKey.size() < 5) {
            publicKey.add(result.getString("PUBLIC_KEY"));
        }
        return publicKey;
    }

}
