package tests;

import database.Database;
import datagramInterfaces.AddCompany;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class AddCompanyTest {

    @Test
    public void addCompany() {
        try {
            Connection con = Database.connect();
            AddCompany newCompany = new AddCompany("companyName",null, "contactTel",
                    "contactEmail", "country", "postalCode", "city",
                    "street", "apartmentNumber");
            int companyId = Database.addCompany(con, newCompany);
            System.out.println(companyId);
            assertTrue(companyId > 0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}