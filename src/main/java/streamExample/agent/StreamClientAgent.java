package streamExample.agent;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import streamExample.channel.StreamClientChannelPipelineFactory;
import streamExample.handler.H264StreamDecoder;
import streamExample.handler.StreamClientListener;
import streamExample.handler.StreamFrameListener;

import java.awt.*;
import java.net.InetSocketAddress;
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
    private ForwarderServerAgent forwarderServerAgent;

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
    }

    public InetSocketAddress getStreamServerAddress() {
        return null;
    }

    @Override
    public void connect(SocketAddress streamServerAddress) {
        logger.info("going to connect to stream server :{}", streamServerAddress);
        clientBootstrap.connect(streamServerAddress);
        int port = 1024 + new Random().nextInt(20000);
        forwarderServerAgent.start(new InetSocketAddress(port));
        System.out.println("OUR SERVER PORT IZ " + port + " !!!11!!1111oneoneone");
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
