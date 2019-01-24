package tests;

import database.Database;
import datagramInterfaces.SetPersonalData;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class SetPersonalDataTest {

    @Test
    public void setPersonalData() {
        try {
            Connection con = Database.connect();
            SetPersonalData data =  new SetPersonalData("3046301006072A8648CE3D020106052B8104001F033200041A53A4A4A282502B6CA8FD65" +
                    "AAEE3FA628F11F513E7D08233362460F9AA50A02B87B226033C23D01F1B70A5D5BD3DEA6",
                    1, "firstName", "lastName", "contactEmail", "country",
                    "postalCode", "city", "street", "apartmentNumber");

            Database.setPersonalData(con, data);
            assertTrue(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}