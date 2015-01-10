package streamExample.agent.ui.client;


import streamExample.agent.ui.components.VideoPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class StreamClientWindow extends JFrame {

    private VideoPanel videoPanel;
    private StreamClientWindowContent windowContent;
    private JMenu connectionMenu;

    /**
     * JFrame window for displaying client application.
     */
    public StreamClientWindow()
    {
        super();

        windowContent = new StreamClientWindowContent();
        videoPanel = new VideoPanel();
        setContentPane(windowContent.mainContentPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        windowContent.addVideoPanel(videoPanel);
        generateConnectionMenu();
        pack();
        setVisible(true);
    }

    public void updateImage(BufferedImage image) {
        videoPanel.updateImage(image);
    }

    private void generateConnectionMenu() {
        connectionMenu = new JMenu("Połączenie");

        JMenuItem connectMenuItem = new JMenuItem("Połącz");
        JMenuItem disconnectMenuItem = new JMenuItem("Rozłącz");
        JMenuItem editConnectionMenuItem = new JMenuItem("Edytuj Połączenie");

        editConnectionMenuItem.addActionListener(new EditConnectionActionListener(this));


        connectionMenu.add(connectMenuItem);
        connectionMenu.add(disconnectMenuItem);
        connectionMenu.addSeparator();
        connectionMenu.add(editConnectionMenuItem);

        windowContent.menuBar.add(connectionMenu);
    }
}

class EditConnectionActionListener implements ActionListener {
    private StreamClientWindow parentWindow;

    public EditConnectionActionListener(StreamClientWindow streamClientWindow) {
        parentWindow = streamClientWindow;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        showDialog();
    }

    // should return host and port values
    private void showDialog() {
        StreamClientEditConnectionDialog dialog = new StreamClientEditConnectionDialog(parentWindow);
        dialog.setLocationRelativeTo(parentWindow);
        dialog.setModal(true);
        dialog.pack();
        dialog.setVisible(true);
    }
}
