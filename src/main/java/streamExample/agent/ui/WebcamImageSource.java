package streamExample.agent.ui;

import com.github.sarxos.webcam.Webcam;
import streamExample.agent.ImageSource;

import java.awt.image.BufferedImage;

public class WebcamImageSource implements ImageSource {
    private final Webcam webcam;

    public WebcamImageSource(Webcam webcam) {
        this.webcam = webcam;
    }

    @Override
    public BufferedImage getImage() {
        return webcam.getImage();
    }
}
