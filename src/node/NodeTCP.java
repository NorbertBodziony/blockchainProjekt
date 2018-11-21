package node;
import database.Database;
import datagramInterfaces.GetAccounts;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;

public class NodeTCP implements Runnable {

    private ServerSocket welcomeSocket ;
    private static Connection connection;

    public NodeTCP() throws IOException {
        this.welcomeSocket =new ServerSocket(6666);
        this.connection = Database.connect();

    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Node startTCP");
                Socket connectionSocket = welcomeSocket.accept();
                System.out.println("new user");
                ObjectOutputStream outToUser = new ObjectOutputStream(connectionSocket.getOutputStream());
                ObjectInputStream inFromUser = new ObjectInputStream(connectionSocket.getInputStream());
                if(inFromUser.readObject().getClass().equals(new GetAccounts(1).getClass()))
                {

                    outToUser.writeObject(Database.GetBlockchain(connection));
                    outToUser.writeObject(Database.GetAccounts(connection));
                    outToUser.writeObject(Database.GetSendBlocks(connection));
                    outToUser.writeObject(Database.GetReciveBlocks(connection));
                    outToUser.writeObject(Database.GetBlocks(connection));

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
        tcp.run();

    }
}


