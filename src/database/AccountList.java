package database;

import java.io.Serializable;

public class AccountList implements Serializable {
    String PublicKey;
    int id;

    @Override
    public String toString() {
        return "AccountList{" +
                "PublicKey='" + PublicKey + '\'' +
                ", id=" + id +
                '}';
    }

    public AccountList(String publicKey, int id) {
        PublicKey = publicKey;
        this.id = id;
    }
}
