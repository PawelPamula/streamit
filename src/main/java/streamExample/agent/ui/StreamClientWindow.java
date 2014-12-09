package streamExample.agent.ui;


import javax.swing.*;
import java.awt.image.BufferedImage;

public class StreamClientWindow extends JFrame {

    private VideoPanel videoPanel;
    private StreamClientWindowContent windowContent;


    /**
     * JFrame window for displaying client application.
     */
    public StreamClientWindow()
    {
        super();

        windowContent = new StreamClientWindowContent();
        videoPanel = new VideoPanel();
        setContentPane(windowContent.mainContentPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        windowContent.addVideoPanel(videoPanel);
    }

    public void updateImage(BufferedImage image) {
        videoPanel.updateImage(image);
    }

}
