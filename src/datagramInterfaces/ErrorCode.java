package datagramInterfaces;

public enum ErrorCode {
    OK,
    UNKNOWN_ADDRESS,
    CANNOT_CREATE_ACCOUNT,
    UNKNOWN_RECIPIENT,
    UNKNOWN_SENDER,
    INVALID_SIGNATURE,
    INVALID_AMOUNT,
    DB_CONNECTION,
    UNRECOGNIZED_REQUEST;

    byte toByte() { return (byte) this.ordinal(); }


    public static void main(String[] args) {
        System.out.println(OK.toByte());
        System.out.println(UNKNOWN_RECIPIENT.toByte());
        System.out.println(INVALID_AMOUNT.toByte());
        System.out.println(DB_CONNECTION.toByte());
    }
}
