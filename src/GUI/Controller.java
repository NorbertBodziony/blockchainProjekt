package GUI;

import cryptography.CryptoConverter;
import datagramInterfaces.NodeRespond;
import datagramInterfaces.PreviousHashesRespond;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import wallet.Wallet;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import static datagramInterfaces.ErrorCode.OK;

public class Controller {
    private MainView mainView;
    private Wallet wallet;
    private Utility utils;


    public Controller(MainView mainView, Wallet wallet) throws Exception {

        utils = new Utility();

        this.mainView = mainView;
        this.wallet = wallet;

        this.mainView.setWalletsLoginUsingSavedWallet(utils.getListOfWallets().toArray(new String[0]));


        this.mainView.setGenerateWalletButton(new listenForGenerateNewWallet());

        this.mainView.setReciveButtonWalletScreen(e ->
        {
            new reciveDialog(wallet.getAddress()).showAndWait();
        });

        this.mainView.setLoginButtonLoginUsingSavedWallet(e ->
        {
            System.out.println(this.mainView.getSelectedWalletLoginUsingSavedWallet());
        });

        this.mainView.setLoginUsingPrivKey(e ->
        {

            try {
                wallet.login(this.mainView.getLoginUsingPrivKeyPublicKey(), this.mainView.getPasswordLoginUsingPrivKey());
                //this.mainView.setAmountWalletScreen();


                this.mainView.setScreenVisible("walletScreen");
                System.out.println(this.mainView.getPasswordLoginUsingPrivKey());
                System.out.println(this.mainView.getLoginUsingPrivKeyPublicKey());
            } catch (InvalidKeySpecException exec) {
                System.out.println("InvalidKeySpecException");

            }


        });

        this.mainView.setGenerateKeys(e ->
        {
            try {
                wallet.createAccount();
                DatagramPacket packet = wallet.listenToNodeRespond();
                NodeRespond respond = wallet.unpackRespond(packet);
                System.out.println(respond);
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            } catch (SignatureException e1) {
                e1.printStackTrace();
            } catch (InvalidKeyException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            keysDialog keysDialog = new keysDialog(wallet.getAddress(), CryptoConverter.keyToHexString(wallet.getPrivateKey()));

            System.out.println(keysDialog.getPublicKey());
            System.out.println(keysDialog.getPrivateKey());


            keysDialog.showAndWait();

        });

        this.mainView.setAmountWalletScreen("80");

        this.mainView.setSendButtonWalletScreen(e ->
        {
            sendDialog sendDialog = new sendDialog();


            sendDialog.showAndWait().ifPresent(amountAndRecipent -> {
                System.out.println("xd=" + amountAndRecipent.getKey() + ", Recipent address=" + amountAndRecipent.getValue());

                int amount = Integer.parseInt(amountAndRecipent.getKey());
                String recipient = amountAndRecipent.getValue();


                try {
                    wallet.getPreviousHashes(recipient);
                    DatagramPacket packet = wallet.listenToNodeRespond();
                    NodeRespond respond = wallet.unpackRespond(packet);
                    if (respond.getErrorCode() != OK) {
                        System.out.println(respond);
                        System.out.println("Invalid Transaction");
                    }
                    else {
                        PreviousHashesRespond hashRespond = (PreviousHashesRespond) respond;
                        String recipientHash = hashRespond.getRecipientPreviousHash();
                        String senderHash = hashRespond.getSenderPreviousHash();
                        wallet.performTransaction(amount, recipient, senderHash, recipientHash);
                        System.out.println("Send: " + amount + " to: " + recipient);
                        DatagramPacket nodePacket = wallet.listenToNodeRespond();
                        System.out.println(wallet.unpackRespond(nodePacket));
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (SignatureException e1) {
                    e1.printStackTrace();
                } catch (InvalidKeyException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }

            });


        });

        this.mainView.setLoginButtonLoginUsingSavedWallet(new listenForLoginUsingSavedWallet());

    }

    private class listenForLoginUsingSavedWallet implements EventHandler<ActionEvent> {
        String[] keys;

        @Override
        public void handle(ActionEvent event) {
            try {

                keys = utils.decipherWalletFile(mainView.getSelectedWalletLoginUsingSavedWallet(), mainView.getPasswordLoginUsingSavedWallet());

                System.out.println(keys[0]);
                System.out.println(keys[1]);

                wallet.login(keys[0], keys[1]);
                //this.mainView.setAmountWalletScreen();


                mainView.setScreenVisible("walletScreen");

            } catch (InvalidKeySpecException exec) {
                System.out.println("InvalidKeySpecException");

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private class listenForGenerateNewWallet implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            System.out.println("XD");
            try {
                if (mainView.getCreateNewWalletPassword().equals(mainView.getCreateNewWalletPassword2())) {
                    try {
                        wallet.createAccount();
                        DatagramPacket packet = wallet.listenToNodeRespond();
                        NodeRespond respond = wallet.unpackRespond(packet);
                        System.out.println(respond);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (NoSuchAlgorithmException e1) {
                        e1.printStackTrace();
                    } catch (SignatureException e1) {
                        e1.printStackTrace();
                    } catch (InvalidKeyException e1) {
                        e1.printStackTrace();
                    }

                    utils.createWalletFile(mainView.getCreateNewWalletPassword(), wallet.getAddress(), CryptoConverter.keyToHexString(wallet.getPrivateKey()), mainView.getCreateNewWalletWalletName());


                    mainView.setWalletsLoginUsingSavedWallet(utils.getListOfWallets().toArray(new String[0]));

                    //System.out.println(mainView.getCreateNewWalletWalletName());
                    System.out.println("Password Equals ");
                } else {
                    System.out.println("Password not equal");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
