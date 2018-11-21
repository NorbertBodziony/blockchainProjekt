---------------------------------------------DROP SCHEME-------------------------------------------

ALTER TABLE receive_block ADD CONSTRAINT fk_sender FOREIGN KEY (sender) REFERENCES account(public_key);
ALTER TABLE send_block ADD CONSTRAINT fk_recipient FOREIGN KEY (recipient) REFERENCES account(public_key);
ALTER TABLE block ADD CONSTRAINT fk_send_block FOREIGN KEY (send_type) REFERENCES send_block(id);
ALTER TABLE block ADD CONSTRAINT fk_receive_block FOREIGN KEY (receive_type) REFERENCES receive_block(id);
ALTER TABLE block ADD CONSTRAINT fk_previous_block FOREIGN KEY (previous_block) REFERENCES block(block_id);
ALTER TABLE account ADD CONSTRAINT fk_blockchain FOREIGN KEY (blockchain) REFERENCES blockchain(blockchain_id);
ALTER TABLE block ADD CONSTRAINT unique_receive_type UNIQUE(receive_type);
ALTER TABLE block ADD CONSTRAINT unique_send_type UNIQUE(send_type);
ALTER TABLE block ADD CONSTRAINT unique_previous_block UNIQUE(previous_block);
ALTER TABLE blockchain ADD CONSTRAINT fk_last_block FOREIGN KEY (last_block) REFERENCES block(block_id);


---------------------------------------------DROP TABLE-------------------------------------------
ALTER TABLE receive_block DROP CONSTRAINT fk_sender;
ALTER TABLE send_block DROP CONSTRAINT fk_recipient;
ALTER TABLE block DROP CONSTRAINT fk_send_block;
ALTER TABLE block DROP CONSTRAINT fk_receive_block;
ALTER TABLE block DROP CONSTRAINT fk_block_in_blockchain;
DROP TABLE SEND_BLOCK;
DROP TABLE RECEIVE_BLOCK;
DROP TABLE ACCOUNT;
DROP TABLE BLOCKCHAIN;
DROP TABLE BLOCK;

-------------------------------------------DROP SEQUENCE-----------------------------------------
DROP SEQUENCE BLOCK_ID_SEQ;
DROP SEQUENCE RECEIVE_BLOCK_SEQ;
DROP SEQUENCE SEND_BLOCK_SEQ;
DROP SEQUENCE BLOCKCHAIN_SEQ;


----------------------------------------DROP TRIGGER----------------------------------------------
DROP TRIGGER  block_on_insert;
DROP TRIGGER  receive_block_on_insert;
DROP TRIGGER  send_block_on_insert;


----------------------------------------DROP FUNCTION ----------------------------------------------
DROP FUNCTION CREATE_RECEIVE_BLOCK;
DROP FUNCTION CREATE_SEND_BLOCK;
DROP FUNCTION CREATE_BLOCK;
DROP FUNCTION CREATE_GENESIS_BLOCK;
DROP FUNCTION CREATE_BLOCKCHAIN;
DROP FUNCTION ACCOUNT_VERIFY;
DROP FUNCTION CREATE_ACCOUNT;
DROP FUNCTION GET_FUNDS_FROM_BLOCK;
DROP FUNCTION GET_BALANCE;
DROP FUNCTION TRANSACTION_VERIFY;

----------------------------------------DROP PROCEDURE ----------------------------------------------
DROP PROCEDURE PERFORM_TRANSACTION 
