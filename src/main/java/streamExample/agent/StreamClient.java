package streamExample.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import streamExample.agent.ui.client.StreamClientWindow;
import streamExample.handler.StreamFrameListener;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.InetSocketAddress;

public class StreamClient implements StreamClientAgent.OnConnectListener {
    protected final static Logger logger = LoggerFactory.getLogger(StreamClient.class);
	private final Dimension startDimension = new Dimension(320,240);
	private StreamClientWindow displayWindow;
	private StreamServer serverInstance;
        
	public void oldMain(String[] args) {
		displayWindow = new StreamClientWindow();
		displayWindow.setVisible(true);
		connectToServer();
	}

	public static void main(String[] args) {
		new StreamClient().oldMain(args);
	}

	private void connectToServer() {
		StreamClientAgent clientAgent = new StreamClientAgent(new StreamFrameListenerIMPL() ,startDimension, this);
		clientAgent.connect(new InetSocketAddress(StreamServer.HOSTNAME, StreamServer.PORT));
	}

	@Override
	public void onConnected() {
		serverInstance = new StreamServer();
	}

	protected class StreamFrameListenerIMPL implements StreamFrameListener {
		private volatile long count = 0;
		@Override
		public void onFrameReceived(BufferedImage image) {
			displayWindow.updateImage(image);			
		}

		@Override
		public void onMsgReceived(Object object) {

		}
	}
}
