package streamExample.agent;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import streamExample.channel.StreamServerChannelPipelineFactory;
import streamExample.handler.StreamServerListener;

import java.net.SocketAddress;
import java.util.concurrent.Executors;

public class ForwarderServerAgent implements IStreamServerAgent {
    protected final static Logger logger = LoggerFactory.getLogger(ForwarderServerAgent.class);
    protected final ServerBootstrap serverBootstrap;
    protected final ChannelGroup channelGroup = new DefaultChannelGroup();

    public ForwarderServerAgent() {
        super();
        serverBootstrap = new ServerBootstrap();
        this.serverBootstrap.setFactory(new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));
        this.serverBootstrap.setPipelineFactory(new StreamServerChannelPipelineFactory(
                new StreamServerListenerIMPL()));
    }

    @Override
    public void start(SocketAddress streamAddress) {
        logger.info("Server started :{}", streamAddress);
        Channel channel = serverBootstrap.bind(streamAddress);
        channelGroup.add(channel);
    }

    @Override
    public void stop() {

    }

    public void forwardImage(Object image) {
        channelGroup.write(image);
    }

    private class StreamServerListenerIMPL implements StreamServerListener {

        @Override
        public void onClientConnectedIn(Channel channel) {
            //here we just start to stream when the first client connected in
            channelGroup.add(channel);
            logger.info("current connected clients :{}", channelGroup.size());
        }

        @Override
        public void onClientDisconnected(Channel channel) {
            channelGroup.remove(channel);
            int size = channelGroup.size();
            logger.info("current connected clients :{}", size);
        }

        @Override
        public void onException(Channel channel, Throwable t) {
            channelGroup.remove(channel);
            channel.close();
            int size = channelGroup.size();
            logger.info("current connected clients :{}", size);
        }
    }
}
