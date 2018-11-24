package datagramInterfaces;
import database.Database;
import account.ReceiveBlock;
import account.SendBlock;
import cryptography.CryptoConverter;
import cryptography.EllipticCurveHelper;
import node.ClientTCP;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static datagramInterfaces.ErrorCode.*;

public class PerformTransaction extends WalletRequest {
    private SendBlock sendBlock;
    private ReceiveBlock receiveBlock;
    ClientTCP clientTCP=new ClientTCP(new Socket("localhost", 6667));;
    List<InetAddress> TCPnodes=new ArrayList<>();

    public PerformTransaction(SendBlock sendBlock, ReceiveBlock receiveBlock) throws IOException {
        this.sendBlock = sendBlock;
        this.receiveBlock = receiveBlock;
    }
    public PerformTransaction(SendBlock sendBlock, ReceiveBlock receiveBlock,ClientTCP clientTCP, List<InetAddress> TCPnodes) throws IOException {
        this.sendBlock = sendBlock;
        this.receiveBlock = receiveBlock;
        this.TCPnodes=TCPnodes;
        this.clientTCP=clientTCP;
    }

    @Override
    public NodeRespond handle(Connection con) throws SQLException, IOException {
        String sender = receiveBlock.getSource();
        String recipient = sendBlock.getRecipient();
        int amount = sendBlock.getAmount();
        byte[] signature = CryptoConverter.hexStringToByteArray(sendBlock.getSignature());
        if(!Database.accountExists(con, sender))
            return new NodeRespond(UNKNOWN_SENDER);
        if(!Database.accountExists(con, recipient))
            return new NodeRespond(UNKNOWN_RECIPIENT);

        if(sender.equals(recipient))
            return new NodeRespond(SELF_TRANSACTION);

        try {
            PublicKey senderPublicKey = CryptoConverter.hexToPublicKey(sender);
            if(!sendBlock.getSignature().equals(receiveBlock.getSignature()))
                return new NodeRespond(INVALID_SIGNATURE);
            if(!EllipticCurveHelper.verifySignature(senderPublicKey, sender + recipient + amount, signature)) {
                System.out.println("valid signature");
                return new NodeRespond(INVALID_SIGNATURE);
            }
        } catch (InvalidKeyException | InvalidKeySpecException | SignatureException e) {
            System.out.println("catch");
            return new NodeRespond(INVALID_SIGNATURE);
        }
        if(amount > Database.getBalance(con, sender) || amount <= 0)
            return new NodeRespond(INVALID_AMOUNT);

        Database.performTransaction(con, sender, recipient, amount, CryptoConverter.bytesToHexString(signature),
                sendBlock.getHash(), receiveBlock.getHash());
                System.out.println("PERFORMIG TCP TRANSACTION");
              clientTCP.SendTransaction(sendBlock,receiveBlock);
        return new NodeRespond(OK);
    }
}
