package streamExample.channel;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import streamExample.agent.EncodedFrameListener;
import streamExample.handler.H264StreamDecoder;
import streamExample.handler.StreamClientHandler;
import streamExample.handler.StreamClientListener;
import streamExample.handler.StreamFrameListener;

import java.awt.*;

public class StreamClientChannelPipelineFactory implements ChannelPipelineFactory {
    protected final StreamClientListener streamClientListener;
    protected final StreamFrameListener streamFrameListener;
    protected final Dimension dimension;
    private final EncodedFrameListener encodedFrameListener;

    public StreamClientChannelPipelineFactory(
            StreamClientListener streamClientListener,
            StreamFrameListener streamFrameListener,
            EncodedFrameListener encodedFrameListener,
            Dimension dimension) {
        super();
        this.streamClientListener = streamClientListener;
        this.streamFrameListener = streamFrameListener;
        this.encodedFrameListener = encodedFrameListener;
        this.dimension = dimension;
    }

    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();
        // add an simple indicator handler
        pipeline.addLast("stream client handler", new StreamClientHandler(streamClientListener));
        // add the frame codec
        pipeline.addLast("frame decoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        // add the video stream handler
        // change the below false --> true ,if using the netty's frame codec
        pipeline.addLast("encoded image notifier", new EncodedImageNotifier(encodedFrameListener));
        pipeline.addLast("stream handler", new H264StreamDecoder(streamFrameListener, dimension, false, false));

        return pipeline;
    }


}
