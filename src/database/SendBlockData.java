package database;

import java.io.Serializable;

public class SendBlockData implements Serializable {
    int ID;
    String Recipient;
    public static final long serialVersionUID = 5L;
    public SendBlockData(int ID, String recipient) {
        this.ID = ID;
        Recipient = recipient;
    }
}
