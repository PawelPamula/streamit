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
import java.awt.image.BufferedImage;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executors;

public class StreamClientAgent implements IStreamClientAgent {
    protected final static Logger logger = LoggerFactory.getLogger(StreamClientAgent.class);
    protected final ClientBootstrap clientBootstrap;
    protected final StreamClientListener streamClientListener;
    protected final StreamFrameListener streamFrameListener;
    protected final H264StreamDecoder h264StreamDecoder;
    protected final Dimension dimension;
    private final EncodedFrameListener encodedFrameListener;
    protected Channel clientChannel;
    private OnConnectListener callback;

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
        this.encodedFrameListener = new EncodedFrameListener() {

            @Override
            public void receiveEncodedFrame(Object frame) {
                System.out.println(frame.toString());
            }
        };
        this.streamClientListener = new StreamClientListenerIMPL();
        this.clientBootstrap.setPipelineFactory(
                new StreamClientChannelPipelineFactory(
                        streamClientListener,
                        streamFrameListener,
                        encodedFrameListener,
                        dimension)
        );
        h264StreamDecoder = new H264StreamDecoder(streamFrameListener, dimension, false, false);
    }

    public InetSocketAddress getStreamServerAddress() {
        return null;
    }

    @Override
    public void connect(SocketAddress streamServerAddress) {
        logger.info("going to connect to stream server :{}", streamServerAddress);
        clientBootstrap.connect(streamServerAddress);
        callback.onConnected();

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
