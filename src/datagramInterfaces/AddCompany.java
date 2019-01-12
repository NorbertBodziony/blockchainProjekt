package datagramInterfaces;

import account.Address;
import account.Company;
import database.Database;
import node.ClientTCP;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static datagramInterfaces.ErrorCode.*;

public class AddCompany extends WalletRequest {
    private Address address;
    private Company company;
    List<ClientTCP> clientTCP;

    public void setClientTCP(List<ClientTCP> clientTCP) {
        this.clientTCP = clientTCP;
    }

    public AddCompany(String companyName, String sector, String contactTel, String contactEmail, String country,
                      String postalCode, String city, String street, String apartmentNumber) {

        this.company = new Company(companyName, sector, contactTel, contactEmail);
        this.address = new Address(country, postalCode, city, street, apartmentNumber);
    }
    public AddCompany(Company company,Address address ,List<ClientTCP> clientTCP) {
        this.company = company;
        this.address = address;
        this.clientTCP=clientTCP;
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

        if(clientTCP!=null){

            System.out.println("Sending to clients ="+clientTCP.size());
            for(int i=0;i<clientTCP.size();i++) {
                System.out.println("PERFORMING TCP COMPANY");
                clientTCP.get(i).SendCompany(companyId,address,company);
            }
        }
        else
        {
            System.out.println("client are empty");
        }

        return new AddCompanyRespond(OK, companyId);
    }
}
