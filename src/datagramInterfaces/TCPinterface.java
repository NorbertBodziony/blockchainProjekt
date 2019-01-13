package datagramInterfaces;

import java.io.Serializable;

public class TCPinterface implements Serializable {
   public static final long serialVersionUID = 7L;
   public enum TCPid{ Blockchain,Transaction,NewAccount,Company,PersonalData};

}
