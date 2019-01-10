package account;

import java.io.Serializable;

import static account.Company.CONTACT_EMAIL_LEN;


public class Customer implements Serializable {
    public static final int FIRST_NAME_LEN = 40;
    public static final int LAST_NAME_LEN = 40;

    private String publicKey;
    private int companyId;
    private String firstName;
    private String lastName;
    private String contactEmail;

    public Customer(String publicKey, int companyId, String firstName, String lastName, String contactEmail) {
        this.publicKey = publicKey;
        this.companyId = companyId;
        this.firstName = trim(firstName, FIRST_NAME_LEN);
        this.lastName = trim(lastName, LAST_NAME_LEN);
        this.contactEmail = trim(contactEmail, CONTACT_EMAIL_LEN);
    }

    private static String trim(String string, int maxLen) {
        return string.length() > maxLen ? string.substring(0 ,maxLen) : string;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public int getCompanyId() {
        return companyId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getContactEmail() {
        return contactEmail;
    }
}
