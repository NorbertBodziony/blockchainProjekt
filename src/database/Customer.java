package database;

import java.io.Serializable;

public class Customer implements Serializable {
    int Customer_id;
    int Company_id;
    int Address_id;
    String First_name;
    String Last_name;
    String Contact_email;

    public Customer(int customer_id, int company_id, int address_id, String first_name, String last_name, String contact_email) {
        Customer_id = customer_id;
        Company_id = company_id;
        Address_id = address_id;
        First_name = first_name;
        Last_name = last_name;
        Contact_email = contact_email;
    }
}
