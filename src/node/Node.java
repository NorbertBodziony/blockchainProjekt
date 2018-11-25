package node;
import database.Database;
import constants.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static datagramInterfaces.DatagramMessage.DATAGRAM_SIZE;

public class Node implements Runnable {
    private DatagramSocket socket;
    private Connection connection;
    List<ClientTCP> clientTCP;
    List<InetAddress> TCPnodes;
    public Node(List<InetAddress> TCPnodes,List<ClientTCP> clientTCP) throws SocketException {
        this.socket = new DatagramSocket(Constants.NODE_PORT, Constants.NODE_IP);
        this.connection = Database.connect();
        this.TCPnodes=TCPnodes;
        this.clientTCP=clientTCP;
        System.out.println("node UDP START");
        if(clientTCP==null)
        {
            System.out.println("ERROR");
        }else
        {
            System.out.println("works!");
        }
    }
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

        new Thread(new RequestHandler(socket, packet, connection,clientTCP,TCPnodes)).start();
    }


    @Override
    public void run() {


        try {
            while (true) {
                System.out.println("listen");
                DatagramPacket request = this.listen();
                System.out.println("new request");
                this.handle(request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}