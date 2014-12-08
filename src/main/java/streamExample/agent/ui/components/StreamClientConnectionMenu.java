package streamExample.agent.ui.components;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * Connection menu for client. 
 */
public class StreamClientConnectionMenu extends JMenu {

    public StreamClientConnectionMenu() {
        super("Połączenie");
        createMenu();
    }
    
    private void createMenu()
    {
        JMenuItem connectItem = new JMenuItem("Połącz");
        JMenuItem disconnectItem = new JMenuItem("Rozłącz");
        JMenuItem editConntectionItem = new JMenuItem("Edytuj połączenie");
        this.add(connectItem);
        this.add(disconnectItem);
        this.addSeparator();
        this.add(editConntectionItem);
        
    }
    
    // TODO: add action controls here
    
}
