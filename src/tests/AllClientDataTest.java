package tests;

import constants.Constants;
import datagramInterfaces.AddCompanyRespond;
import datagramInterfaces.NodeRespond;
import org.junit.Test;
import wallet.Wallet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import static datagramInterfaces.ErrorCode.OK;
import static org.junit.Assert.assertTrue;

public class AllClientDataTest {

    @Test
    public void simpleTest() {
        try {
            // add new company
            Wallet wallet = new Wallet(Constants.NODE_IP, Constants.NODE_PORT);

            wallet.addCompany("companyName","sector", "contactTel",
                    "contactEmail", "country", "postalCode", "city",
                    "street", "apartmentNumber");

            DatagramPacket packet = wallet.listenToNodeRespond();
            NodeRespond respond = wallet.unpackRespond(packet);

            AddCompanyRespond companyRespond = (AddCompanyRespond) respond;

            assertTrue(companyRespond.getErrorCode() == OK);
            int companyId = companyRespond.getCompanyId();
            System.out.println("company id");
            assertTrue(companyRespond.getCompanyId() > 0);

            // create new account
            wallet.createAccount();
            packet = wallet.listenToNodeRespond();
            respond = wallet.unpackRespond(packet);

            System.out.println(respond);
            assertTrue(respond.getErrorCode() == OK);

            // set personal data to new account
            wallet.setPersonalData(wallet.getAddress(), companyId, "firstname", "lastname",
                    "contactEmaill", "country", "postalCode", "city", "street",
                    "apartmentNumber");

            packet = wallet.listenToNodeRespond();
            respond = wallet.unpackRespond(packet);

            System.out.println(respond);
            assertTrue(respond.getErrorCode() == OK);


        }catch (IOException | NoSuchAlgorithmException | SignatureException |
                InvalidKeyException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}