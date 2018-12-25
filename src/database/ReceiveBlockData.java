package database;

import java.io.Serializable;

public class ReceiveBlockData implements Serializable {
  int id;
  String Sender;
    public static final long serialVersionUID = 4L;
    public ReceiveBlockData(int id, String sender) {
        this.id = id;
        Sender = sender;
    }
}
