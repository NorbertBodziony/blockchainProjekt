package datagramInterfaces;

import database.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static datagramInterfaces.AddCompany.*;
import static datagramInterfaces.ErrorCode.*;

public class SetPersonalData extends WalletRequest {
    private String publicKey;
    private int companyId;
    private String firstName;
    private String lastName;
    private String contactEmail;
    private String country;
    private String postalCode;
    private String city;
    private String street;
    private String apartmentNumber;

    public static final int FIRST_NAME_LEN = 40;
    public static final int LAST_NAME_LEN = 40;

    private static String trim(String string, int maxLen) {
        return string.length() > maxLen ? string.substring(0 ,maxLen) : string;
    }

    public SetPersonalData( String publicKey,  int companyId,  String firstName,  String lastName,
                            String contactEmail,  String country, String postalCode,
                            String city, String street, String apartmentNumber) {
        boolean setAddress = country != null && postalCode != null &&
                city != null && street != null && apartmentNumber != null;

        this.publicKey = publicKey;
        this.companyId = companyId;
        this.firstName = trim(firstName, FIRST_NAME_LEN);
        this.lastName = trim(lastName, LAST_NAME_LEN);
        this.contactEmail = trim(contactEmail, CONTACT_EMAIL_LEN);
        this.country = !setAddress ? null : trim(country, COUNTRY_LEN);
        this.postalCode = !setAddress ? null :  trim(postalCode, POSTAL_CODE_LEN);
        this.city = !setAddress ? null : trim(city, CITY_LEN);
        this.street = !setAddress ? null : trim(street, STREET_LEN);
        this.apartmentNumber = !setAddress ? null : trim(apartmentNumber, APARTMENT_NUMBER_LEN);
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

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    @Override
    public NodeRespond handle(Connection con) throws SQLException, IOException {
        // add error handler
        try{
            Database.setPersonalData(con, this);
        }catch (SQLException e){
            return new NodeRespond(DB_CONNECTION);
        }
        return new NodeRespond(OK);
    }
}
