package tests;

import constants.Constants;
import database.Transaction;
import datagramInterfaces.NodeRespond;
import datagramInterfaces.TransactionRespond;
import org.junit.Test;
import wallet.Wallet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;

public class GetTransactionHistoryTest {
    // first run genesis node

    @Test
    public void getAllIncomingTransactions() {
        try {
            Wallet wallet = new Wallet(Constants.NODE_IP, Constants.NODE_PORT);
            wallet.createAccount();
            DatagramPacket packet = wallet.listenToNodeRespond();
            NodeRespond creatingRespond = wallet.unpackRespond(packet);
            System.out.println("creatingRespond result: " + creatingRespond);

            List<Transaction> transactions = new LinkedList<>();
            TransactionRespond tr;

            wallet.getTransactionHistory(true, null, null, null);
            do{
                DatagramPacket respond = wallet.listenToNodeRespond();
                tr = (TransactionRespond) wallet.unpackRespond(respond);
                transactions.addAll(tr.getTransactions());
            }while (!tr.isEnd());

            for(Transaction t : transactions)
                System.out.println(t);

        } catch (IOException | NoSuchAlgorithmException | SignatureException |
                InvalidKeyException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllOutgoingTransactionsFromOneAccount() {
        try {
            String publicKey = "3046301006072A8648CE3D020106052B8104001F033200040ECF3F02B901E842D80B5B03671B5B515777BB4D7794752DA2EDA2C894AD0CD702CA14DCC69C26BC46A7632FC142E9D0";
            Wallet wallet = new Wallet(Constants.NODE_IP, Constants.NODE_PORT);
            wallet.createAccount();
            DatagramPacket packet = wallet.listenToNodeRespond();
            NodeRespond creatingRespond = wallet.unpackRespond(packet);
            System.out.println("creatingRespond result: " + creatingRespond);

            List<Transaction> transactions = new LinkedList<>();
            TransactionRespond tr;

            wallet.getTransactionHistory(false, publicKey, null, null);
            do{
                DatagramPacket respond = wallet.listenToNodeRespond();
                tr = (TransactionRespond) wallet.unpackRespond(respond);
                transactions.addAll(tr.getTransactions());
            }while (!tr.isEnd());

            for(Transaction t : transactions)
                System.out.println(t);

        } catch (IOException | NoSuchAlgorithmException | SignatureException |
                InvalidKeyException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllIncomingTransactionsBetweenTimestamps() {
            try {
                Wallet wallet = new Wallet(Constants.NODE_IP, Constants.NODE_PORT);
                wallet.createAccount();
                DatagramPacket packet = wallet.listenToNodeRespond();
                NodeRespond creatingRespond = wallet.unpackRespond(packet);
                System.out.println("creatingRespond result: " + creatingRespond);

                List<Transaction> transactions = new LinkedList<>();
                TransactionRespond tr;

                wallet.getTransactionHistory(true, null, "06.01.2019 17:00:00", "06.01.2019 18:00:00");
                do{
                    DatagramPacket respond = wallet.listenToNodeRespond();
                     tr = (TransactionRespond) wallet.unpackRespond(respond);
                     transactions.addAll(tr.getTransactions());
                }while (!tr.isEnd());

                for(Transaction t : transactions)
                    System.out.println(t);


            } catch (IOException | NoSuchAlgorithmException | SignatureException |
                    InvalidKeyException | ClassNotFoundException e) {
                e.printStackTrace();
            }
    }

}