package database;

import java.io.Serializable;

public class AccountList implements Serializable {
    String PublicKey;
    int id;
    int Company_Type;
    int Customer_Type;
    public static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "AccountList{" +
                "PublicKey='" + PublicKey + '\'' +
                ", id=" + id +
                ", Company_Type=" + Company_Type +
                ", Customer_Type=" + Customer_Type +
                '}';
    }

    public AccountList(String publicKey, int id, int company_Type, int customer_Type) {
        PublicKey = publicKey;
        this.id = id;
        Company_Type = company_Type;
        Customer_Type = customer_Type;
    }
}
