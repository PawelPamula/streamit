package streamit.ui.server;


import com.github.sarxos.webcam.WebcamPanel;
import javax.swing.*;

/**
 * JFrame window for displaying server application.
 */
public class StreamServerWindow extends JFrame {

    private WebcamPanel webcamPanel;
    private StreamServerWindowContent windowContent;

    public StreamServerWindow() {
        super();

        windowContent = new StreamServerWindowContent();
        setContentPane(windowContent.mainContentPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public void setWebCamPanel(WebcamPanel panel) {
        webcamPanel = panel;
        windowContent.addVideoPanel(webcamPanel);
    }

}
