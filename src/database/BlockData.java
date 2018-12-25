package database;

import java.io.Serializable;

public class BlockData implements Serializable {
    int Block_ID;
    int Blockchain_ID;
    int Previous_Block;
    String Signature;
    String Hash_Code;
    String Previous_Hash_Code;
    int Amount;
    int Receive_Type;
    int Send_Type;
    public static final long serialVersionUID = 3L;
    public BlockData(int block_ID, int blockchain_ID, int previous_Block, String signature, String hash_Code, String previous_Hash_Code, int amount, int receive_Type, int send_Type) {
        Block_ID = block_ID;
        Blockchain_ID = blockchain_ID;
        Previous_Block = previous_Block;
        Signature = signature;
        Hash_Code = hash_Code;
        Previous_Hash_Code = previous_Hash_Code;
        Amount = amount;
        Receive_Type = receive_Type;
        Send_Type = send_Type;
    }
}
