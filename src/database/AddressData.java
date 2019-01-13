package database;

import java.io.Serializable;

public class AddressData implements Serializable {
    int Address_ID;
    String Country;
    String Postal_code;
    String City;
    String Street;
    String Apartament_number;

    public AddressData(int address_ID, String country, String postal_code, String city, String street, String apartament_number) {
        Address_ID = address_ID;
        Country = country;
        Postal_code = postal_code;
        City = city;
        Street = street;
        Apartament_number = apartament_number;
    }
}
