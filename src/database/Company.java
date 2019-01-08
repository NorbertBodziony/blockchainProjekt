package database;

import java.io.Serializable;

public class Company implements Serializable {
    int Company_id;
    String Company_name;
    String Sector;
    String Contact_tel;
    int Address_id;
    String Contact_email;

    public Company(int company_id, String company_name, String sector, String contact_tel, int address_id, String contact_email) {
        Company_id = company_id;
        Company_name = company_name;
        Sector = sector;
        Contact_tel = contact_tel;
        Address_id = address_id;
        Contact_email = contact_email;
    }
}
