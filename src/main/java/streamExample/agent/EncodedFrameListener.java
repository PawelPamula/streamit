package streamExample.agent;

import streamExample.handler.StreamFrameListener;

import java.awt.image.BufferedImage;

public interface EncodedFrameListener {
    public void receiveEncodedFrame(Object frame);
}
