package lab_6;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JImageDisplay extends JComponent {

    private BufferedImage image;

    public JImageDisplay(int w, int h)
    {
        this.image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        this.setPreferredSize(new Dimension(w, h));
        this.setMinimumSize(new Dimension(w, h));
    }

    public BufferedImage getImage()
    {
        return image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.image, 0, 0, null);
    }
}
