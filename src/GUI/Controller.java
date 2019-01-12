package GUI;

import cryptography.CryptoConverter;
import database.Transaction;
import datagramInterfaces.*;
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
import java.util.LinkedList;
import java.util.List;

import static datagramInterfaces.ErrorCode.OK;

public class Controller {
    private MainView mainView;
    private Wallet wallet;
    private Utility utils;
    private keysDialog keysDialog;
    private reciveDialog reciveDialog, searchBySurname;
    private sendDialog sendDialog;
    private updatePersonalData updatePersonalData;
    private addCompanyDialog addCompanyDialog;


    public Controller(MainView mainView, Wallet wallet) throws Exception {

        this.utils = new Utility();

        this.mainView = mainView;
        this.wallet = wallet;

        this.mainView.setWalletsLoginUsingSavedWallet(utils.getListOfWallets().toArray(new String[0]));


        this.mainView.setGenerateWalletButton(new listenForGenerateNewWallet());

        this.mainView.clearSurnameTextFieldWalletScreen();

        this.mainView.setReciveButtonWalletScreen(e ->
        {
            reciveDialog = new reciveDialog(wallet.getAddress());
            reciveDialog.showAndWait();
        });

        this.mainView.setLoginButtonLoginUsingSavedWallet(e ->
        {
            System.out.println(this.mainView.getSelectedWalletLoginUsingSavedWallet());

        });

        mainView.setInTextAreaWalletScreen("TEST");
        this.mainView.setLoginUsingPrivKey(e ->
        {

            try {
                wallet.login(this.mainView.getLoginUsingPrivKeyPublicKey(), this.mainView.getPasswordLoginUsingPrivKey());

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
            keysDialog = new keysDialog(wallet.getAddress(), CryptoConverter.keyToHexString(wallet.getPrivateKey()));

            System.out.println(keysDialog.getPublicKey());
            System.out.println(keysDialog.getPrivateKey());


            keysDialog.showAndWait();

        });

        this.mainView.setAmountWalletScreen("80");

        this.mainView.setRefreshWalletScree(e->
        {
            System.out.println("Refresh");
            try {
                System.out.println("Weszlo");
                // listen to node respond -  result of creating
                DatagramPacket packet;

                // unpack respond
                NodeRespond respond;

                // System.out.println(respond);


                // send balance request
                wallet.getBalance();

                // listen to node respond - balance
                packet = wallet.listenToNodeRespond();

                // unpack respond
                respond = wallet.unpackRespond(packet);

                // cast to BalanceRespond
                GetBalanceRespond balanceRespond = (GetBalanceRespond) respond;
                int balance =  balanceRespond.getBalance();

                System.out.println("Your Balance is: ");

                System.out.println(balance);
                this.mainView.setLabelAmountWalletScreen(Integer.toString(balance));



            } catch (IOException | ClassNotFoundException e5) {
                e5.printStackTrace();
            }



        });

        this.mainView.setRefreshHistoryWaletScreen(e->
        {
            System.out.println("Działa");
            System.out.println(mainView.getStartTimeWalletString() + mainView.getStopTimeWalletScreen());
            System.out.println(wallet.getAddress());
            mainView.setInTextAreaClearWalletScreen();
            mainView.setOutTextAreaClearWalletScreen();


            try {

                DatagramPacket packet;

                // unpack respond
                NodeRespond creatingRespond;
                wallet.getTransactionHistory(true, null, mainView.getStartTimeWalletString(), mainView.getStopTimeWalletScreen());

                //packet = wallet.listenToNodeRespond();
                //creatingRespond = wallet.unpackRespond(packet);

                //System.out.println("creatingRespond result: " + creatingRespond);

                List<Transaction> transactions = new LinkedList<>();
                TransactionRespond tr;

                do{
                    DatagramPacket respond = wallet.listenToNodeRespond();
                    tr = (TransactionRespond) wallet.unpackRespond(respond);
                    transactions.addAll(tr.getTransactions());
                }while (!tr.isEnd());

                for(Transaction t : transactions) {
                    if (mainView.getSelectedRadioButtonWalletScreen().equals("This account")) {
                        if (t.getRecipient().equals(wallet.getAddress())) {
                            //System.out.println(mainView.getSelectedRadioButtonWalletScreen());
                            mainView.setInTextAreaWalletScreen(" Amount: " + t.getAmount() + " From: " + (t.getSender() == null ? "Genesis Block" : t.getSender()));


                        }
                    } else if (mainView.getSelectedRadioButtonWalletScreen().equals("Entire network"))
                    {
                        mainView.setInTextAreaWalletScreen(" Amount: " + t.getAmount() + " To: " + t.getRecipient() + " From: " + (t.getSender() == null ? " Genesis Block" : t.getSender()));

                    }
                    else if (mainView.getSelectedRadioButtonWalletScreen().equals("Specified account"))
                    {
                        if (t.getRecipient().equals(mainView.getSpecAccontWalletScreen())) {
                            //System.out.println(mainView.getSelectedRadioButtonWalletScreen());
                            mainView.setInTextAreaWalletScreen(" Amount: " + t.getAmount() + " From: " + (t.getSender() == null ? "Genesis Block" : t.getSender()));


                        }
                    }
                }

            }catch (IOException | ClassNotFoundException e9) {
                e9.printStackTrace();
            }

            try {

                DatagramPacket packet;

                // unpack respond
                NodeRespond creatingRespond;
                wallet.getTransactionHistory(false, null, mainView.getStartTimeWalletString(), mainView.getStopTimeWalletScreen());

                //packet = wallet.listenToNodeRespond();
                //creatingRespond = wallet.unpackRespond(packet);

                //System.out.println("creatingRespond result: " + creatingRespond);

                List<Transaction> transactions = new LinkedList<>();
                TransactionRespond tr;

                do{
                    DatagramPacket respond = wallet.listenToNodeRespond();
                    tr = (TransactionRespond) wallet.unpackRespond(respond);
                    transactions.addAll(tr.getTransactions());
                }while (!tr.isEnd());

                for(Transaction t : transactions) {
                    if (mainView.getSelectedRadioButtonWalletScreen().equals("This account")) {
                        if (t.getSender().equals(wallet.getAddress())) {
                            //System.out.println(mainView.getSelectedRadioButtonWalletScreen());
                            mainView.setOutTextAreaWalletScreen(" Amount: "+t.getAmount()+" To: "+t.getRecipient());


                        }
                    } else if (mainView.getSelectedRadioButtonWalletScreen().equals("Entire network"))
                    {
                        mainView.setOutTextAreaWalletScreen(" Amount: "+t.getAmount()+" To: "+t.getRecipient() +" From: "+t.getSender());

                    }
                    else if (mainView.getSelectedRadioButtonWalletScreen().equals("Specified account"))
                    {
                        if (t.getSender().equals(mainView.getSpecAccontWalletScreen())) {
                            //System.out.println(mainView.getSelectedRadioButtonWalletScreen());
                            mainView.setOutTextAreaWalletScreen(" Amount: "+t.getAmount()+" To: "+t.getRecipient());


                        }
                    }
                }

            }catch (IOException | ClassNotFoundException e9) {
                e9.printStackTrace();
            }
        });

        this.mainView.setSendButtonWalletScreen(e ->
        {
            sendDialog = new sendDialog();


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
                    } else {
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
                try {
                    System.out.println("Weszlo");
                    // listen to node respond -  result of creating
                    DatagramPacket packet;

                    // unpack respond
                    NodeRespond respond;

                    // System.out.println(respond);


                    // send balance request
                    wallet.getBalance();

                    // listen to node respond - balance
                    packet = wallet.listenToNodeRespond();

                    // unpack respond
                    respond = wallet.unpackRespond(packet);

                    // cast to BalanceRespond
                    GetBalanceRespond balanceRespond = (GetBalanceRespond) respond;
                    int balance =  balanceRespond.getBalance();

                    System.out.println("Your Balance is: ");

                    System.out.println(balance);
                    mainView.setLabelAmountWalletScreen(Integer.toString(balance));



                } catch (IOException | ClassNotFoundException e5) {
                    e5.printStackTrace();
                }


            });
        });

        this.mainView.setSearchWalletScreen(e -> {
            System.out.println("setSendButtonWalletScreen");
            System.out.println(this.mainView.getSurnameTextFieldWalletScreen());
            System.out.println(this.mainView.getNameTextFieldWalletScreen());

            try {
                wallet.findPublicKey(this.mainView.getNameTextFieldWalletScreen(), this.mainView.getSurnameTextFieldWalletScreen());

                DatagramPacket packet = wallet.listenToNodeRespond();
                NodeRespond respond = wallet.unpackRespond(packet);

                FindPublicKeyRespond r = (FindPublicKeyRespond) respond;
                List<String> publicKeys = r.getPublicKeys();

               /* for (String publicKey : publicKeys) {
                    System.out.println(publicKey);

                    searchBySurname = new reciveDialog(publicKey);

                }*/
               if(publicKeys.size() == 0) {
                   System.out.println("Pub key: " + publicKeys.size());
                   searchBySurname = new reciveDialog("not found");
               }
                else
                    searchBySurname = new reciveDialog(publicKeys.get(0));


            } catch (IOException | ClassNotFoundException e1) {
                e1.printStackTrace();
            }




            searchBySurname.showAndWait();
        });

        this.mainView.setAddCompanyMainView(e ->
        {
            addCompanyDialog = new addCompanyDialog();

            addCompanyDialog.showAndWait().ifPresent(handler -> {
                System.out.println("Name=" + handler.getFirst() + ", Sector: " + handler.getSecond() + "PhoneNumber: " + handler.getThird());
                try {
                    DatagramPacket packet;
                    NodeRespond respond;


                    wallet.addCompany(handler.getFirst(), handler.getSecond(),
                            handler.getThird(), handler.getFour(), handler.getFive(), handler.getSix(), handler.getSeven(),
                            handler.getEight(), handler.getNine());


                    packet = wallet.listenToNodeRespond();
                    respond = wallet.unpackRespond(packet);

                    AddCompanyRespond companyRespond = (AddCompanyRespond) respond;

                    System.out.println(companyRespond.getCompanyId());


                } catch (IOException | ClassNotFoundException e10) {
                    e10.printStackTrace();
                }

            });

        });

        this.mainView.setAddPersonalDataWalletScreen(e ->
        {
            updatePersonalData = new updatePersonalData();

            updatePersonalData.showAndWait().ifPresent(handler -> {
                System.out.println("Name=" + handler.getFirst() + ", Surname: " + handler.getSecond() + "Company: " + handler.getThird());
                try {
                    DatagramPacket packet;
                    NodeRespond respond;

                    wallet.setPersonalData(wallet.getAddress(), Integer.parseInt(handler.getThird()), handler.getFirst(), handler.getSecond(),
                            handler.getFour(), handler.getFive(), handler.getSix(), handler.getSeven(),
                            handler.getEight(), handler.getNine());

                    System.out.println("ID" + Integer.parseInt(handler.getThird()) + "Name" + handler.getFirst() + "Surname" + handler.getSecond() +
                            "Email" + handler.getFour() + "Country" + handler.getFive() + "Postal Code" + handler.getSix() + "City" + handler.getSeven() +
                            "Street" + handler.getEight() + "Apartment" + handler.getNine());

                  /* wallet.setPersonalData(wallet.getAddress(), Integer.parseInt(handler.getThird()), handler.getFirst(),  handler.getSecond(),
                           handler.getFour(),  handler.getFive(), handler.getSix() , handler.getSeven(),
                           handler.getEight(),handler.getNine());*/

                    packet = wallet.listenToNodeRespond();
                    respond = wallet.unpackRespond(packet);

                    System.out.println(respond);


                } catch (IOException | ClassNotFoundException e10) {
                    e10.printStackTrace();
                }

            });

        });

        this.mainView.setLoginButtonLoginUsingSavedWallet(new listenForLoginUsingSavedWallet());
        this.mainView.setWalletsManageSetting(utils.getListOfWallets().toArray(new String[0]));
        this.mainView.setDeleteButtonManageSetting(new listenForDeleteButtonManageSetting());
        this.mainView.setViewButtonManageSetting(new listenForViewButtonManageSetting());

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

                mainView.setScreenVisible("walletScreen");

                try {
                    System.out.println("Weszlo");
                    // listen to node respond -  result of creating
                    DatagramPacket packet;

                    // unpack respond
                    NodeRespond respond;

                    // System.out.println(respond);


                    // send balance request
                    wallet.getBalance();

                    // listen to node respond - balance
                    packet = wallet.listenToNodeRespond();

                    // unpack respond
                    respond = wallet.unpackRespond(packet);

                    // cast to BalanceRespond
                    GetBalanceRespond balanceRespond = (GetBalanceRespond) respond;
                    int balance =  balanceRespond.getBalance();

                    System.out.println("Your Balance is: ");

                    System.out.println(balance);
                    mainView.setLabelAmountWalletScreen(Integer.toString(balance));



                } catch (IOException | ClassNotFoundException e5) {
                    e5.printStackTrace();
                }

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
                    mainView.setWalletsManageSetting(utils.getListOfWallets().toArray(new String[0]));


                    System.out.println("Password Equals ");
                    mainView.setScreenVisible("mainPanel");

                } else {
                    System.out.println("Password not equal");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private class listenForDeleteButtonManageSetting implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {


            try {
                String temp = mainView.getSelectedWalletManageSetting();
                System.out.println(temp);
                utils.deleteWalletFile(temp);
                System.out.println(temp + ": delted.");
                mainView.setWalletsManageSetting(utils.getListOfWallets().toArray(new String[0]));
                mainView.setWalletsLoginUsingSavedWallet(utils.getListOfWallets().toArray(new String[0]));

            } catch (Exception a) {
                a.printStackTrace();
            }

        }
    }



    private class listenForViewButtonManageSetting implements EventHandler<ActionEvent> {
        String[] keys;

        @Override
        public void handle(ActionEvent event) {


            try {
                String temp = mainView.getSelectedWalletManageSetting();

                keys = utils.decipherWalletFile(temp, mainView.getPasswordManageSetting());


                keysDialog = new keysDialog(keys[0], keys[1]);
                System.out.println(keysDialog.getPublicKey());
                System.out.println(keysDialog.getPrivateKey());

                keysDialog.showAndWait();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


}
