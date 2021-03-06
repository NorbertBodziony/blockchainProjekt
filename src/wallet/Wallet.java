package wallet;

import account.Account;
import account.ReceiveBlock;
import account.SendBlock;
import constants.Constants;
import cryptography.CryptoConverter;
import datagramInterfaces.*;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

import static datagramInterfaces.DatagramMessage.DATAGRAM_SIZE;

public class Wallet {
    private int nodePort;
    private InetAddress nodeIp;
    private Account currentAccount;
    private DatagramSocket socket;

    public Wallet(InetAddress nodeAddress, int port) throws SocketException {
        this.nodePort = port;
        this.nodeIp = nodeAddress;
        this.socket = new DatagramSocket();
    }

    public byte[] sign(String previousHash, int amount) throws SignatureException, InvalidKeyException {
        String transactionContents = previousHash + amount;
        return currentAccount.sign(transactionContents);
    }

    public ReceiveBlock createGenesisBlock() throws SignatureException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {
        String recipient = currentAccount.getAddress();
        String sender = "";
        int amount = Constants.INITIAL_BALANCE;
        byte[] signature = sign(Constants.GENESIS_PREV_HASH, amount);
        return new ReceiveBlock(amount, signature, Constants.GENESIS_PREV_HASH, sender);
    }


    public void createAccount() throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        currentAccount = new Account();
        System.out.println("public key: " + currentAccount.getAddress());
        System.out.println("private key: " + CryptoConverter.keyToHexString(currentAccount.getPrivateKey()));
        ReceiveBlock genesisBlock = createGenesisBlock();
        WalletRequest walletRequest = new CreateAccount(currentAccount, genesisBlock);
        send(walletRequest);
    }

    public void getBalance() throws IOException {
        WalletRequest walletRequest = new GetBalance(currentAccount);
        send(walletRequest);
    }

    public void getPreviousHashes(String recipient) throws IOException {
        WalletRequest walletRequest = new GetPreviousHashes(currentAccount.getAddress(), recipient);
        send(walletRequest);
    }

    public void login(String publicKey, String privateKey) throws InvalidKeySpecException {
        currentAccount = new Account(privateKey, publicKey);
    }

    public void performTransaction(int amount, String recipient, String senderPrevHash, String recpientPrevHash)
            throws SignatureException, InvalidKeyException, IOException {
        byte[] senderSignature = sign(senderPrevHash, amount);
        SendBlock sendBlock = new SendBlock(amount, senderSignature, senderPrevHash, recipient);
        ReceiveBlock receiveBlock = new ReceiveBlock(amount, senderSignature, recpientPrevHash, currentAccount.getAddress());
        PerformTransaction walletRequest = new PerformTransaction(sendBlock, receiveBlock);
        send(walletRequest);
    }

    public void getTransactionHistory(boolean directory, String publicKey, String startTime, String stopTime) throws IOException {
        WalletRequest request = new GetTransactionHistory(directory, publicKey, startTime, stopTime);
        send(request);
    }

    public void addCompany(String companyName, String sector, String contactTel, String contactEmail, String country,
                           String postalCode, String city, String street, String apartmentNumber) throws IOException {
        WalletRequest request = new AddCompany(companyName, sector, contactTel, contactEmail,
                country, postalCode, city, street, apartmentNumber);

        send(request);
    }

    public void setPersonalData(String publicKey,  int companyId,  String firstName,  String lastName,
                                String contactEmail,  String country, String postalCode,
                                String city, String street, String apartmentNumber) throws IOException {
        WalletRequest request = new SetPersonalData(publicKey, companyId, firstName, lastName,
                contactEmail, country, postalCode, city, street, apartmentNumber);

        send(request);
    }

    public void findPublicKey(String firstName, String lastName) throws IOException {
        WalletRequest request = new FindPublicKey(firstName, lastName);

        send(request);
    }


    public DatagramPacket listenToNodeRespond() throws IOException {
        byte[] data = new byte[DATAGRAM_SIZE];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        //block function
        socket.receive(packet);
        return packet;
    }

    public void send(WalletRequest request) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(DATAGRAM_SIZE);
        ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
        os.flush();
        os.writeObject(request);
        os.flush();

        byte[] sendBuf = byteStream.toByteArray();
        System.out.println("buff size: " + sendBuf.length);
        DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length, nodeIp, nodePort);
        System.out.println("packet length = " + packet.getLength());
        socket.send(packet);
        os.close();
    }

    public NodeRespond unpackRespond(DatagramPacket packet) throws IOException, ClassNotFoundException {
        byte[] recvBuf = packet.getData();
        ByteArrayInputStream byteStream = new ByteArrayInputStream(recvBuf);
        ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream));
        NodeRespond respond = (NodeRespond) is.readObject();
        is.close();
        return respond;
    }
    public PublicKey getPublicKey() {
        return currentAccount.getPublicKey();
    }
    public PrivateKey getPrivateKey() {
        return currentAccount.getPrivateKey();
    }
    public String getAddress() {
        return CryptoConverter.keyToHexString(currentAccount.getPublicKey());
    }






}
