package datagramInterfaces;

import database.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static datagramInterfaces.ErrorCode.*;

public class AddCompany extends WalletRequest {
    public static final int COMPANY_NAME_LEN = 80;
    public static final int SECTOR_LEN = 80;
    public static final int CONTACT_TEL_LEN = 12;
    public static final int CONTACT_EMAIL_LEN = 80;

    public static final int COUNTRY_LEN = 40;
    public static final int POSTAL_CODE_LEN = 10;
    public static final int CITY_LEN = 50;
    public static final int STREET_LEN = 60;
    public static final int APARTMENT_NUMBER_LEN = 60;

    private String companyName;
    private String sector;
    private String contactTel;
    private String contactEmail;

    private String country;
    private String postalCode;
    private String city;
    private String street;
    private String apartmentNumber;

    private static String trim(String string, int maxLen) {
        return string.length() > maxLen ? string.substring(0 ,maxLen) : string;
    }


    public AddCompany(String companyName, String sector, String contactTel, String contactEmail, String country,
                      String postalCode, String city, String street, String apartmentNumber) {

        this.companyName = trim(companyName, COMPANY_NAME_LEN);
        this.sector = (sector == null ? null : trim(companyName, SECTOR_LEN));
        this.contactTel = trim(contactTel, CONTACT_TEL_LEN);
        this.contactEmail = trim(contactEmail, CONTACT_EMAIL_LEN);
        this.country = trim(country, COUNTRY_LEN);
        this.postalCode = trim(postalCode, POSTAL_CODE_LEN);
        this.city = trim(city, CITY_LEN);
        this.street = trim(street, STREET_LEN);
        this.apartmentNumber = trim(apartmentNumber, APARTMENT_NUMBER_LEN);
    }

    /*public AddCompany(String companyName, String contactTel, String contactEmail, String country, String postalCode,
                      String city, String street, String apartmentNumber) {
        this(companyName, null, contactTel, contactEmail, country, postalCode, city, street, apartmentNumber);
    }*/

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
        int companyId = Database.addCompany(con, this);
        return new AddCompanyRespond(OK, companyId);
    }
}
