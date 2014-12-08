package streamExample.agent.ui;

import streamExample.agent.ui.components.StreamClientConnectionMenu;
import streamExample.agent.ui.components.StreamClientInfoPanel;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 * Display window for stream client.
 */
public class StreamClientDisplayWindow extends StreamDisplayWindow {

    protected final StreamClientInfoPanel infoPanel;
    protected final StreamClientConnectionMenu connectionMenu;
    protected final VideoPanel videoPannel;
    
    public StreamClientDisplayWindow(String name, Dimension dimension) {
        super(name, dimension);
        this.infoPanel = new StreamClientInfoPanel();
        this.connectionMenu = new StreamClientConnectionMenu();
        this.videoPannel = new VideoPanel();

        this.videoPannel.setPreferredSize(dimension);
        this.window.addVideoPanel(videoPannel);
        this.window.addMenu(connectionMenu);
        this.window.addInfoPanel(infoPanel);
    }

    public void updateImage(BufferedImage image) {
        videoPannel.updateImage(image);
    }

    public void close() {
        window.dispose();
        videoPannel.close();
    }
}
