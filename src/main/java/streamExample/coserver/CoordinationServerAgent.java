package streamExample.coserver;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
            run();
        } catch (IOException e) {
            logger.debug("Cannot start coordination server! Message: "+e.getMessage());
        }

    }

    public void run() {
        while(true) {
            try {
                clientSocket = serverSocket.accept();
                logger.debug("Making connection!");
                ObjectOutputStream outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
                InetSocketAddress clientAddress =
                        new InetSocketAddress(clientSocket.getLocalAddress(), clientSocket.getPort());

                clients.add(clientAddress);
                if(clients.size() > 1) {
                    InetSocketAddress address = getAddressForChannel();
                    outToClient.writeObject(address);
                }

                
            } catch (IOException e) {
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
        int index = clients.size();
        index = (index % 2 == 1) ? index/2 : (index-1)/2;

        return clients.get(index);
    }

}
