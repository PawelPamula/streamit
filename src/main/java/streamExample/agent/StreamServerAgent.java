package streamExample.agent;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import streamExample.channel.StreamServerChannelPipelineFactory;
import streamExample.handler.H264StreamEncoder;
import streamExample.handler.StreamServerListener;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.SocketAddress;
import java.util.concurrent.*;

public class StreamServerAgent implements IStreamServerAgent {
    protected final static Logger logger = LoggerFactory.getLogger(StreamServerAgent.class);

    protected final ImageSource imageSource;
    protected final Dimension dimension;
    protected final ChannelGroup channelGroup = new DefaultChannelGroup();
    protected final ServerBootstrap serverBootstrap;
    //I just move the stream encoder out of the channel pipeline for the performance
    protected final H264StreamEncoder h264StreamEncoder;
    protected volatile boolean isStreaming;
    protected ScheduledExecutorService timeWorker;
    protected ExecutorService encodeWorker;
    protected double FPS = 25;
    protected ScheduledFuture<?> imageGrabTaskFuture;

    public StreamServerAgent(ImageSource imageSource, Dimension dimension) {
        super();
        this.imageSource = imageSource;
        this.dimension = dimension;
        this.serverBootstrap = new ServerBootstrap();
        this.serverBootstrap.setFactory(new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));
        this.serverBootstrap.setPipelineFactory(new StreamServerChannelPipelineFactory(
                new StreamServerListenerIMPL()));
        this.timeWorker = new ScheduledThreadPoolExecutor(1);
        this.encodeWorker = Executors.newSingleThreadExecutor();
        this.h264StreamEncoder = new H264StreamEncoder(dimension, false);
    }


    public double getFPS() {
        return FPS;
    }

    public void setFPS(double FPS) {
        this.FPS = FPS;
    }

    @Override
    public void start(SocketAddress streamAddress) {
        logger.info("Server started :{}", streamAddress);
        Channel channel = serverBootstrap.bind(streamAddress);
        channelGroup.add(channel);
    }

    @Override
    public void stop() {
        logger.info("server is stoping");
        channelGroup.close();
        timeWorker.shutdown();
        encodeWorker.shutdown();
        serverBootstrap.releaseExternalResources();
    }


    private class StreamServerListenerIMPL implements StreamServerListener {

        @Override
        public void onClientConnectedIn(Channel channel) {
            //here we just start to stream when the first client connected in
            channelGroup.add(channel);
            if (!isStreaming) {
                Runnable imageGrabTask = new ImageGrabTask();
                ScheduledFuture<?> imageGrabFuture =
                        timeWorker.scheduleWithFixedDelay(imageGrabTask,
                                0,
                                (long) (1000000 / FPS),
                                TimeUnit.MICROSECONDS);
                imageGrabTaskFuture = imageGrabFuture;
                isStreaming = true;
            }
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

        protected volatile long frameCount = 0;

        private class ImageGrabTask implements Runnable {

            @Override
            public void run() {
                logger.info("image grabed ,count :{}", frameCount++);
                BufferedImage bufferedImage = imageSource.getImage();
                encodeWorker.execute(new EncodeTask(bufferedImage));
            }

        }

        private class EncodeTask implements Runnable {
            private final BufferedImage image;

            public EncodeTask(BufferedImage image) {
                super();
                this.image = image;
            }

            @Override
            public void run() {
                try {
                    Object msg = h264StreamEncoder.encode(image);
                    if (msg != null) {
                        channelGroup.write(msg);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    }
}
