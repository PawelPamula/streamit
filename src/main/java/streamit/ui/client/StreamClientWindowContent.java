package streamit.ui.client;

import streamit.ui.components.VideoPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Code behind file for client window content.
 */
public class StreamClientWindowContent {


    protected JLabel hostLabel;
    protected JLabel portLabel;
    protected JLabel statusLabel;
    protected JMenuBar menuBar;
    protected JPanel videoContentPanel;
    protected JPanel mainContentPanel;

    protected void addVideoPanel(VideoPanel panel) {
        videoContentPanel.removeAll();
        videoContentPanel.setLayout(new GridLayout(1,1));
        videoContentPanel.add(panel);
    }
}
