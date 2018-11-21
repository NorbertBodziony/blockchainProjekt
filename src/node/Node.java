package node;

import constants.Constants;
import database.Database;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.SQLException;

import static datagramInterfaces.DatagramMessage.DATAGRAM_SIZE;

public class Node {
    private DatagramSocket socket;
    private Connection connection;

    public Node() throws SocketException {
        this.socket = new DatagramSocket(Constants.NODE_PORT, Constants.NODE_IP);
        this.connection = Database.connect();
    }

    public static void main(String[] args) throws IOException, SQLException {
        Node node = new Node();
        try {
            while (true) {
                System.out.println("listen");
                DatagramPacket request = node.listen();
                System.out.println("new request");
                node.handle(request);
            }
        }finally {
            node.connection.close();
        }
    }

    public DatagramPacket listen() throws IOException {
        byte[] data = new byte[DATAGRAM_SIZE];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        socket.receive(packet);
        return packet;
    }

    public void handle(DatagramPacket packet) {
        new Thread(new RequestHandler(socket, packet, connection)).start();
    }


}
