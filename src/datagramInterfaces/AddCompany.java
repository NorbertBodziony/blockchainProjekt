package datagramInterfaces;

import account.Address;
import account.Company;
import database.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static datagramInterfaces.ErrorCode.*;

public class AddCompany extends WalletRequest {
    private Address address;
    private Company company;


    public AddCompany(String companyName, String sector, String contactTel, String contactEmail, String country,
                      String postalCode, String city, String street, String apartmentNumber) {

        this.company = new Company(companyName, sector, contactTel, contactEmail);
        this.address = new Address(country, postalCode, city, street, apartmentNumber);
    }


    public String getCompanyName() {
        return company.getCompanyName();
    }

    public String getSector() {
        return company.getSector();
    }

    public String getContactTel() {
        return company.getContactTel();
    }

    public String getContactEmail() {
        return company.getContactEmail();
    }

    public String getCountry() {
        return address.getCountry();
    }

    public String getPostalCode() {
        return address.getPostalCode();
    }

    public String getCity() {
        return address.getCity();
    }

    public String getStreet() {
        return address.getStreet();
    }

    public String getApartmentNumber() {
        return address.getApartmentNumber();
    }

    @Override
    public NodeRespond handle(Connection con) throws SQLException, IOException {
        int companyId = Database.addCompany(con, this);
        return new AddCompanyRespond(OK, companyId);
    }
}
