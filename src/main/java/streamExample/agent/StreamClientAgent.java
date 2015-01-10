package streamExample.agent;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import streamExample.channel.StreamClientChannelPipelineFactory;
import streamExample.coserver.CoordinationServer;
import streamExample.handler.H264StreamDecoder;
import streamExample.handler.StreamClientListener;
import streamExample.handler.StreamFrameListener;
import streamExample.utils.HostData;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Random;
import java.util.concurrent.Executors;

public class StreamClientAgent implements IStreamClientAgent, EncodedFrameListener {
    protected final static Logger logger = LoggerFactory.getLogger(StreamClientAgent.class);
    protected final ClientBootstrap clientBootstrap;
    protected final StreamClientListener streamClientListener;
    protected final StreamFrameListener streamFrameListener;
    protected final H264StreamDecoder h264StreamDecoder;
    protected final Dimension dimension;
    protected Channel clientChannel;
//    private OnConnectListener callback;
    private ForwarderServerAgent forwarderServerAgent;
    public int port;

    public StreamClientAgent(StreamFrameListener streamFrameListener,
                             Dimension dimension) {
        super();
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
                        this,
                        dimension)
        );
        h264StreamDecoder = new H264StreamDecoder(streamFrameListener, dimension, false, false);

        forwarderServerAgent = new ForwarderServerAgent();

        // choose a random port
        this.port = 1024 + new Random().nextInt(20000);
    }

    public InetSocketAddress getStreamServerAddress() {
        Socket clientSocket = null;
        InetSocketAddress address = null;
        try {
            clientSocket = new Socket(CoordinationServer.COORDINATION_SERVER_HOST, CoordinationServer.COORDINATION_SERVER_PORT);
            ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
            String msgType = CoordinationServer.CLIENT_CONNECTING;
            outToServer.writeObject(new HostData(null, this.port, msgType));
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
        streamServerAddress = getStreamServerAddress();

        logger.info("going to connect to stream server :{}", streamServerAddress);
        clientBootstrap.connect(streamServerAddress);
        forwarderServerAgent.start(new InetSocketAddress(this.port));
        System.out.println("OUR SERVER PORT IZ " + this.port + " !!!11!!1111oneoneone");
    }

    @Override
    public void stop() {
        clientChannel.close();
        clientBootstrap.releaseExternalResources();
        forwarderServerAgent.stop();
    }

    @Override
    public void receiveEncodedFrame(Object frame) {
        forwarderServerAgent.forwardImage(frame);
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
