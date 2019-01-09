package datagramInterfaces;

public class AddCompanyRespond extends NodeRespond {
    private int companyId;

    public AddCompanyRespond(ErrorCode errorCode, int companyId) {
        super(errorCode);
        this.companyId = companyId;
    }

    public int getCompanyId() {
        return companyId;
    }
}
