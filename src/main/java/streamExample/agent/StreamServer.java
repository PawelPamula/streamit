package streamExample.agent;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.net.InetSocketAddress;


public class StreamServer {
    protected final static Logger logger = LoggerFactory.getLogger(StreamServer.class);
    public static String HOSTNAME = "localhost";
    public static int PORT = 20001;

	public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        Webcam.setAutoOpenMode(true);
        logger.debug("Finding webcam");
		Webcam webcam = Webcam.getDefault();
        logger.debug("Webcam found");
//        Dimension dimension = webcam.getViewSize();
        Dimension dimension = new Dimension(320, 240);
        JFrame window = new JFrame("Streamit Server");
        window.setResizable(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        if (webcam == null) {
            JOptionPane.showMessageDialog(window, "No webcam detected", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } else {
            webcam.setViewSize(dimension);

            WebcamPanel panel = new WebcamPanel(webcam);
            panel.setFPSDisplayed(true);
            panel.setDisplayDebugInfo(true);
            panel.setImageSizeDisplayed(true);

            window.add(panel);
            window.pack();
            window.setVisible(true);

            StreamServerAgent serverAgent = new StreamServerAgent(webcam, dimension);
            serverAgent.start(new InetSocketAddress(HOSTNAME, PORT));
        }
	}

}
