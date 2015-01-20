package streamExample.agent.ui.server;

import javax.swing.*;
import java.awt.*;

public class SelectionRectangle extends JFrame {
        private DrawPanel drawPanel;

        public SelectionRectangle(Dimension selectionSize) {
                this.setSize(selectionSize);
                this.setUndecorated(true);
                this.setOpacity(0.25F);
                this.setBackground(new Color(0, 0, 0, 0.25F));
                //this.setAlwaysOnTop(true);
                // this.setAutoRequestFocus(false);
                // this.setResizable(true); TODO enable this option
                this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);

                drawPanel = new DrawPanel();
                this.add(drawPanel);
        }

        private class DrawPanel extends JPanel {
                @Override
                public void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.drawRect(0, 0, this.getWidth(), this.getHeight());
                }
        }
}