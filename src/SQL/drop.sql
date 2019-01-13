---------------------------------------------DROP SCHEAM-------------------------------------------
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
DROP TABLE CUSTOMER;
DROP TABLE COMPANY;
DROP TABLE ADDRESS;

-------------------------------------------DROP SEQUENCE-----------------------------------------
DROP SEQUENCE BLOCK_ID_SEQ;
DROP SEQUENCE RECEIVE_BLOCK_SEQ;
DROP SEQUENCE SEND_BLOCK_SEQ;
DROP SEQUENCE BLOCKCHAIN_SEQ;
DROP SEQUENCE address_seq;
DROP SEQUENCE customer_seq;
DROP SEQUENCE company_seq;


----------------------------------------DROP TRIGGER----------------------------------------------
DROP TRIGGER  block_on_insert;
DROP TRIGGER  receive_block_on_insert;
DROP TRIGGER  send_block_on_insert;
DROP TRIGGER address_on_insert;
DROP TRIGGER company_on_insert;
DROP TRIGGER customer_on_insert;


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
DROP FUNCTION ADD_COMPANY;

----------------------------------------DROP PROCEDURE ----------------------------------------------
DROP PROCEDURE PERFORM_TRANSACTION 
DROP PROCEDURE SET_PERSONAL_DATA;


-----------------------------------------DROP ONLY CONSTRINTS--------------------------------------
ALTER TABLE receive_block DROP CONSTRAINT fk_sender;
ALTER TABLE send_block DROP CONSTRAINT fk_recipient;
ALTER TABLE block DROP CONSTRAINT fk_send_block;
ALTER TABLE block DROP CONSTRAINT fk_receive_block;
ALTER TABLE block DROP CONSTRAINT fk_block_in_blockchain;
ALTER TABLE account DROP CONSTRAINT fk_company_type;
ALTER TABLE account DROP CONSTRAINT fk_customer_type;
ALTER TABLE customer DROP CONSTRAINT fk_company_id;
ALTER TABLE customer DROP CONSTRAINT fk_address_cust;
ALTER TABLE company DROP CONSTRAINT fk_address_comp;


-----------------------------------------DROP TABLES AFTER DROP CONSTRINTS-------------------------------------
DROP TABLE SEND_BLOCK;
DROP TABLE RECEIVE_BLOCK;
DROP TABLE ACCOUNT;
DROP TABLE BLOCKCHAIN;
DROP TABLE BLOCK;
DROP TABLE CUSTOMER;
DROP TABLE COMPANY;
DROP TABLE ADDRESS;
