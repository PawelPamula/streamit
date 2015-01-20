package streamExample.channel;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;
import streamExample.handler.StreamServerHandler;
import streamExample.handler.StreamServerListener;

public class StreamServerChannelPipelineFactory implements ChannelPipelineFactory {
    protected final StreamServerListener streamServerListener;

    public StreamServerChannelPipelineFactory(
            StreamServerListener streamServerListener) {
        super();
        this.streamServerListener = streamServerListener;
    }


    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();
        // comment the netty's frame encoder, if want to use the build in h264 encoder
        pipeline.addLast("frame encoder", new LengthFieldPrepender(4, false));
        pipeline.addLast("stream server handler", new StreamServerHandler(streamServerListener));
        return pipeline;
    }


}
