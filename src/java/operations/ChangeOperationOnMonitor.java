package operations;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public  class ChangeOperationOnMonitor {


    public  JLabel showLaunch(int heightScreen) throws IOException {
        BufferedImage backgroundImage = ImageIO.read(new File("res/image/launch.jpg"));
        Image scaleBackground = backgroundImage.getScaledInstance(700, heightScreen / 2, Image.SCALE_SMOOTH);
        Icon imageIcon = new ImageIcon(scaleBackground);

        return new JLabel(imageIcon, 0);
    }
}
