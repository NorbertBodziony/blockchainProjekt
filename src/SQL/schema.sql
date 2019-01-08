--------------------------------------------------------
-----------------------------------------------------------
-- CREATE SCHEMA:
CREATE TABLE send_block(
	id INT PRIMARY KEY,
    recipient VARCHAR2(144) NOT NULL
);

CREATE TABLE receive_block(
	 id INT PRIMARY KEY,
    sender VARCHAR2(144) NULL
);


CREATE TABLE block(
    block_id INT PRIMARY KEY,
    blockchain_id INT NOT NULL,
    previous_block INT NULL,
    signature VARCHAR2(112) NOT NULL,
    hash_code VARCHAR2(64) NOT NULL,
    previous_hash_code VARCHAR2(64) NOT NULL,
    amount INT NOT NULL,
    receive_type INT NULL,
    send_type INT NULL,
    transaction_time TIMESTAMP
);
ALTER TABLE block ADD CONSTRAINT fk_send_block FOREIGN KEY (send_type) REFERENCES send_block(id);
ALTER TABLE block ADD CONSTRAINT fk_receive_block FOREIGN KEY (receive_type) REFERENCES receive_block(id);
ALTER TABLE block ADD CONSTRAINT fk_previous_block FOREIGN KEY (previous_block) REFERENCES block(block_id);


CREATE TABLE blockchain(
	blockchain_id INT PRIMARY KEY,
    last_block INT NULL
);
ALTER TABLE blockchain ADD CONSTRAINT fk_last_block FOREIGN KEY (last_block) REFERENCES block(block_id);



CREATE TABLE account(
    public_key VARCHAR2(144) PRIMARY KEY,
	company_type INT,
	customer_type INT,
    blockchain INT NOT NULL
	);
  
  -- recive, send, block foreign key
ALTER TABLE receive_block ADD CONSTRAINT fk_sender FOREIGN KEY (sender) REFERENCES account(public_key);
ALTER TABLE send_block ADD CONSTRAINT fk_recipient FOREIGN KEY (recipient) REFERENCES account(public_key);
ALTER TABLE block ADD CONSTRAINT fk_block_in_blockchain FOREIGN KEY (blockchain_id) REFERENCES blockchain(blockchain_id);

-- account
ALTER TABLE account ADD CONSTRAINT fk_blockchain FOREIGN KEY (blockchain) REFERENCES blockchain(blockchain_id);


-- new tabels
CREATE TABLE company(
	company_id int PRIMARY KEY,
	company_name VARCHAR2(80) NOT NULL,
	sector VARCHAR2(80),
	contact_tel VARCHAR2(12) NOT NULL,
	address_id INT NOT NULL,
	contact_email VARCHAR2(80) NOT NULL);
  
	
CREATE TABLE customer(
	customer_id INT PRIMARY KEY,
	company_id INT NOT NULL,
	address_id INT,
	first_name VARCHAR2(40) NOT NULL,
	last_name VARCHAR2(40) NOT NULL,
	contact_email VARCHAR2(80) NOT NULL);
  	
CREATE TABLE address(
	address_id INT PRIMARY KEY,
	country VARCHAR2(40) NOT NULL,
	postal_code VARCHAR2(10) NOT NULL,
	city VARCHAR2(50) NOT NULL,
	street VARCHAR2(60) NOT NULL,
	apartment_number VARCHAR2(60) NOT NULL);
  
-- company  
ALTER TABLE company ADD CONSTRAINT fk_address_comp FOREIGN KEY(address_id) REFERENCES address(address_id);

-- customer
ALTER TABLE customer ADD CONSTRAINT fk_company_id FOREIGN KEY(company_id) REFERENCES company(company_id);
ALTER TABLE customer ADD CONSTRAINT fk_address_cust FOREIGN KEY(address_id) REFERENCES address(address_id);

-- account
ALTER TABLE account ADD CONSTRAINT fk_company_type FOREIGN KEY (company_type) REFERENCES company(company_id);
ALTER TABLE account ADD CONSTRAINT fk_customer_type FOREIGN KEY(customer_type) REFERENCES customer(customer_id);
	
