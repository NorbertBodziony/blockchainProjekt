package database;

import java.io.Serializable;

public class SendBlockData implements Serializable {
    int ID;
    String Recipient;

    public SendBlockData(int ID, String recipient) {
        this.ID = ID;
        Recipient = recipient;
    }
}
