package streamExample.agent.ui;

import java.awt.*;

/**
 * Basic display window with elements common to both client and server. 
 */
public class StreamDisplayWindow {
    
    protected StreamDisplayFrame window;
    

    public StreamDisplayWindow(String name, Dimension dimension) {
        super();
        this.window = new StreamDisplayFrame(name);
    }
    
    public StreamDisplayFrame getDisplayFrame() {
        return window;
    }
    
    public void setVisible(boolean visible) {
        this.window.setVisible(visible);
    }
    
}
