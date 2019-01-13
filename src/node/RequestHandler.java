package node;

import datagramInterfaces.*;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static datagramInterfaces.DatagramMessage.DATAGRAM_SIZE;

public class RequestHandler implements Runnable {
    private DatagramSocket socket;
    private DatagramPacket packet;
    private Connection connection;
    List<ClientTCP> clientTCP;
    List<InetAddress> TCPnodes;

    public RequestHandler(DatagramSocket socket, DatagramPacket packet, Connection connection,List<ClientTCP> clientTCP,List<InetAddress> TCPnodes) {
        this.socket = socket;
        this.packet = packet;
        this.connection = connection;
        this.TCPnodes=TCPnodes;
        this.clientTCP=clientTCP;
        if(clientTCP==null)
        {
            System.out.println("ERROR");
        }
    }
    public RequestHandler(DatagramSocket socket, DatagramPacket packet, Connection connection) {
        this.socket = socket;
        this.packet = packet;
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            WalletRequest request = unpackRequest();

            if(request.getClass()==PerformTransaction.class)
            {
                ((PerformTransaction) request).setClientTCP(clientTCP);
                ((PerformTransaction) request).setTCPnodes(TCPnodes);
            }
            if(request.getClass()==CreateAccount.class)
            {
                ((CreateAccount)request).setClientTCP(clientTCP);
            }
            if(request.getClass()==AddCompany.class)
            {
                ((AddCompany)request).setClientTCP(clientTCP);
            }
            if(request.getClass()==SetPersonalData.class)
            {
                ((SetPersonalData)request).setClientTCP(clientTCP);
            }
            if(request instanceof GetTransactionHistory) {
                List<NodeRespond> responds = ((GetTransactionHistory) request).handleHistory(connection);
                for(NodeRespond respond : responds)
                    sendRespond(respond);
            }else {
                NodeRespond respond = request.handle(connection);
                sendRespond(respond);
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendRespond(NodeRespond respond) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(DATAGRAM_SIZE);
        ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
        os.flush();
        os.writeObject(respond);
        os.flush();
        byte[] sendBuf = byteStream.toByteArray();
        System.out.println("buff size: " + sendBuf.length);
        DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length,
                this.packet.getAddress(), this.packet.getPort());
        System.out.println("packet length = " + packet.getLength());
        socket.send(packet);
        os.close();
    }

    public WalletRequest unpackRequest() throws IOException, ClassNotFoundException {
        byte[] recvBuf = packet.getData();
        ByteArrayInputStream byteStream = new ByteArrayInputStream(recvBuf);
        ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream));
        WalletRequest request = (WalletRequest) is.readObject();
        is.close();
        return request;
    }

}
