package streamExample.agent.ui;

import com.github.sarxos.webcam.WebcamPanel;
import streamExample.agent.ui.components.StreamServerInfoPanel;
import java.awt.Dimension;

/**
 * Display window for stream server.
 */
public class StreamServerDisplayWindow extends StreamDisplayWindow {

    protected final StreamServerInfoPanel menuPanel;
    protected WebcamPanel webcamPanel;
    
    public StreamServerDisplayWindow(String name, Dimension dimension) {
        super(name, dimension);
        menuPanel = new StreamServerInfoPanel();
    }
    
    public void setWebCamPanel(WebcamPanel panel) { 
        this.webcamPanel = panel;
        this.window.addVideoPanel(webcamPanel);
    }
}
