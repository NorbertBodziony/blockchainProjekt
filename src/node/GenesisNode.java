package node;
import account.ReceiveBlock;
import account.SendBlock;
import constants.Constants;
import database.Database;

import datagramInterfaces.PerformTransaction;
import datagramInterfaces.TCPinterface;

import javax.sound.midi.Soundbank;
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
                    System.out.println("Adding new server");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    clientTCP.add(new ClientTCP(new Socket("localhost", 6667)));
                    new Thread(clientTCP.get(clientTCP.size()-1)).start();
                    TCPnodes.add(connectionSocket.getLocalAddress());
                    System.out.println(TCPnodes.size());

                }
                if(request.equals(TCPinterface.TCPid.Transaction))
                {
                    System.out.println("new transaction");
                    SendBlock sendBlock= (SendBlock) inFromUser.readObject();
                    ReceiveBlock receiveBlock= (ReceiveBlock) inFromUser.readObject();
                    new PerformTransaction(sendBlock,receiveBlock,clientTCP,TCPnodes).handle(connection);
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
        GenesisNode tcp=new GenesisNode();
        new Thread(tcp).start();
    }
}

