package node;

import datagramInterfaces.NodeRespond;
import datagramInterfaces.WalletRequest;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.sql.Connection;
import java.sql.SQLException;

import static datagramInterfaces.DatagramMessage.DATAGRAM_SIZE;

public class RequestHandler implements Runnable {
    private DatagramSocket socket;
    private DatagramPacket packet;
    private Connection connection;

    public RequestHandler(DatagramSocket socket, DatagramPacket packet, Connection connection) {
        this.socket = socket;
        this.packet = packet;
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            WalletRequest request = unpackRequest();
            System.out.println(request.toString());
            NodeRespond respond = request.handle(connection);
            sendRespond(respond);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendRespond(NodeRespond respond) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(DATAGRAM_SIZE);
        ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
        os.flush();
        os.writeObject(respond);
        os.flush();
        byte[] sendBuf = byteStream.toByteArray();
        System.out.println("buff size: " + sendBuf.length);
        DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length,
                this.packet.getAddress(), this.packet.getPort());
        System.out.println("packet length = " + packet.getLength());
        socket.send(packet);
        os.close();
    }

    public WalletRequest unpackRequest() throws IOException, ClassNotFoundException {
        byte[] recvBuf = packet.getData();
        ByteArrayInputStream byteStream = new ByteArrayInputStream(recvBuf);
        ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream));
        WalletRequest request = (WalletRequest) is.readObject();
        is.close();
        return request;
    }

}
