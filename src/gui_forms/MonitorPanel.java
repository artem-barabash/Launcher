package gui_forms;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

class MonitorPanel extends JPanel {
    private Image image;

    public MonitorPanel() {
        super(new GridLayout());
        try {
            image = ImageIO.read(new URL("http://www.java2s.com/style/download.png"));
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        int w = image.getWidth(null) / 2;
        int h = image.getHeight(null) / 2;
        this.add(new JLabel(new ImageIcon(image.getScaledInstance(w, h,
                Image.SCALE_SMOOTH))));
    }
}
