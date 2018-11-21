package database;

import constants.Constants;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Formatter;

public class InitDatabase {
    private static StringBuilder sb = new StringBuilder();
    private static Formatter f = new Formatter(sb);

    public static void createSchema(Statement s) throws SQLException {
        execute("CREATE TABLE send_block(\n" +
                "   id INT PRIMARY KEY,\n" +
                "   recipient VARCHAR2(%d) NOT NULL\n)",
                s, Constants.PUBLIC_KEY_LENGTH);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        execute("CREATE TABLE receive_block(\n" +
                "    id INT PRIMARY KEY,\n" +
                "    sender VARCHAR2(%d) NULL\n" +
                ")", s, Constants.PUBLIC_KEY_LENGTH);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        execute("CREATE TABLE block(\n" +
                "    block_id INT PRIMARY KEY,\n" +
                "    blockchain_id INT NOT NULL,\n" +
                "    previous_block INT NULL,\n" +
                "    signature VARCHAR2(%d) NOT NULL,\n" +
                "    hash_code VARCHAR2(%2$d) NOT NULL,\n" +
                "    previous_hash_code VARCHAR2(%2$d) NOT NULL,\n" +
                "    amount INT NOT NULL,\n" +
                "    receive_type INT NULL,\n" +
                "    send_type INT NULL\n" +
                ")", s, Constants.SIGNATURE_LENGTH, Constants.HASH_LENGTH);

        execute("ALTER TABLE block ADD CONSTRAINT fk_send_block FOREIGN KEY (send_type) REFERENCES send_block(id)", s);
        execute("ALTER TABLE block ADD CONSTRAINT fk_receive_block FOREIGN KEY (receive_type) REFERENCES receive_block(id)", s);
        execute("ALTER TABLE block ADD CONSTRAINT fk_previous_block FOREIGN KEY (previous_block) REFERENCES block(block_id)", s);
        execute("ALTER TABLE block ADD CONSTRAINT unique_receive_type UNIQUE(receive_type)", s);
        execute("ALTER TABLE block ADD CONSTRAINT unique_send_type UNIQUE(send_type)", s);
        execute("ALTER TABLE block ADD CONSTRAINT unique_previous_block UNIQUE(previous_block)", s);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        execute("CREATE TABLE blockchain(\n" +
                "    blockchain_id INT PRIMARY KEY,\n" +
                "    last_block INT NULL\n" +
                ")", s);
        execute("ALTER TABLE blockchain ADD CONSTRAINT fk_last_block FOREIGN KEY (last_block) REFERENCES block(block_id)", s);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        execute("CREATE TABLE account(\n" +
                "    public_key VARCHAR2(%d) PRIMARY KEY,\n" +
                "    blockchain INT NOT NULL\n" +
                ")", s, Constants.PUBLIC_KEY_LENGTH);
        execute("ALTER TABLE account ADD CONSTRAINT fk_blockchain FOREIGN KEY (blockchain) REFERENCES blockchain(blockchain_id)", s);
        execute("ALTER TABLE account ADD CONSTRAINT unique_blockchain UNIQUE(blockchain)", s);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        execute("ALTER TABLE receive_block ADD CONSTRAINT fk_sender FOREIGN KEY (sender) REFERENCES account(public_key)", s);
        execute("ALTER TABLE send_block ADD CONSTRAINT fk_recipient FOREIGN KEY (recipient) REFERENCES account(public_key)", s);
        execute("ALTER TABLE block ADD CONSTRAINT fk_block_in_blockchain FOREIGN KEY (blockchain_id) REFERENCES blockchain(blockchain_id)", s);
    }

    public static void createFuntions(Statement s) throws SQLException {
        execute("create or replace \n" +
                "FUNCTION CREATE_RECEIVE_BLOCK \n" +
                "(\n" +
                "  SENDER IN VARCHAR2  \n" +
                ") RETURN NUMBER AS\n" +
                "  block_id INT;\n" +
                "BEGIN\n" +
                "  INSERT INTO RECEIVE_BLOCK(sender) VALUES(SENDER);\n" +
                "  SELECT MAX(rb.id) INTO block_id FROM receive_block rb;\n" +
                "  \n" +
                "  RETURN block_id;\n" +
                "END CREATE_RECEIVE_BLOCK;", s);

        execute("create or replace \n" +
                "FUNCTION CREATE_SEND_BLOCK \n" +
                "(\n" +
                "  RECIPIENT IN VARCHAR2  \n" +
                ") RETURN NUMBER AS\n" +
                "  block_id INT;\n" +
                "BEGIN\n" +
                "  INSERT INTO SEND_BLOCK(recipient) VALUES(RECIPIENT);\n" +
                "  SELECT MAX(sb.id) INTO block_id FROM send_block sb;\n" +
                "  \n" +
                "  RETURN block_id;\n" +
                "END CREATE_SEND_BLOCK;", s);

        execute("create or replace \n" +
                "FUNCTION CREATE_BLOCK \n" +
                "(\n" +
                "  PREVIOUS_BLOCK INT\n" +
                ", BLOCKCHAIN_ID INT\n" +
                ", SIGNATURE IN VARCHAR2  \n" +
                ", HASH_CODE IN VARCHAR2  \n" +
                ", PREVIOUS_HASH_CODE IN VARCHAR2 \n" +
                ", AMOUNT IN INT\n" +
                ", RECEIVE_TYPE IN INT  \n" +
                ", SEND_TYPE IN INT  \n" +
                ") RETURN NUMBER AS\n" +
                "block_id INT;\n" +
                "last_block INT;\n" +
                "BEGIN\n" +
                "  INSERT INTO BLOCK(previous_block, blockchain_id, signature, hash_code, previous_hash_code, amount, receive_type, send_type)\n" +
                "  VALUES(PREVIOUS_BLOCK, BLOCKCHAIN_ID, SIGNATURE, HASH_CODE, PREVIOUS_HASH_CODE, AMOUNT, RECEIVE_TYPE, SEND_TYPE);\n" +
                "  SELECT MAX(b.block_id) INTO block_id FROM block b;\n" +
                "  RETURN block_id;\n" +
                "END CREATE_BLOCK;", s);

        execute("create or replace \n" +
                "FUNCTION CREATE_GENESIS_BLOCK \n" +
                "(\n" +
                "  BLOCKCHIAN_ID IN INT\n" +
                ", GENESIS_BALANCE IN INT  \n" +
                ", HASH_CODE IN VARCHAR2\n" +
                ", GENESIS_PREVIOUS_HASH IN VARCHAR2\n" +
                ", SIGNATURE IN VARCHAR2  \n" +
                ") RETURN NUMBER AS \n" +
                "RECEIVE_TYPE_ID INT;\n" +
                "BLOCK_ID INT;\n" +
                "BLOCKCHAIN_ID INT;\n" +
                "BEGIN\n" +
                "  RECEIVE_TYPE_ID := create_receive_block(sender => null);\n" +
                "  BLOCK_ID := create_block(previous_block => NULL, blockchain_id => BLOCKCHIAN_ID, signature => SIGNATURE, hash_code => HASH_CODE,\n" +
                "    previous_hash_code => GENESIS_PREVIOUS_HASH, amount => GENESIS_BALANCE,\n" +
                "    receive_type => RECEIVE_TYPE_ID, send_type => NULL);\n" +
                "  RETURN BLOCK_ID;\n" +
                "  \n" +
                "END CREATE_GENESIS_BLOCK;", s);

        execute("create or replace \n" +
                "FUNCTION CREATE_BLOCKCHAIN \n" +
                "(\n" +
                "  GENESIS_BALANCE IN INT\n" +
                ", HASH_CODE IN VARCHAR2\n" +
                ", GENESIS_PREVIOUS_HASH IN VARCHAR2\n" +
                ", SIGNATURE IN VARCHAR2  \n" +
                ") RETURN NUMBER AS \n" +
                "NEW_BLOCKCHAIN_ID INT;\n" +
                "GENSIS_BLOCK_ID INT;\n" +
                "BEGIN\n" +
                "  INSERT INTO blockchain(blockchain_id, last_block) VALUES (blockchain_seq.nextval, NULL);\n" +
                "  SELECT MAX(BLOCKCHAIN_ID) INTO NEW_BLOCKCHAIN_ID FROM blockchain;\n" +
                "  GENSIS_BLOCK_ID := create_genesis_block(blockchian_id => NEW_BLOCKCHAIN_ID, genesis_balance => GENESIS_BALANCE,\n" +
                "  hash_code => HASH_CODE, genesis_previous_hash => GENESIS_PREVIOUS_HASH, signature => SIGNATURE);\n" +
                "  UPDATE BLOCKCHAIN SET BLOCKCHAIN.last_block = GENSIS_BLOCK_ID WHERE BLOCKCHAIN.blockchain_id = NEW_BLOCKCHAIN_ID;\n" +
                "  \n" +
                "  RETURN NEW_BLOCKCHAIN_ID;\n" +
                "END CREATE_BLOCKCHAIN;", s);



        execute("create or replace \n" +
                "FUNCTION ACCOUNT_VERIFY \n" +
                "(\n" +
                "  PUB_KEY IN VARCHAR2  \n" +
                ") RETURN INT AS\n" +
                "exist_account INT;\n" +
                "BEGIN\n" +
                "  SELECT COUNT(*) INTO exist_account FROM account WHERE public_key = pub_key;\n" +
                "  IF exist_account = 0 THEN\n" +
                "    RETURN 1;\n" +
                "  ELSE\n" +
                "    RETURN 0;\n" +
                "  END IF;\n" +
                "END ACCOUNT_VERIFY;", s);

        execute("create or replace \n" +
                "FUNCTION CREATE_ACCOUNT \n" +
                "(\n" +
                "  PUBLIC_KEY IN VARCHAR2\n" +
                ", GENESIS_BALANCE IN INT  \n" +
                ", HASH_CODE IN VARCHAR2\n" +
                ", GENESIS_PREVIOUS_HASH IN VARCHAR2\n" +
                ", SIGNATURE IN VARCHAR2  \n" +
                ") RETURN NUMBER AS\n" +
                "KEY_NOT_EXIST INT;\n" +
                "BLOCKCHAIN_ID INT;\n" +
                "BEGIN\n" +
                "  KEY_NOT_EXIST := account_verify(PUB_KEY => PUBLIC_KEY);\n" +
                "  IF KEY_NOT_EXIST = 1 THEN\n" +
                "    BLOCKCHAIN_ID := create_blockchain(genesis_balance => GENESIS_BALANCE ,hash_code => HASH_CODE,\n" +
                "    genesis_previous_hash => GENESIS_PREVIOUS_HASH, signature => SIGNATURE);\n" +
                "    INSERT INTO ACCOUNT(public_key, blockchain) VALUES\n" +
                "    (PUBLIC_KEY, BLOCKCHAIN_ID);  \n" +
                "  END IF;\n" +
                "  RETURN KEY_NOT_EXIST;\n" +
                "END CREATE_ACCOUNT;", s);

        execute("create or replace \n" +
                "FUNCTION GET_FUNDS_FROM_BLOCK \n" +
                "(\n" +
                "  B_ID IN NUMBER  \n" +
                ") RETURN NUMBER AS\n" +
                "RECEIVE_TYPE_ID INT;\n" +
                "SEND_TYPE_ID INT;\n" +
                "FUNDS INT;\n" +
                "BEGIN\n" +
                "  SELECT RECEIVE_TYPE, SEND_TYPE INTO RECEIVE_TYPE_ID, SEND_TYPE_ID FROM BLOCK WHERE BLOCK_ID = B_ID;\n" +
                "  IF RECEIVE_TYPE_ID IS NOT NULL THEN\n" +
                "    SELECT amount INTO FUNDS FROM block WHERE BLOCK_ID = B_ID;\n" +
                "  ELSE\n" +
                "    SELECT -1 * amount INTO FUNDS FROM block WHERE BLOCK_ID = B_ID;\n" +
                "  END IF;\n" +
                "  RETURN FUNDS;\n" +
                "END GET_FUNDS_FROM_BLOCK;", s);

        execute("create or replace \n" +
                "FUNCTION GET_BALANCE \n" +
                "(\n" +
                "  address IN VARCHAR2  \n" +
                ") RETURN INT AS\n" +
                "balance_result INT := 0;\n" +
                "block_balance INT;\n" +
                "current_block_id INT;\n" +
                "previous_block_id INT;\n" +
                "BEGIN\n" +
                "  --INIT\n" +
                "  SELECT blockchain.last_block INTO current_block_id FROM account JOIN blockchain\n" +
                "  ON account.blockchain = blockchain.blockchain_id WHERE account.public_key = address;\n" +
                "  \n" +
                "  LOOP\n" +
                "   block_balance := GET_FUNDS_FROM_BLOCK(current_block_id);\n" +
                "   balance_result := balance_result + block_balance;\n" +
                "   \n" +
                "   SELECT previous_block INTO previous_block_id FROM block WHERE block_id = current_block_id;\n" +
                "   \n" +
                "   EXIT WHEN previous_block_id IS NULL;\n" +
                "   current_block_id := previous_block_id;\n" +
                "\n" +
                "  END LOOP;\n" +
                "  \n" +
                "  RETURN balance_result;\n" +
                "END GET_BALANCE;", s);

        execute("create or replace \n" +
                "FUNCTION GET_PREVIOUS_HASH \n" +
                "(\n" +
                "  PUB_KEY IN VARCHAR2  \n" +
                ") RETURN VARCHAR2 AS\n" +
                "PREVIOUS_HASH VARCHAR2(%d);\n" +
                "BEGIN\n" +
                "  SELECT HASH_CODE INTO PREVIOUS_HASH FROM BLOCK WHERE BLOCK.block_id = \n" +
                "  (SELECT blockchain.last_block FROM blockchain JOIN account on blockchain.blockchain_id = account.blockchain where account.public_key = PUB_KEY);\n" +
                "  RETURN PREVIOUS_HASH;\n" +
                "END GET_PREVIOUS_HASH;", s, Constants.HASH_LENGTH);

        execute("create or replace \n" +
                "FUNCTION TRANSACTION_VERIFY \n" +
                "(\n" +
                "  SENDER IN VARCHAR2  \n" +
                ", RECIPIENT IN VARCHAR2  \n" +
                ", AMOUNT IN NUMBER   \n" +
                ") RETURN NUMBER AS\n" +
                "recipient_exist INT;\n" +
                "sender_exist INT;\n" +
                "sender_balance INT;\n" +
                "BEGIN\n" +
                "  sender_exist := account_verify(pub_key => SENDER);\n" +
                "  recipient_exist := account_verify(pub_key => RECIPIENT);\n" +
                "  IF sender_exist = 1 OR recipient_exist = 1 THEN\n" +
                "    RETURN 0;\n" +
                "  END IF;\n" +
                "\n" +
                "  sender_balance := get_balance(address => SENDER);\n" +
                "  IF sender_balance < AMOUNT THEN\n" +
                "    RETURN 0;\n" +
                "  END IF;\n" +
                "  \n" +
                "  RETURN 1;\n" +
                "END TRANSACTION_VERIFY;", s);
    }

    public static void createProcedures(Statement s) throws SQLException {
        execute("create or replace \n" +
                "PROCEDURE PERFORM_TRANSACTION \n" +
                "(\n" +
                "  SENDER IN VARCHAR2  \n" +
                ", RECIPIENT IN VARCHAR2  \n" +
                ", AMOUNT IN NUMBER  \n" +
                ", SEND_BLOCK_SIGNATURE IN VARCHAR2 \n" +
                ", RECEIVE_BLOCK_SIGNATURE IN VARCHAR2\n" +
                ", SEND_HASH_CODE IN VARCHAR2 \n" +
                ", RECEIVE_HASH_CODE IN VARCHAR2\n" +
                ") AS\n" +
                "send_type_id INT;\n" +
                "receive_type_id INT;\n" +
                "sender_block_id INT;\n" +
                "recipient_block_id INT;\n" +
                "sender_previous_block INT;\n" +
                "recipient_previous_block INT;\n" +
                "sender_previous_hash_code VARCHAR2(%d);\n" +
                "recipient_previous_hash_code VARCHAR2(%d);\n" +
                "sender_blockchain_id INT;\n" +
                "recipient_blockchain_id INT;\n" +
                "BEGIN\n" +
                "  -- get blockchain\n" +
                "  SELECT account.blockchain INTO sender_blockchain_id  from account join blockchain \n" +
                "  on account.blockchain = blockchain.blockchain_id WHERE account.public_key = SENDER;\n" +
                "  SELECT account.blockchain INTO recipient_blockchain_id  from account join blockchain \n" +
                "  on account.blockchain = blockchain.blockchain_id WHERE account.public_key = RECIPIENT;\n" +
                "\n" +
                "  -- SENDER\n" +
                "  SELECT last_block INTO sender_previous_block FROM blockchain WHERE blockchain_id = sender_blockchain_id;\n" +
                "  SELECT hash_code INTO sender_previous_hash_code FROM block WHERE block_id = sender_previous_block;\n" +
                "  send_type_id := create_send_block(recipient => RECIPIENT);\n" +
                "\n" +
                "  sender_block_id := create_block(previous_block => sender_previous_block,blockchain_id => sender_blockchain_id,\n" +
                "  signature => SEND_BLOCK_SIGNATURE, hash_code => SEND_HASH_CODE, previous_hash_code => sender_previous_hash_code,\n" +
                "  amount => AMOUNT, receive_type => NULL,send_type => send_type_id);\n" +
                "  UPDATE blockchain SET last_block = sender_block_id WHERE blockchain.blockchain_id = sender_blockchain_id;\n" +
                "\n" +
                "  -- RECIPEINT\n" +
                "  SELECT last_block INTO recipient_previous_block FROM blockchain WHERE blockchain_id = recipient_blockchain_id;\n" +
                "  SELECT hash_code INTO recipient_previous_hash_code FROM block WHERE block_id = recipient_previous_block;\n" +
                "  receive_type_id := create_receive_block(sender => SENDER);\n" +
                "  \n" +
                "  recipient_block_id := create_block(previous_block => recipient_previous_block, blockchain_id => recipient_blockchain_id,\n" +
                "  signature => RECEIVE_BLOCK_SIGNATURE, hash_code => RECEIVE_HASH_CODE, previous_hash_code => recipient_previous_hash_code,\n" +
                "  amount => AMOUNT, receive_type => receive_type_id, send_type => NULL);\n" +
                "  UPDATE blockchain SET last_block = recipient_block_id WHERE blockchain.blockchain_id = recipient_blockchain_id;\n" +
                "  \n" +
                "END PERFORM_TRANSACTION;", s, Constants.PUBLIC_KEY_LENGTH, Constants.PUBLIC_KEY_LENGTH);
    }

    public static void createTriggers(Statement s) throws SQLException {
        execute("create or replace \n" +
                "TRIGGER send_block_on_insert\n" +
                "  BEFORE INSERT ON send_block\n" +
                "  FOR EACH ROW\n" +
                "BEGIN\n" +
                "  SELECT send_block_seq.nextval\n" +
                "  INTO :new.id FROM dual;\n" +
                "END;", s);

        execute("create or replace \n" +
                "TRIGGER receive_block_on_insert\n" +
                "  BEFORE INSERT ON receive_block\n" +
                "  FOR EACH ROW\n" +
                "BEGIN\n" +
                "  SELECT receive_block_seq.nextval\n" +
                "  INTO :new.id FROM dual;\n" +
                "END;", s);

        execute("create or replace \n" +
                "TRIGGER block_on_insert\n" +
                "  BEFORE INSERT ON block\n" +
                "  FOR EACH ROW\n" +
                "BEGIN\n" +
                "  SELECT block_id_seq.nextval\n" +
                "  INTO :new.block_id FROM dual;\n" +
                "END;", s);
    }

    public static void createSequences(Statement s) throws SQLException {
        execute("CREATE SEQUENCE block_id_seq increment by 1 start with 1", s);
        execute("CREATE SEQUENCE send_block_seq increment by 1 start with 1", s);
        execute("CREATE SEQUENCE receive_block_seq increment by 1 start with 1", s);
        execute("CREATE SEQUENCE blockchain_seq increment by 1 start with 1", s);
    }

    public static void dropSchema(Statement s) throws SQLException {
        execute("ALTER TABLE receive_block DROP CONSTRAINT fk_sender", s);
        execute("ALTER TABLE send_block DROP CONSTRAINT fk_recipient", s);
        execute("ALTER TABLE block DROP CONSTRAINT fk_send_block", s);
        execute("ALTER TABLE block DROP CONSTRAINT fk_receive_block", s);
        execute("ALTER TABLE block DROP CONSTRAINT fk_block_in_blockchain", s);

        execute("DROP TABLE SEND_BLOCK", s);
        execute("DROP TABLE RECEIVE_BLOCK", s);
        execute("DROP TABLE ACCOUNT", s);
        execute("DROP TABLE BLOCKCHAIN", s);
        execute("DROP TABLE BLOCK", s);
    }

    public static void dropFunctions(Statement s) throws SQLException {
        execute("DROP FUNCTION CREATE_RECEIVE_BLOCK", s);
        execute("DROP FUNCTION CREATE_SEND_BLOCK", s);
        execute("DROP FUNCTION CREATE_BLOCK", s);
        execute("DROP FUNCTION CREATE_GENESIS_BLOCK", s);
        execute("DROP FUNCTION CREATE_BLOCKCHAIN", s);
        execute("DROP FUNCTION ACCOUNT_VERIFY", s);
        execute("DROP FUNCTION CREATE_ACCOUNT", s);
        execute("DROP FUNCTION GET_FUNDS_FROM_BLOCK", s);
        execute("DROP FUNCTION GET_BALANCE", s);
        execute("DROP FUNCTION TRANSACTION_VERIFY", s);
    }

    public static void dropProcedures(Statement s) throws SQLException {
        execute("DROP PROCEDURE PERFORM_TRANSACTION", s);
    }


    public static void dropSequences(Statement s) throws SQLException {
        execute("DROP SEQUENCE block_id_seq", s);
        execute("DROP SEQUENCE send_block_seq", s);
        execute("DROP SEQUENCE receive_block_seq", s);
        execute("DROP SEQUENCE blockchain_seq", s);
    }

    public static void dropTriggers(Statement s) throws SQLException {
        execute("DROP TRIGGER block_on_insert", s);
        execute("DROP TRIGGER receive_block_on_insert", s);
        execute("DROP TRIGGER send_block_on_insert", s);
    }

    private static void execute(String sql, Statement s) throws SQLException {
        sb.append(sql);
        System.out.println(sb.toString()+"\n\n");
        s.executeUpdate(sb.toString());
        sb.setLength(0);
    }

    private static void execute(String sql, Statement s, int arg) throws SQLException {
        f.format(sql, arg);
        System.out.println(sb.toString()+"\n\n");
        s.executeUpdate(sb.toString());
        sb.setLength(0);
    }

    private static void execute(String sql, Statement s, int arg0, int arg1) throws SQLException {
        f.format(sql, arg0, arg1);
        System.out.println(sb.toString()+"\n\n");
        s.executeUpdate(sb.toString());
        sb.setLength(0);
    }

}
