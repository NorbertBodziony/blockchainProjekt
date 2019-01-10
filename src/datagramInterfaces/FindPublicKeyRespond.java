package datagramInterfaces;

import java.util.List;

public class FindPublicKeyRespond extends NodeRespond {
    private List<String> publicKeys;

    public FindPublicKeyRespond(ErrorCode errorCode, List<String> publicKeys) {
        super(errorCode);
        this.publicKeys = publicKeys;
    }

    public List<String> getPublicKeys() {
        return publicKeys;
    }
}
