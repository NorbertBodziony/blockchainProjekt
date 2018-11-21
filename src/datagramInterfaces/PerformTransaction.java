package datagramInterfaces;

import account.ReceiveBlock;
import account.SendBlock;
import cryptography.CryptoConverter;
import cryptography.EllipticCurveHelper;
import database.Database;

import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;

import static datagramInterfaces.ErrorCode.*;

public class PerformTransaction extends WalletRequest {
    private SendBlock sendBlock;
    private ReceiveBlock receiveBlock;

    public PerformTransaction(SendBlock sendBlock, ReceiveBlock receiveBlock) {
        this.sendBlock = sendBlock;
        this.receiveBlock = receiveBlock;
    }

    @Override
    public NodeRespond handle(Connection con) throws SQLException {
        String sender = receiveBlock.getSource();
        String recipient = sendBlock.getRecipient();
        int amount = sendBlock.getAmount();
        byte[] signature = CryptoConverter.hexStringToByteArray(sendBlock.getSignature());
        if(!Database.accountExists(con, sender))
            return new NodeRespond(UNKNOWN_SENDER);
        if(!Database.accountExists(con, recipient))
            return new NodeRespond(UNKNOWN_RECIPIENT);

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
                sendBlock.getPrevBlock(), receiveBlock.getPrevBlock());
        return new NodeRespond(OK);
    }
}
