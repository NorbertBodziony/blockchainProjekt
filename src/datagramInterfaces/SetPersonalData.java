package datagramInterfaces;

import account.Address;
import account.Customer;
import database.Database;
import node.ClientTCP;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static datagramInterfaces.ErrorCode.*;

public class SetPersonalData extends WalletRequest {
    private Customer customer;
    private Address address;
    List<ClientTCP> clientTCP;


    public SetPersonalData( String publicKey,  int companyId,  String firstName,  String lastName,
                            String contactEmail,  String country, String postalCode,
                            String city, String street, String apartmentNumber) {
        boolean setAddress = country != null && postalCode != null &&
                city != null && street != null && apartmentNumber != null;

        this.customer = new Customer(publicKey, companyId, firstName, lastName, contactEmail);
        if(setAddress)
            this.address = new Address(country, postalCode, city, street, apartmentNumber);
    }
    public SetPersonalData(Customer customer,Address address ,List<ClientTCP> clientTCP) {
        this.customer=customer;
        this.address=address;
        this.clientTCP=clientTCP;
    }

    public String getPublicKey() { return customer.getPublicKey(); }

    public int getCompanyId() {
        return customer.getCompanyId();
    }

    public String getFirstName() {
        return customer.getFirstName();
    }

    public String getLastName() { return customer.getLastName(); }

    public String getContactEmail() {
        return customer.getContactEmail();
    }

    public String getCountry() {
        return address == null ? null :address.getCountry();
    }

    public String getPostalCode() {
        return address == null ? null : address.getPostalCode();
    }

    public String getCity() {
        return address == null ? null : address.getCity();
    }

    public String getStreet() {
        return address == null ? null : address.getStreet();
    }

    public String getApartmentNumber() {
        return address == null ? null : address.getApartmentNumber();
    }

    @Override
    public NodeRespond handle(Connection con) throws SQLException, IOException {
        // add error handler
        try{
            Database.setPersonalData(con, this);
        }catch (SQLException e){
            return new NodeRespond(DB_CONNECTION);
        }

        if(clientTCP!=null){

            System.out.println("Sending to clients ="+clientTCP.size());
            for(int i=0;i<clientTCP.size();i++) {
                System.out.println("PERFORMIG TCP TRANSACTION");
             //   clientTCP.get(i).SendPersonalData(Id,address,customer);
            }
        }
        else
        {
            System.out.println("client are empty");
        }
        return new NodeRespond(OK);
    }
}
