package visual;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Splash animation: logo in the sky, a car waits at a red traffic light, then moves when the light turns green,
 * and finally launches the main app window.
 *
 * Requires FlatLaf (https://www.formdev.com/flatlaf/)
 */
public class TrafficLightCarAnimation extends JFrame {
    private static final long serialVersionUID = 1L;

    public TrafficLightCarAnimation() {
        setTitle("Traffic Light & Car Animation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720); // New window size
        setLocationRelativeTo(null);
        setResizable(false);
        setContentPane(new AnimationPanel(this));
        setVisible(true);
    }

    // Animation Panel
    static class AnimationPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private final JFrame parent;
        private int carX = 120;
        private boolean isGreen = false;
        private int ticks = 0;
        private final int carY = 480; // Road is lower
        private final int trafficLightX = 1050; // Move to right edge
        private final int trafficLightY = 300;  // Lower to match new road
        private final Timer timer;
        private Image logoImage;

        public AnimationPanel(JFrame parent) {
            this.parent = parent;
            setBackground(new Color(230, 243, 255));

            // Try load the logo (adjust the path as needed)
            logoImage = null;
            try {
                logoImage = new ImageIcon(getClass().getResource("/visual/logo_en.png")).getImage();
            } catch (Exception e) {
                // fallback: logo not found
            }

            timer = new Timer(40, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onTick();
                }
            });
            timer.start();
        }

        private void onTick() {
            ticks++;
            if (ticks == 70) { // after ~2.8 seconds, turn green
                isGreen = true;
                repaint();
            }
            if (isGreen && carX < 1280) {
                carX += 18; // move the car smoothly
                repaint();
            }
            // When car leaves the screen, launch the main app
            if (carX >= 1280) {
                timer.stop();
                SwingUtilities.invokeLater(() -> {
                    parent.dispose();
                    EventQueue.invokeLater(() -> {
                        new LicenseManagementUI().setVisible(true);
                    });
                });
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Gradient sky
            GradientPaint sky = new GradientPaint(0, 0, new Color(135, 206, 250), 0, getHeight(), new Color(230, 243, 255));
            g2d.setPaint(sky);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Logo: centered horizontally, high up (y = 25), larger size for 1280x720
            int logoW = 410;
            int logoH = 410;
            int logoX = (getWidth() - logoW) / 2;
            int logoY = 25;
            if (logoImage != null) {
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.drawImage(logoImage, logoX, logoY, logoW, logoH, this);
            }

            // Animated clouds, avoid overlapping the logo (draw beside and below)
            int cloudOffset = (ticks / 2) % getWidth();
            // Left of logo
            drawCloud(g2d, logoX - 180 + cloudOffset / 2, logoY + 85, 110, 65);
            // Right of logo
            drawCloud(g2d, logoX + logoW + 40 + (cloudOffset / 3), logoY + 100, 110, 65);
            // Lower clouds (well below logo)
            drawCloud(g2d, 320 + cloudOffset, logoY + logoH + 30, 130, 80);
            drawCloud(g2d, 800 + cloudOffset, logoY + logoH + 20, 130, 85);

            // Road
            g2d.setColor(new Color(110, 110, 110));
            g2d.fillRect(0, 510, getWidth(), 120);

            // Road stripes
            g2d.setColor(Color.YELLOW);
            for (int i = 0; i < getWidth(); i += 65) {
                g2d.fillRect(i, 570, 40, 11);
            }

            // Traffic light pole
            g2d.setColor(new Color(70, 70, 70));
            g2d.fillRect(trafficLightX + 21, trafficLightY + 105, 10, 140);

            // Traffic light body
            g2d.setColor(new Color(30, 30, 30));
            g2d.fillRoundRect(trafficLightX, trafficLightY, 60, 160, 24, 24);

            // Red light
            g2d.setColor(!isGreen ? Color.RED : new Color(70, 0, 0));
            g2d.fillOval(trafficLightX + 18, trafficLightY + 32, 30, 30);

            // Green light
            g2d.setColor(isGreen ? Color.GREEN : new Color(0, 70, 0));
            g2d.fillOval(trafficLightX + 18, trafficLightY + 98, 30, 30);

            // Car body
            g2d.setColor(new Color(45, 120, 255));
            g2d.fillRoundRect(carX, carY, 180, 55, 30, 30);
            // Car windows
            g2d.setColor(new Color(200, 230, 255));
            g2d.fillRoundRect(carX + 27, carY + 10, 90, 28, 15, 15);
            // Car roof
            g2d.setColor(new Color(30, 100, 220));
            g2d.fillRoundRect(carX + 22, carY - 9, 120, 25, 22, 22);
            // Car wheels
            g2d.setColor(Color.BLACK);
            g2d.fillOval(carX + 24, carY + 37, 40, 40);
            g2d.fillOval(carX + 116, carY + 37, 40, 40);

            // Headlights (when green, as if car is starting)
            if (isGreen) {
                g2d.setColor(new Color(255, 255, 120, 140));
                g2d.fillArc(carX + 172, carY + 15, 68, 28, -10, 30);
            }
        }

        private void drawCloud(Graphics2D g2d, int x, int y, int w, int h) {
            g2d.setColor(new Color(255, 255, 255, 220));
            g2d.fillOval(x, y, w, h);
            g2d.fillOval(x + (w / 3), y - (h / 3), w, h + 10);
            g2d.fillOval(x + (2 * w / 3), y, w, h);
        }
    }
} 