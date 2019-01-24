package node;

import constants.Constants;
import database.Database;
import database.InitDatabase;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NodeTCP implements Runnable {

    private ServerSocket welcomeSocket ;
    private static Connection connection;
    List<InetAddress> TCPnodes=new ArrayList<>();
    List<ClientTCP> clientTCP=new ArrayList<>();
    public NodeTCP() throws IOException, SQLException, ClassNotFoundException {
        Connection connection = Database.connect();
        Statement statement = Database.createStatement(connection);
        try {
            System.out.println("DROP");
            InitDatabase.dropTriggers(statement);
            InitDatabase.dropConstraints(statement);
            InitDatabase.dropSchema(statement);
            InitDatabase.dropSequences(statement);
            InitDatabase.dropProcedures(statement);
            InitDatabase.dropFunctions(statement);
        }catch ( Exception e)
        {
            System.out.println(e.toString());
        }
            System.out.println("INIT");
            InitDatabase.createSchema(statement);
            InitDatabase.createSequences(statement);
            InitDatabase.createTriggers(statement);
            InitDatabase.createProcedures(statement);
            InitDatabase.createFuntions(statement);

        Database.closeConnection(connection, statement);

        InitDatabase.createConstraints(statement);
        this.welcomeSocket =new ServerSocket(Constants.TCP_PORT);

        clientTCP.add(new ClientTCP(new Socket("localhost", 6667)));
        System.out.println("getdatabase");
        clientTCP.get(0).GetDatabase();
        new Thread(clientTCP.get(clientTCP.size()-1)).start();

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


    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        NodeTCP tcp=new NodeTCP();
        new Thread(tcp).start();
    }
}


