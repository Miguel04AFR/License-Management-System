package visual_utils;


import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private Image image;

    // Constructor que recibe un ImageIcon
    public ImagePanel(ImageIcon icon) {
        if (icon != null) {
            this.image = icon.getImage();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            // Dibuja la imagen escalada al tamaño del panel
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
