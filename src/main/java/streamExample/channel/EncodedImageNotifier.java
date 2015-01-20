package streamExample.channel;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import streamExample.agent.EncodedFrameListener;
import streamExample.handler.StreamFrameListener;

public class EncodedImageNotifier extends OneToOneDecoder {
    private EncodedFrameListener streamFrameListener;

    public EncodedImageNotifier(EncodedFrameListener streamFrameListener) {
        this.streamFrameListener = streamFrameListener;
    }

    @Override
    protected Object decode(ChannelHandlerContext channelHandlerContext, Channel channel, Object o) throws Exception {
        streamFrameListener.receiveEncodedFrame(o);
        return o;
    }
}
