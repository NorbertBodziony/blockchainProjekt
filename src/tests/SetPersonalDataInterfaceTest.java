package tests;

import constants.Constants;
import datagramInterfaces.NodeRespond;
import org.junit.Test;
import wallet.Wallet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import static datagramInterfaces.ErrorCode.OK;
import static org.junit.Assert.*;

public class SetPersonalDataInterfaceTest {

    @Test
    public void withAddress() throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            // connect
            Wallet wallet = new Wallet(Constants.NODE_IP, Constants.NODE_PORT);

            // send request - create new account
            wallet.createAccount();

            // listen to node respond -  result of creating
            DatagramPacket packet = wallet.listenToNodeRespond();

            // unpack respond
            NodeRespond respond = wallet.unpackRespond(packet);

            System.out.println(respond);
            assertTrue(respond.getErrorCode() == OK);


            // set personal data
            wallet.setPersonalData(wallet.getAddress(), 1, "firstname", "lastname",
                    "contactEmaill", "country", "postalCode", "city", "street",
                    "apartmentNumber");

            // listen to node respond - balance
            packet = wallet.listenToNodeRespond();

            // unpack respond
            respond = wallet.unpackRespond(packet);

            System.out.println(respond);
            assertTrue(respond.getErrorCode() == OK);


        } catch (IOException | NoSuchAlgorithmException | SignatureException |
                InvalidKeyException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void withoutAddress() {
        try {
            // connect
            Wallet wallet = new Wallet(Constants.NODE_IP, Constants.NODE_PORT);

            // send request - create new account
            wallet.createAccount();

            // listen to node respond -  result of creating
            DatagramPacket packet = wallet.listenToNodeRespond();

            // unpack respond
            NodeRespond respond = wallet.unpackRespond(packet);

            System.out.println(respond);
            assertTrue(respond.getErrorCode() == OK);


            // set personal data - one null in address field causes that person not contain address
            wallet.setPersonalData(wallet.getAddress(), 1, "firstname", "lastname",
                    "contactEmail", null, "postalCode", "city", "street",
                    "apartmentNumber");

            // listen to node respond - balance
            packet = wallet.listenToNodeRespond();

            // unpack respond
            respond = wallet.unpackRespond(packet);

            System.out.println(respond);
            assertTrue(respond.getErrorCode() == OK);


        } catch (IOException | NoSuchAlgorithmException | SignatureException |
                InvalidKeyException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void maxSize() {
        try {
            // connect
            Wallet wallet = new Wallet(Constants.NODE_IP, Constants.NODE_PORT);

            // send request - create new account
            wallet.createAccount();

            // listen to node respond -  result of creating
            DatagramPacket packet = wallet.listenToNodeRespond();

            // unpack respond
            NodeRespond respond = wallet.unpackRespond(packet);

            System.out.println(respond);
            assertTrue(respond.getErrorCode() == OK);


            // set personal data
            wallet.setPersonalData(wallet.getAddress(), 1,
                    "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789",
                    "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789",
                    "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789",
                    "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789",
                    "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789",
                    "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789",
                    "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789",
                    "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789");

            // listen to node respond - balance
            packet = wallet.listenToNodeRespond();

            // unpack respond
            respond = wallet.unpackRespond(packet);

            System.out.println(respond);
            assertTrue(respond.getErrorCode() == OK);


        } catch (IOException | NoSuchAlgorithmException | SignatureException |
                InvalidKeyException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}