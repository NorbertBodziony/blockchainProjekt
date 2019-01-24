package node;

import constants.Constants;
import database.Database;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class GenesisNode implements Runnable {

    private ServerSocket welcomeSocket ;
    private static Connection connection;
    List<InetAddress> TCPnodes=new ArrayList<>();
    List<ClientTCP> clientTCP=new ArrayList<>();
    public GenesisNode() throws IOException {

        this.connection = Database.connect();
        this.welcomeSocket =new ServerSocket(Constants.TCP_PORT);
        if(clientTCP==null)
        {
            System.out.println("ERROR");
        }else
        {
            System.out.println("works!");
        }

        Node nodeUDP=new Node(TCPnodes,clientTCP);
        new Thread(nodeUDP).start();



    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Node startTCP");
                Socket connectionSocket = welcomeSocket.accept();

                new ServerThread(connectionSocket,TCPnodes,clientTCP).start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) throws IOException {
        GenesisNode tcp=new GenesisNode();
        new Thread(tcp).start();
    }
}


