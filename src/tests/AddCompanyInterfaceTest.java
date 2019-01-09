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

import static org.junit.Assert.*;

public class AddCompanyInterfaceTest {

    @Test
    public void simpleTest() {
        try {
            Wallet wallet = new Wallet(Constants.NODE_IP, Constants.NODE_PORT);

            wallet.addCompany("companyName",null, "contactTel",
                    "contactEmail", "country", "postalCode", "city",
                    "street", "apartmentNumber");

            DatagramPacket packet = wallet.listenToNodeRespond();
            NodeRespond respond = wallet.unpackRespond(packet);

            AddCompanyRespond companyRespond = (AddCompanyRespond) respond;

            System.out.println(companyRespond.getCompanyId());
            assertTrue(true);

        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void maxSizeTest() {
        try {
            Wallet wallet = new Wallet(Constants.NODE_IP, Constants.NODE_PORT);

            wallet.addCompany("123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789"
                    ,"123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789",
                    "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789",
                    "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789",
                    "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789",
                    "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789",
                    "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789",
                    "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789",
                    "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789");

            DatagramPacket packet = wallet.listenToNodeRespond();
            NodeRespond respond = wallet.unpackRespond(packet);

            AddCompanyRespond companyRespond = (AddCompanyRespond) respond;

            System.out.println(companyRespond.getCompanyId());
            assertTrue(true);

        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

}