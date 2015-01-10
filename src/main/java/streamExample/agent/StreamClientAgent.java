package streamExample.agent;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import streamExample.channel.StreamClientChannelPipelineFactory;
import streamExample.handler.StreamClientListener;
import streamExample.handler.StreamFrameListener;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.Executors;

public class StreamClientAgent implements IStreamClientAgent {
    protected final static Logger logger = LoggerFactory.getLogger(StreamClientAgent.class);
    protected final ClientBootstrap clientBootstrap;
        protected final StreamClientListener streamClientListener;
    protected final StreamFrameListener streamFrameListener;
    protected final Dimension dimension;
    protected Channel clientChannel;
    private OnConnectListener callback;
    protected final String coServerAddress = "localhost";
    protected final int coServerPort = 20002;

    public interface OnConnectListener {
        public void onConnected();
    }

    public StreamClientAgent(StreamFrameListener streamFrameListener,
                             Dimension dimension, OnConnectListener callback) {
        super();
        this.callback = callback;
        this.dimension = dimension;
        this.clientBootstrap = new ClientBootstrap();
        this.clientBootstrap.setFactory(new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));
        this.streamFrameListener = streamFrameListener;
        this.streamClientListener = new StreamClientListenerIMPL();
        this.clientBootstrap.setPipelineFactory(
                new StreamClientChannelPipelineFactory(
                        streamClientListener,
                        streamFrameListener,
                        dimension)
        );


    }

    public InetSocketAddress getStreamServerAddress() {
        Socket clientSocket = null;
        InetSocketAddress address = null;
        try {
            clientSocket = new Socket(coServerAddress, coServerPort);
            ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
            address = (InetSocketAddress) inFromServer.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return address;
    }

    @Override
    public void connect(SocketAddress streamServerAddress) {
        // find address to connect to 
        do {
            streamServerAddress = getStreamServerAddress();
        } while(streamServerAddress == null);

        logger.info("going to connect to stream server :{}", streamServerAddress);
        clientBootstrap.connect(streamServerAddress);
    }

    @Override
    public void stop() {
        clientChannel.close();
        clientBootstrap.releaseExternalResources();
    }

    protected class StreamClientListenerIMPL implements StreamClientListener {

        @Override
        public void onConnected(Channel channel) {
            //	logger.info("stream connected to server at :{}",channel);
            clientChannel = channel;
        }

        @Override
        public void onDisconnected(Channel channel) {
            //	logger.info("stream disconnected to server at :{}",channel);
        }

        @Override
        public void onException(Channel channel, Throwable t) {
            //	logger.debug("exception at :{},exception :{}",channel,t);
        }


    }

}
