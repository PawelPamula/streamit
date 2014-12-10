package streamit.ui.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import streamit.ui.server.StreamServer;
import streamit.handler.StreamFrameListener;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.InetSocketAddress;

public class StreamClient {
    protected final static Logger logger = LoggerFactory.getLogger(StreamClient.class);
	private final static Dimension startDimension = new Dimension(320,240);
	private static StreamClientWindow displayWindow;
        
	public static void main(String[] args) {
		displayWindow = new StreamClientWindow();
		displayWindow.setVisible(true);

        // todo: add in agent changing dimensions OR create agent after connecting and getting resolution
		StreamClientAgent clientAgent = new StreamClientAgent(new StreamFrameListenerIMPL() ,startDimension);
		clientAgent.connect(new InetSocketAddress(StreamServer.HOSTNAME, StreamServer.PORT));
	}

	protected static class StreamFrameListenerIMPL implements StreamFrameListener {
		private volatile long count = 0;
		@Override
		public void onFrameReceived(BufferedImage image) {
			displayWindow.updateImage(image);			
		}
	}
}
