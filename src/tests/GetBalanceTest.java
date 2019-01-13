package tests;

import constants.Constants;
import datagramInterfaces.GetBalanceRespond;
import datagramInterfaces.NodeRespond;
import org.junit.Test;
import wallet.Wallet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import static datagramInterfaces.ErrorCode.*;
import static org.junit.Assert.*;

public class GetBalanceTest {

    @Test
    public void getBalanceFromNewAccount() {
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



            // send balance request
            wallet.getBalance();

            // listen to node respond - balance
            packet = wallet.listenToNodeRespond();

            // unpack respond
            respond = wallet.unpackRespond(packet);

            // cast to BalanceRespond
            GetBalanceRespond balanceRespond = (GetBalanceRespond) respond;
            int balance =  balanceRespond.getBalance();

            System.out.println(balance);
            assertTrue(balance == Constants.INITIAL_BALANCE);


        } catch (IOException | NoSuchAlgorithmException | SignatureException |
                InvalidKeyException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}