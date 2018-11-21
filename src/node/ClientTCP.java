package node;
import database.Database;
import database.AccountList;
import datagramInterfaces.GetAccounts;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ClientTCP implements Runnable {
    Socket clientSocket;
    private Connection connection;

    public ClientTCP(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.connection = Database.connect();
    }
    public void GetDatabase() throws IOException, ClassNotFoundException, SQLException {
        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
        outToServer.writeObject(new GetAccounts(1));
        List<AccountList> AccountData= (List<AccountList>) inFromServer.readObject();
        for(int i=0;i<AccountData.size();i++)
        {
            Database.InsertAccounts(connection,AccountData.get(i));
        }
    }
    @Override
    public void run() {
        while(true)
        {

        }

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        Socket clientSocket = new Socket("localhost", 6666);
        ClientTCP client=new ClientTCP(clientSocket);
        client.GetDatabase();
    }
}
