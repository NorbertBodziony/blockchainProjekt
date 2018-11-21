-- get blockchain
SELECT signature, hash_code, previous_hash_code, amount, receive_type, send_type FROM BLOCK 
WHERE BLOCK.blockchain_id =  
  (SELECT account.blockchain FROM account join blockchain on account.blockchain = blockchain.blockchain_id 
  WHERE account.public_key = 'ENTER_NAME') ORDER BY block.block_id;