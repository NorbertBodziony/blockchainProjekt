package database;

import java.io.Serializable;

public class BlockchainData implements Serializable {
    int id;

    public BlockchainData(int id) {
        this.id = id;
    }
}
