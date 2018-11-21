package datagramInterfaces;

import java.io.Serializable;

public class GetAccounts implements Serializable {
    int id;
    public GetAccounts(int id) {
        this.id=id;
    }
}
