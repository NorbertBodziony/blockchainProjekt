package datagramInterfaces;

public class GetBalanceRespond extends NodeRespond {
    private int balance;

    public GetBalanceRespond(ErrorCode errorCode, int balance) {
        super(errorCode);
        this.balance = balance;
    }

    @Override
    public String toString() {
        return String.valueOf(balance);
    }
}
