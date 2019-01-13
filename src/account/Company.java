package account;

import java.io.Serializable;

public class Company implements Serializable {
    public static final int COMPANY_NAME_LEN = 80;
    public static final int SECTOR_LEN = 80;
    public static final int CONTACT_TEL_LEN = 12;
    public static final int CONTACT_EMAIL_LEN = 80;

    private String companyName;
    private String sector;
    private String contactTel;
    private String contactEmail;

    public Company(String companyName, String sector, String contactTel, String contactEmail) {
        this.companyName = trim(companyName, COMPANY_NAME_LEN);
        this.sector = (sector == null ? null : trim(companyName, SECTOR_LEN));
        this.contactTel = trim(contactTel, CONTACT_TEL_LEN);
        this.contactEmail = trim(contactEmail, CONTACT_EMAIL_LEN);
    }

    private static String trim(String string, int maxLen) {
        return string.length() > maxLen ? string.substring(0 ,maxLen) : string;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getSector() {
        return sector;
    }

    public String getContactTel() {
        return contactTel;
    }

    public String getContactEmail() {
        return contactEmail;
    }
}
