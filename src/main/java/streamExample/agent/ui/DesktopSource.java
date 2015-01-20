package streamExample.agent.ui;

import streamExample.agent.ImageSource;
import streamExample.agent.ui.server.SelectionRectangle;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DesktopSource implements ImageSource {
    private Robot robot;
    private SelectionRectangle selection;
    private Dimension selectionSize;

    public DesktopSource(Dimension dimension){
        try {
            robot = new Robot();
            selectionSize = dimension;
            selection = new SelectionRectangle(selectionSize);
            selection.setVisible(true);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BufferedImage getImage() {
        return robot.createScreenCapture(new Rectangle(selection.getX(), selection.getY(), selection.getWidth(), selection.getHeight()));
    }

    public Dimension getSelectionRectangleSize() {
        return selection.getSize();
    }
}
