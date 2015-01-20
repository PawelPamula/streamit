package streamExample.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import streamExample.agent.ui.client.StreamClientWindow;
import streamExample.handler.StreamFrameListener;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.InetSocketAddress;

public class StreamClient {
    protected final static Logger logger = LoggerFactory.getLogger(StreamClient.class);
    private final Dimension startDimension = new Dimension(320, 240);
    private StreamClientWindow displayWindow;
    //private static InetSocketAddress streamServerAddress;

    public void oldMain(String[] args) {
        displayWindow = new StreamClientWindow();
        displayWindow.setVisible(true);

        // todo: add in agent changing dimensions OR create agent after connecting and getting resolution
		StreamClientAgent clientAgent = new StreamClientAgent(new StreamFrameListenerIMPL(), startDimension);
		// streamServerAddress = clientAgent.getStreamServerAddress();
        // TO NIE JEST ADRES POD KTÓRY SIĘ ŁĄCZYMY!
		clientAgent.connect(new InetSocketAddress(StreamServer.HOSTNAME, StreamServer.PORT));

        //GUI update
        displayWindow.updateInfo(clientAgent.getClientPort());
	}

    public static void main(String[] args) {
        new StreamClient().oldMain(args);
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
