------------------------------------create sequece --------------------------------------------------------------

CREATE SEQUENCE block_id_seq increment by 1 start with 1;
CREATE SEQUENCE send_block_seq increment by 1 start with 1;
CREATE SEQUENCE receive_block_seq increment by 1 start with 1;
CREATE SEQUENCE blockchain_seq increment by 1 start with 1;
---------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------
--------------------------------TRIGERS-----------------------------------------------------
CREATE OR REPLACE TRIGGER send_block_on_insert
  BEFORE INSERT ON send_block
  FOR EACH ROW
BEGIN
  SELECT send_block_seq.nextval
  INTO :new.id FROM dual;
END;



CREATE OR REPLACE TRIGGER receive_block_on_insert
  BEFORE INSERT ON receive_block
  FOR EACH ROW
BEGIN
  SELECT receive_block_seq.nextval
  INTO :new.id FROM dual;
END;


CREATE OR REPLACE TRIGGER block_on_insert
  BEFORE INSERT ON block
  FOR EACH ROW
BEGIN
  SELECT block_id_seq.nextval
  INTO :new.block_id FROM dual;
END;


--------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------------FUNTION-------------------------------------------------------

--CREATE_RECEIVE_BLOCK--------------------------------------------
create or replace 
FUNCTION CREATE_RECEIVE_BLOCK 
(
  SENDER IN VARCHAR2  
) RETURN NUMBER AS
  block_id INT;
BEGIN
  INSERT INTO RECEIVE_BLOCK(sender) VALUES(SENDER);
  SELECT MAX(rb.id) INTO block_id FROM receive_block rb;
  
  RETURN block_id;
END CREATE_RECEIVE_BLOCK;
--------------------------------------------------------------------------------------------------------------------------

--CREATE_SEND_BLOCK--------------------------------------------
create or replace 
FUNCTION CREATE_SEND_BLOCK 
(
  RECIPIENT IN VARCHAR2  
) RETURN NUMBER AS
  block_id INT;
BEGIN
  INSERT INTO SEND_BLOCK(recipient) VALUES(RECIPIENT);
  SELECT MAX(sb.id) INTO block_id FROM send_block sb;
  
  RETURN block_id;
END CREATE_SEND_BLOCK;
--------------------------------------------------------------------------------------------------------------------------

--CREATE_BLOCK--------------------------------------------
create or replace 
FUNCTION CREATE_BLOCK 
(
  PREVIOUS_BLOCK INT
, BLOCKCHAIN_ID INT
, SIGNATURE IN VARCHAR2  
, HASH_CODE IN VARCHAR2  
, PREVIOUS_HASH_CODE IN VARCHAR2 
, AMOUNT IN INT
, RECEIVE_TYPE IN INT  
, SEND_TYPE IN INT  
) RETURN NUMBER AS
block_id INT;
last_block INT;
BEGIN
  INSERT INTO BLOCK(previous_block, blockchain_id, signature, hash_code, previous_hash_code, amount, receive_type, send_type)
  VALUES(PREVIOUS_BLOCK, BLOCKCHAIN_ID, SIGNATURE, HASH_CODE, PREVIOUS_HASH_CODE, AMOUNT, RECEIVE_TYPE, SEND_TYPE);
  SELECT MAX(b.block_id) INTO block_id FROM block b;
  RETURN block_id;
END CREATE_BLOCK;
--------------------------------------------------------------------------------------------------------------------------

--CREATE_GENESIS_BLOCK--------------------------------------------
create or replace 
FUNCTION CREATE_GENESIS_BLOCK 
(
  BLOCKCHIAN_ID IN INT
, GENESIS_BALANCE IN INT  
, HASH_CODE IN VARCHAR2
, GENESIS_PREVIOUS_HASH IN VARCHAR2
, SIGNATURE IN VARCHAR2  
) RETURN NUMBER AS 
RECEIVE_TYPE_ID INT;
BLOCK_ID INT;
BLOCKCHAIN_ID INT;
BEGIN
  RECEIVE_TYPE_ID := create_receive_block(sender => null);
  BLOCK_ID := create_block(previous_block => NULL, blockchain_id => BLOCKCHIAN_ID, signature => SIGNATURE, hash_code => HASH_CODE,
    previous_hash_code => GENESIS_PREVIOUS_HASH, amount => GENESIS_BALANCE,
    receive_type => RECEIVE_TYPE_ID, send_type => NULL);
  RETURN BLOCK_ID;
  
END CREATE_GENESIS_BLOCK;
--------------------------------------------------------------------------------------------------------------------------

--CREATE_BLOCKCHAIN--------------------------------------------
create or replace 
FUNCTION CREATE_BLOCKCHAIN 
(
  GENESIS_BALANCE IN INT
, HASH_CODE IN VARCHAR2
, GENESIS_PREVIOUS_HASH IN VARCHAR2
, SIGNATURE IN VARCHAR2  
) RETURN NUMBER AS 
NEW_BLOCKCHAIN_ID INT;
GENSIS_BLOCK_ID INT;
BEGIN
  INSERT INTO blockchain(blockchain_id, last_block) VALUES (blockchain_seq.nextval, NULL);
  SELECT MAX(BLOCKCHAIN_ID) INTO NEW_BLOCKCHAIN_ID FROM blockchain;
  GENSIS_BLOCK_ID := create_genesis_block(blockchian_id => NEW_BLOCKCHAIN_ID, genesis_balance => GENESIS_BALANCE, hash_code => HASH_CODE, genesis_previous_hash => GENESIS_PREVIOUS_HASH, signature => SIGNATURE);
  UPDATE BLOCKCHAIN SET BLOCKCHAIN.last_block = GENSIS_BLOCK_ID WHERE BLOCKCHAIN.blockchain_id = NEW_BLOCKCHAIN_ID;
  
  RETURN NEW_BLOCKCHAIN_ID;
END CREATE_BLOCKCHAIN;
--------------------------------------------------------------------------------------------------------------------------

--ACCOUNT_VERIFY--------------------------------------------
create or replace 
FUNCTION ACCOUNT_VERIFY 
(
  PUB_KEY IN VARCHAR2  
) RETURN INT AS
exist_account INT;
BEGIN
  SELECT COUNT(*) INTO exist_account FROM account WHERE public_key = pub_key;
  IF exist_account = 0 THEN
    RETURN 1;
  ELSE
    RETURN 0;
  END IF;
  /*
  return 0 - konoto o podanym kluczu istnieje
  return 1 - konto o podanym kluczu NIE istnieje
  */
END ACCOUNT_VERIFY;
--------------------------------------------------------------------------------------------------------------------------

--CREATE_ACCOUNT--------------------------------------------
create or replace 
FUNCTION CREATE_ACCOUNT 
(
  PUBLIC_KEY IN VARCHAR2
, GENESIS_BALANCE IN INT  
, HASH_CODE IN VARCHAR2
, GENESIS_PREVIOUS_HASH IN VARCHAR2
, SIGNATURE IN VARCHAR2  
) RETURN NUMBER AS
KEY_NOT_EXIST INT;
BLOCKCHAIN_ID INT;
BEGIN
  KEY_NOT_EXIST := account_verify(PUB_KEY => PUBLIC_KEY);
  IF KEY_NOT_EXIST = 1 THEN
    BLOCKCHAIN_ID := create_blockchain(genesis_balance => GENESIS_BALANCE ,hash_code => HASH_CODE, genesis_previous_hash => GENESIS_PREVIOUS_HASH, signature => SIGNATURE);
    INSERT INTO ACCOUNT(public_key, blockchain) VALUES
    (PUBLIC_KEY, BLOCKCHAIN_ID);  
  END IF;
  RETURN KEY_NOT_EXIST;
  /*
  return 0 - NIE utworzono konta
  return 1 - utworzono konto
  */
END CREATE_ACCOUNT;
--------------------------------------------------------------------------------------------------------------------------

--GET_FUNDS_FROM_BLOCK--------------------------------------------
create or replace 
FUNCTION GET_FUNDS_FROM_BLOCK 
(
  B_ID IN NUMBER  
) RETURN NUMBER AS
RECEIVE_TYPE_ID INT;
SEND_TYPE_ID INT;
FUNDS INT;
BEGIN
  SELECT RECEIVE_TYPE, SEND_TYPE INTO RECEIVE_TYPE_ID, SEND_TYPE_ID FROM BLOCK WHERE BLOCK_ID = B_ID;
  IF RECEIVE_TYPE_ID IS NOT NULL THEN
    SELECT amount INTO FUNDS FROM block WHERE BLOCK_ID = B_ID;
  ELSE
    SELECT -1 * amount INTO FUNDS FROM block WHERE BLOCK_ID = B_ID;
  END IF;
  RETURN FUNDS;
END GET_FUNDS_FROM_BLOCK;
--------------------------------------------------------------------------------------------------------------------------

--GET_PREVIOUS_HASH-------------------------------------------
create or replace 
FUNCTION GET_PREVIOUS_HASH 
(
  PUB_KEY IN VARCHAR2  
) RETURN VARCHAR2 AS
PREVIOUS_HASH VARCHAR2(64);
BEGIN
  SELECT HASH_CODE INTO PREVIOUS_HASH FROM BLOCK WHERE BLOCK.block_id = 
  (SELECT blockchain.last_block FROM blockchain JOIN account on blockchain.blockchain_id = account.blockchain where account.public_key = PUB_KEY);
  RETURN PREVIOUS_HASH;
END GET_PREVIOUS_HASH;



--GET_BALANCE--------------------------------------------
create or replace 
FUNCTION GET_BALANCE 
(
  address IN VARCHAR2  
) RETURN INT AS
balance_result INT := 0;
block_balance INT;
current_block_id INT;
previous_block_id INT;
BEGIN
  --INIT
  SELECT blockchain.last_block INTO current_block_id FROM account JOIN blockchain
  ON account.blockchain = blockchain.blockchain_id WHERE account.public_key = address;
  
  LOOP
   block_balance := GET_FUNDS_FROM_BLOCK(current_block_id);
   balance_result := balance_result + block_balance;
   
   SELECT previous_block INTO previous_block_id FROM block WHERE block_id = current_block_id;
   
   EXIT WHEN previous_block_id IS NULL;
   current_block_id := previous_block_id;

  END LOOP;
  
  RETURN balance_result;
END GET_BALANCE;
--------------------------------------------------------------------------------------------------------------------------

--TRANSACTION_VERIFY--------------------------------------------
create or replace 
FUNCTION TRANSACTION_VERIFY 
(
  SENDER IN VARCHAR2  
, RECIPIENT IN VARCHAR2  
, AMOUNT IN NUMBER   
) RETURN NUMBER AS
recipient_exist INT;
sender_exist INT;
sender_balance INT;
BEGIN
  sender_exist := account_verify(pub_key => SENDER);
  recipient_exist := account_verify(pub_key => RECIPIENT);
  IF sender_exist = 1 OR recipient_exist = 1 THEN
    RETURN 0;
  END IF;

  sender_balance := get_balance(address => SENDER);
  IF sender_balance < AMOUNT THEN
    RETURN 0;
  END IF;
  
  RETURN 1;
  /*
  return 1 - prawidlowa
  return 0 - NIEprawidlowa
  */
END TRANSACTION_VERIFY;
--------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------------PROCEDURE-------------------------------------------------------

--PERFORM_TRANSACTION--------------------------------------------
create or replace 
PROCEDURE PERFORM_TRANSACTION 
(
  SENDER IN VARCHAR2  
, RECIPIENT IN VARCHAR2  
, AMOUNT IN NUMBER  
, SEND_BLOCK_SIGNATURE IN VARCHAR2 
, RECEIVE_BLOCK_SIGNATURE IN VARCHAR2
, SEND_HASH_CODE IN VARCHAR2 
, RECEIVE_HASH_CODE IN VARCHAR2
) AS
send_type_id INT;
receive_type_id INT;
sender_block_id INT;
recipient_block_id INT;
sender_previous_block INT;
recipient_previous_block INT;
sender_previous_hash_code VARCHAR2(64);
recipient_previous_hash_code VARCHAR2(64);
sender_blockchain_id INT;
recipient_blockchain_id INT;
BEGIN
  -- get blockchain
  SELECT account.blockchain INTO sender_blockchain_id  from account join blockchain on account.blockchain = blockchain.blockchain_id WHERE account.public_key = SENDER;
  SELECT account.blockchain INTO recipient_blockchain_id  from account join blockchain on account.blockchain = blockchain.blockchain_id WHERE account.public_key = RECIPIENT;

  -- SENDER
  SELECT last_block INTO sender_previous_block FROM blockchain WHERE blockchain_id = sender_blockchain_id;
  
  SELECT hash_code INTO sender_previous_hash_code FROM block WHERE block_id = sender_previous_block;

  send_type_id := create_send_block(recipient => RECIPIENT);
  
  sender_block_id := create_block(previous_block => sender_previous_block,blockchain_id => sender_blockchain_id, signature => SEND_BLOCK_SIGNATURE, hash_code => SEND_HASH_CODE, 
    previous_hash_code => sender_previous_hash_code, amount => AMOUNT, receive_type => NULL,send_type => send_type_id);
    
  UPDATE blockchain SET last_block = sender_block_id WHERE blockchain.blockchain_id = sender_blockchain_id;

  -- RECIPEINT
  SELECT last_block INTO recipient_previous_block FROM blockchain WHERE blockchain_id = recipient_blockchain_id;
  
  SELECT hash_code INTO recipient_previous_hash_code FROM block WHERE block_id = recipient_previous_block;
  
  receive_type_id := create_receive_block(sender => SENDER);
  
  recipient_block_id := create_block(previous_block => recipient_previous_block, blockchain_id => recipient_blockchain_id,signature => RECEIVE_BLOCK_SIGNATURE, hash_code => RECEIVE_HASH_CODE, 
    previous_hash_code => recipient_previous_hash_code, amount => AMOUNT, receive_type => receive_type_id, send_type => NULL);
    
  UPDATE blockchain SET last_block = recipient_block_id WHERE blockchain.blockchain_id = recipient_blockchain_id;
  
  
END PERFORM_TRANSACTION;
--------------------------------------------------------------------------------------------------------------------------
