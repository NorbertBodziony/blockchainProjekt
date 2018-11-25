package node;

import account.ReceiveBlock;
import account.SendBlock;
import constants.Constants;
import database.*;

import datagramInterfaces.TCPinterface;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientTCP implements Runnable {
    Socket clientSocket;
    private Connection connection;
    List<InetAddress> TCPnodes;

    public ClientTCP(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.connection = Database.connect();
    }
    public ClientTCP(Socket clientSocket,List<InetAddress> TCPnodes) {
        this.clientSocket = clientSocket;
        this.connection = Database.connect();
        this.TCPnodes=TCPnodes;
    }
    public void GetDatabase() throws IOException, ClassNotFoundException, SQLException {
        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
        TCPinterface.TCPid request=TCPinterface.TCPid.Blockchain;
        outToServer.writeObject(request);

        List<BlockchainData> AccountData= (List<BlockchainData>) inFromServer.readObject();
        for(int i=0;i<AccountData.size();i++)
        {System.out.println("dataupdate1");
            Database.InsertBlockchain(connection,AccountData.get(i));

        }
        List<AccountList> AccountData1= (List<AccountList>) inFromServer.readObject();

        for(int i=0;i<AccountData1.size();i++)
        {System.out.println("dataupdate2");
            Database.InsertAccounts(connection,AccountData1.get(i));
        }
        List<SendBlockData> AccountData2= (List<SendBlockData>) inFromServer.readObject();

        for(int i=0;i<AccountData2.size();i++)
        {System.out.println("dataupdate3");
            Database.InsertSendBlock(connection,AccountData2.get(i));
        }
        List<ReceiveBlockData> AccountData3= (List<ReceiveBlockData>) inFromServer.readObject();

        for(int i=0;i<AccountData3.size();i++)
        {System.out.println("dataupdate4");
            Database.InsertReceiveBlock(connection,AccountData3.get(i));
        }
        List<BlockData> AccountData4= (List<BlockData>) inFromServer.readObject();

        for(int i=0;i<AccountData4.size();i++)
        {System.out.println("dataupdate6");
            Database.InsertBlocks(connection,AccountData4.get(i));
        }
    }
    public void SendTransaction(SendBlock sendBlock, ReceiveBlock receiveBlock) throws IOException {

        System.out.println("send to "+clientSocket.getInetAddress()+clientSocket.getPort());
        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());

        TCPinterface.TCPid request=TCPinterface.TCPid.Transaction;

        outToServer.writeObject(request);
        outToServer.writeObject(sendBlock);
        outToServer.writeObject(receiveBlock);

    }
    @Override
    public void run() {
        while(true)
        {

        }

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        Socket clientSocket = new Socket("localhost", Constants.TCP_PORT);
        ClientTCP client=new ClientTCP(clientSocket);
        client.GetDatabase();
        client.run();
    }

    @Override
    public String toString() {
        return "ClientTCP{" +
                "clientSocket=" + clientSocket.getLocalAddress() +
                ", connection=" + connection +
                ", TCPnodes=" + TCPnodes +
                '}';
    }
}
