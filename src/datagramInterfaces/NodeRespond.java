package datagramInterfaces;


public class NodeRespond extends DatagramMessage {
    private ErrorCode errorCode;

    public NodeRespond(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return errorCode.name();
    }
}
