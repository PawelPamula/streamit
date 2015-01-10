package streamExample.coserver;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import streamExample.utils.HostData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class CoordinationServerAgent
{

    protected final static Logger logger = LoggerFactory.getLogger(CoordinationServerAgent.class);
    private ServerSocket serverSocket = null;
    private final int port;
    private Socket clientSocket;
    protected final ArrayList<InetSocketAddress> clients;

    public CoordinationServerAgent(int port) {
        this.port = port;
        this.clients = new ArrayList<InetSocketAddress>();
        try {
            serverSocket = new ServerSocket(port);
            logger.debug("Coordination server running..");
            run();
        } catch (IOException e) {
            logger.debug("Cannot start coordination server! Message: "+e.getMessage());
        }

    }

    public void run() {
        while(true) {
            try {
                clientSocket = serverSocket.accept();
                logger.debug("Incoming connection!");
                ObjectOutputStream outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream inFromClient = new ObjectInputStream(clientSocket.getInputStream());
                HostData hostData = (HostData) inFromClient.readObject();
                InetSocketAddress clientAddress =
                        new InetSocketAddress(clientSocket.getInetAddress(), hostData.getPort());


                logger.debug("Received message: "+hostData.getType());
                if((clients.size() == 0 && hostData.getType().equals(CoordinationServer.SERVER_CONNECTING))
                    || (clients.size()>0 && hostData.getType().equals(CoordinationServer.CLIENT_CONNECTING)))
                {
                    clients.add(clientAddress);
                    logger.debug(clientAddress.toString());
                }

                if(clients.size() > 1 && hostData.getType().equals(CoordinationServer.CLIENT_CONNECTING)) {
                    InetSocketAddress address = getAddressForChannel();
                    outToClient.writeObject(address);
                }


            } catch (IOException e) {
                logger.debug(e.getMessage() + e.getStackTrace());
            } catch (ClassNotFoundException e) {
                logger.debug(e.getMessage() + e.getStackTrace());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    logger.debug(e.getMessage()+e.getStackTrace());
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private InetSocketAddress getAddressForChannel() {
        int index = clients.size()-1;
        index = (index % 2 == 1) ? index/2 : (index-1)/2;

        return clients.get(index);
    }

}
