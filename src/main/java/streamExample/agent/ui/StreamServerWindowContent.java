package streamExample.agent.ui;

import com.github.sarxos.webcam.WebcamPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Code behind file for server window content.
 */
public class StreamServerWindowContent {
    protected JPanel mainContentPanel;
    protected JMenuBar menuBar;
    protected JPanel videoContentPanel;

    protected void addVideoPanel(WebcamPanel panel) {
        videoContentPanel.removeAll();
        videoContentPanel.setLayout(new GridLayout(1,1));
        videoContentPanel.add(panel);
    }
}
