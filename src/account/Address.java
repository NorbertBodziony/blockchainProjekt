package account;

import java.io.Serializable;

public class Address implements Serializable {
    public static final int COUNTRY_LEN = 40;
    public static final int POSTAL_CODE_LEN = 10;
    public static final int CITY_LEN = 50;
    public static final int STREET_LEN = 60;
    public static final int APARTMENT_NUMBER_LEN = 60;

    private String country;
    private String postalCode;
    private String city;
    private String street;
    private String apartmentNumber;

    public Address(String country, String postalCode, String city, String street, String apartmentNumber) {
        this.country = trim(country, COUNTRY_LEN);
        this.postalCode = trim(postalCode, POSTAL_CODE_LEN);
        this.city = trim(city, CITY_LEN);
        this.street = trim(street, STREET_LEN);
        this.apartmentNumber = trim(apartmentNumber, APARTMENT_NUMBER_LEN);
    }

    private static String trim(String string, int maxLen) {
        return string.length() > maxLen ? string.substring(0 ,maxLen) : string;
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
}
