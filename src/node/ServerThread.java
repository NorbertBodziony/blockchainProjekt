package node;

import account.*;
import database.Database;
import datagramInterfaces.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread {
    Socket connectionSocket;
    private static Connection connection;
    List<InetAddress> TCPnodes;
    List<ClientTCP> clientTCP;

    public ServerThread(Socket connectionSocket, List<InetAddress> TCPnodes, List<ClientTCP> clientTCP) {
        this.connectionSocket = connectionSocket;
        this.TCPnodes = TCPnodes;
        this.clientTCP = clientTCP;
        this.connection = Database.connect();
    }

    public void run(){
        try {
            while (true) {

                ObjectOutputStream outToUser = new ObjectOutputStream(connectionSocket.getOutputStream());
                ObjectInputStream inFromUser = new ObjectInputStream(connectionSocket.getInputStream());
                TCPinterface.TCPid request = (TCPinterface.TCPid) inFromUser.readObject();
                if (request.equals(TCPinterface.TCPid.Blockchain)) {
                    outToUser.writeObject(Database.GetAddressData(connection));
                    outToUser.writeObject(Database.GetCompany(connection));
                    outToUser.writeObject(Database.GetCustomer(connection));
                    outToUser.writeObject(Database.GetBlockchain(connection));
                    outToUser.writeObject(Database.GetAccounts(connection));
                    outToUser.writeObject(Database.GetSendBlocks(connection));
                    outToUser.writeObject(Database.GetReciveBlocks(connection));
                    outToUser.writeObject(Database.GetBlocks(connection));
                    System.out.println("Adding new server");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(connectionSocket.getInetAddress());
                    System.out.println(connectionSocket.toString());

                    clientTCP.add(new ClientTCP(new Socket(connectionSocket.getInetAddress(), 6666)));
                    new Thread(clientTCP.get(clientTCP.size() - 1)).start();
                    TCPnodes.add(connectionSocket.getInetAddress());
                    System.out.println(clientTCP.size());

                }
                if (request.equals(TCPinterface.TCPid.Transaction)) {
                    System.out.println("new transaction");
                    SendBlock sendBlock = (SendBlock) inFromUser.readObject();
                    ReceiveBlock receiveBlock = (ReceiveBlock) inFromUser.readObject();
                    new PerformTransaction(sendBlock, receiveBlock, clientTCP, TCPnodes).handle(connection);
                }
                if (request.equals(TCPinterface.TCPid.NewAccount)) {
                    System.out.println("new Account");
                    Account account = (Account) inFromUser.readObject();
                    ReceiveBlock receiveBlock = (ReceiveBlock) inFromUser.readObject();
                    if (Database.AccountExist(connection, account)) {
                        System.out.println("account exist");
                    } else {
                        new CreateAccount(account, receiveBlock, clientTCP).handle(connection);
                    }
                }
                if (request.equals(TCPinterface.TCPid.Company)) {
                    System.out.println("new Company");
                    int Id= (int) inFromUser.readObject();
                    Address address = (Address) inFromUser.readObject();
                    Company company = (Company) inFromUser.readObject();
                    if(Database.Companyexist(connection,Id)==false){
                   new AddCompany( company, address, clientTCP).handle(connection);}
                    else {
                        System.out.println("data exist");
                    }
                }
                if (request.equals(TCPinterface.TCPid.PersonalData)) {
                    System.out.println("new PersonalData");
                    int Id= (int) inFromUser.readObject();
                    Address address = (Address) inFromUser.readObject();
                    Customer customer = (Customer) inFromUser.readObject();
                    if(Database.GetLastIdPersonalData(connection)<Id){
                    new SetPersonalData(customer,address,clientTCP);
                }else {
                        System.out.println("data exist");
                    }
                }
                sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return ;
        } catch (SQLException e) {
            e.printStackTrace();return ;
        } catch (InterruptedException e) {
            e.printStackTrace();return ;
        }finally {
            ClientTCP clientDisconnected=null;
            for(int i=0;i<clientTCP.size();i++)
            {
                if(clientTCP.get(i).clientSocket.getInetAddress()==connectionSocket.getInetAddress())
                {
                    clientDisconnected=clientTCP.get(i);
                }

            }
            clientTCP.remove(clientDisconnected);
            System.out.println(clientTCP);
        }

    }}

