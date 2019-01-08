package datagramInterfaces;

import java.io.Serializable;

public abstract class DatagramMessage implements Serializable {
    public static final int DATAGRAM_SIZE = 2048;
    public static final long serialVersionUID = 6L;
}
