package streamExample.handler;

import com.xuggle.xuggler.*;
import com.xuggle.xuggler.IPixelFormat.Type;
import com.xuggle.xuggler.IStreamCoder.Direction;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import streamExample.handler.frame.FrameEncoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class H264StreamEncoder extends OneToOneEncoder {
    protected final static Logger logger = LoggerFactory.getLogger(Logger.class);
    protected final IStreamCoder iStreamCoder = IStreamCoder.make(Direction.ENCODING, ICodec.ID.CODEC_ID_H264);
    protected final IPacket iPacket = IPacket.make();
    protected long startTime;
    protected final Dimension dimension;
    protected final FrameEncoder frameEncoder;


    public H264StreamEncoder(Dimension dimension, boolean usingInternalFrameEncoder) {
        super();
        this.dimension = dimension;
        if (usingInternalFrameEncoder) {
            frameEncoder = new FrameEncoder(4);
        } else {
            frameEncoder = null;
        }
        initialize();
    }

    private void initialize() {
        // todo: fps can change!!!
        iStreamCoder.setNumPicturesInGroupOfPictures(25);

        // todo: settings in gui
        iStreamCoder.setBitRate(200000);
        iStreamCoder.setBitRateTolerance(10000);
        iStreamCoder.setPixelType(Type.YUV420P);
        iStreamCoder.setHeight(dimension.height);
        iStreamCoder.setWidth(dimension.width);
        iStreamCoder.setFlag(IStreamCoder.Flags.FLAG_QSCALE, true);
        iStreamCoder.setGlobalQuality(0);
        // rate
        // todo: fps can change!!!
        IRational rate = IRational.make(25, 1);
        iStreamCoder.setFrameRate(rate);
        // time base
        //iStreamCoder.setAutomaticallyStampPacketsForStream(true);
        iStreamCoder.setTimeBase(IRational.make(rate.getDenominator(), rate.getNumerator()));
        IMetaData codecOptions = IMetaData.make();
        codecOptions.setValue("tune", "zerolatency"); // equals -tune zerolatency in ffmpeg
        //open it
        int revl = iStreamCoder.open(codecOptions, null);
        if (revl < 0) {
            throw new RuntimeException("could not open the coder");
        }
    }


    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel,
                            Object msg) throws Exception {
        return encode(msg);
    }

    public Object encode(Object msg) throws Exception {
        if (msg == null) {
            return null;
        }
        if (!(msg instanceof BufferedImage)) {
            throw new IllegalArgumentException("your need to pass into an bufferedimage");
        }
        logger.info("encode the frame");
        BufferedImage bufferedImage = (BufferedImage) msg;
        //here is the encode
        //convert the image
        BufferedImage convertedImage = ImageUtils.convertToType(bufferedImage, BufferedImage.TYPE_3BYTE_BGR);
        IConverter converter = ConverterFactory.createConverter(convertedImage, Type.YUV420P);
        //to frame
        long now = System.currentTimeMillis();
        if (startTime == 0) {
            startTime = now;
        }
        IVideoPicture pFrame = converter.toPicture(convertedImage, (now - startTime) * 1000);
        //pFrame.setQuality(0);
        iStreamCoder.encodeVideo(iPacket, pFrame, 0);
        //free the MEM
        pFrame.delete();
        converter.delete();
        //write to the container
        if (iPacket.isComplete()) {
            //iPacket.delete();
            //here we send the package to the remote peer
            try {
                ByteBuffer byteBuffer = iPacket.getByteBuffer();
                if (iPacket.isKeyPacket()) {
                    logger.info("key frame");
                }
                ChannelBuffer channelBuffe = ChannelBuffers.copiedBuffer(byteBuffer.order(ByteOrder.BIG_ENDIAN));
                if (frameEncoder != null) {
                    return frameEncoder.encode(channelBuffe);
                }
                return channelBuffe;
            } finally {
                iPacket.reset();
            }
        } else {
            return null;
        }
    }

}
