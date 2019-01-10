package tests;

import constants.Constants;
import datagramInterfaces.FindPublicKey;
import datagramInterfaces.FindPublicKeyRespond;
import datagramInterfaces.NodeRespond;
import org.junit.Test;
import wallet.Wallet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.List;

import static org.junit.Assert.*;

public class FindPublicKeyInterfaceTest {

    @Test
    public void simpleTest() throws IOException, ClassNotFoundException {
        Wallet wallet = new Wallet(Constants.NODE_IP, Constants.NODE_PORT);

        wallet.findPublicKey("first", "last");

        DatagramPacket packet = wallet.listenToNodeRespond();
        NodeRespond respond = wallet.unpackRespond(packet);

        FindPublicKeyRespond r = (FindPublicKeyRespond) respond;
        List<String> publicKeys = r.getPublicKeys();

        for(String publicKey : publicKeys)
            System.out.println(publicKey);
    }

}