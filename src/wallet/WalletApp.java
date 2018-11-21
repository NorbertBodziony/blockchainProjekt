package wallet;

import constants.Constants;
import datagramInterfaces.NodeRespond;
import datagramInterfaces.PreviousHashesRespond;

import java.io.IOException;
import java.net.DatagramPacket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

import static datagramInterfaces.ErrorCode.OK;

public class WalletApp {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Wallet wallet = new Wallet(Constants.NODE_IP, Constants.NODE_PORT);
        Scanner in = new Scanner(System.in);
        try {
            String decision;
            System.out.println("Do you want to create new account[yes/no]");
            decision = in.nextLine();
            if(decision.toLowerCase().equals("yes")) {
                wallet.createAccount();
                DatagramPacket packet = wallet.listenToNodeRespond();
                NodeRespond respond = wallet.unpackRespond(packet);
                System.out.println(respond);

            }
            else{
                String publicKey;
                String privateKey;
                System.out.println("Enter the public key");
                publicKey = in.nextLine();
                System.out.println("Enter the private key");
                privateKey = in.nextLine();
                try {
                    wallet.login(publicKey, privateKey);
                }catch (InvalidKeySpecException e) {
                    System.out.println("InvalidKeySpecException");
                    System.exit(1);
                }

            }
            while (true) {
                System.out.println("[b]alance/[t]ransaction");
                decision = in.nextLine();
                if(decision.toLowerCase().equals("t")) {
                    System.out.println("Enter recipient: ");
                    String recipient = in.nextLine();
                    wallet.getPreviousHashes(recipient);
                    DatagramPacket packet = wallet.listenToNodeRespond();
                    NodeRespond respond = wallet.unpackRespond(packet);
                    if(respond.getErrorCode() != OK) {
                        System.out.println(respond);
                        continue;
                    }
                    PreviousHashesRespond hashRespond = (PreviousHashesRespond)respond;
                    System.out.println("Enter amount");
                    int amount = Integer.parseInt(in.nextLine());
                    String recipientHash = hashRespond.getRecipientPreviousHash();
                    String senderHash = hashRespond.getSenderPreviousHash();
                    wallet.performTransaction(amount, recipient, senderHash, recipientHash);

                }else {
                    wallet.getBalance();
                }
                DatagramPacket packet = wallet.listenToNodeRespond();
                NodeRespond respond = wallet.unpackRespond(packet);
                System.out.println(respond);
            }

        }catch (IOException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
