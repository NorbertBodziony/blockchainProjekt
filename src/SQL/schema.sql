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
    send_type INT NULL
);
ALTER TABLE block ADD CONSTRAINT fk_send_block FOREIGN KEY (send_type) REFERENCES send_block(id);
ALTER TABLE block ADD CONSTRAINT fk_receive_block FOREIGN KEY (receive_type) REFERENCES receive_block(id);
ALTER TABLE block ADD CONSTRAINT fk_previous_block FOREIGN KEY (previous_block) REFERENCES block(block_id);
ALTER TABLE block ADD CONSTRAINT unique_receive_type UNIQUE(receive_type);
ALTER TABLE block ADD CONSTRAINT unique_send_type UNIQUE(send_type);
ALTER TABLE block ADD CONSTRAINT unique_previous_block UNIQUE(previous_block);


CREATE TABLE blockchain(
	blockchain_id INT PRIMARY KEY,
    last_block INT NULL
);
ALTER TABLE blockchain ADD CONSTRAINT fk_last_block FOREIGN KEY (last_block) REFERENCES block(block_id);



CREATE TABLE account(
    public_key VARCHAR2(144) PRIMARY KEY,
    blockchain INT NOT NULL
);
ALTER TABLE account ADD CONSTRAINT fk_blockchain FOREIGN KEY (blockchain) REFERENCES blockchain(blockchain_id);
ALTER TABLE account ADD CONSTRAINT unique_blockchain UNIQUE(blockchain);

-- recive, send, block foreign key
ALTER TABLE receive_block ADD CONSTRAINT fk_sender FOREIGN KEY (sender) REFERENCES account(public_key);
ALTER TABLE send_block ADD CONSTRAINT fk_recipient FOREIGN KEY (recipient) REFERENCES account(public_key);
ALTER TABLE block ADD CONSTRAINT fk_block_in_blockchain FOREIGN KEY (blockchain_id) REFERENCES blockchain(blockchain_id);