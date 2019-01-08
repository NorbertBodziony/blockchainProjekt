package database;

import java.io.Serializable;

public class BlockchainData implements Serializable {
    int id;
    public static final long serialVersionUID = 2L;
    public BlockchainData(int id) {
        this.id = id;
    }
}
