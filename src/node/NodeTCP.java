package node;
import account.ReceiveBlock;
import account.SendBlock;
import constants.Constants;
import database.Database;

import datagramInterfaces.PerformTransaction;
import datagramInterfaces.TCPinterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static datagramInterfaces.DatagramMessage.DATAGRAM_SIZE;

public class NodeTCP implements Runnable {

    private ServerSocket welcomeSocket ;
    private static Connection connection;
    List<InetAddress> TCPnodes=new ArrayList<>();
    public NodeTCP() throws IOException {

        this.connection = Database.connect();
        this.welcomeSocket =new ServerSocket(Constants.TCP_PORT);

        ClientTCP clientTCP=new ClientTCP(new Socket("localhost", Constants.TCP_PORT));
        new Thread(clientTCP).start();
        Node nodeUDP=new Node(TCPnodes,clientTCP);
        new Thread(nodeUDP).start();



    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Node startTCP");
                Socket connectionSocket = welcomeSocket.accept();
                System.out.println(TCPnodes.size());
                TCPnodes.add(connectionSocket.getLocalAddress());
                System.out.println("new user");
                ObjectOutputStream outToUser = new ObjectOutputStream(connectionSocket.getOutputStream());
                ObjectInputStream inFromUser = new ObjectInputStream(connectionSocket.getInputStream());
                TCPinterface.TCPid request= (TCPinterface.TCPid) inFromUser.readObject();
                if(request.equals(TCPinterface.TCPid.Blockchain))
                {

                    outToUser.writeObject(Database.GetBlockchain(connection));
                    outToUser.writeObject(Database.GetAccounts(connection));
                    outToUser.writeObject(Database.GetSendBlocks(connection));
                    outToUser.writeObject(Database.GetReciveBlocks(connection));
                    outToUser.writeObject(Database.GetBlocks(connection));

                }
                if(request.equals(TCPinterface.TCPid.Transaction))
                {
                    SendBlock sendBlock= (SendBlock) inFromUser.readObject();
                    ReceiveBlock receiveBlock= (ReceiveBlock) inFromUser.readObject();
                    new PerformTransaction(sendBlock,receiveBlock).handle(connection);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) throws IOException {
        NodeTCP tcp=new NodeTCP();
        new Thread(tcp).start();
    }
}


