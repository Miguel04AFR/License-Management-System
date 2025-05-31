package visual;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Splash animation: a car waits at a red traffic light, then moves when the light turns green,
 * and finally launches the main app window.
 *
 * Requires FlatLaf (https://www.formdev.com/flatlaf/)
 */
public class TrafficLightCarAnimation extends JFrame {
	private static final long serialVersionUID = 1L;
    public TrafficLightCarAnimation() {
        setTitle("Traffic Light & Car Animation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 340);
        setLocationRelativeTo(null);
        setResizable(false);
        setContentPane(new AnimationPanel(this));
        setVisible(true);
    }

    // Animation Panel
    static class AnimationPanel extends JPanel {
    	private static final long serialVersionUID = 1L;
        private final JFrame parent;
        private int carX = 50;
        private boolean isGreen = false;
        private int ticks = 0;
        private final int carY = 195;
        private final int trafficLightX = 480;
        private final int trafficLightY = 90;
        private final Timer timer;

        public AnimationPanel(JFrame parent) {
            this.parent = parent;
            setBackground(new Color(230, 243, 255));
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
            if (ticks == 50) { // after ~2 seconds, turn green
                isGreen = true;
                repaint();
            }
            if (isGreen && carX < 650) {
                carX += 10; // move the car smoothly
                repaint();
            }
            // When car leaves the screen, launch the main app
            if (carX >= 650) {
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

        	    // Animated clouds
        	    int cloudOffset = (ticks / 2) % getWidth();
        	    drawCloud(g2d, 100 + cloudOffset, 60);
        	    drawCloud(g2d, 300 + cloudOffset, 40);
        	    drawCloud(g2d, 500 + cloudOffset, 70);
            // Road
            g2d.setColor(new Color(110, 110, 110));
            g2d.fillRect(0, 210, getWidth(), 60);

            // Road stripes
            g2d.setColor(Color.YELLOW);
            for (int i = 0; i < getWidth(); i += 40) {
                g2d.fillRect(i, 239, 20, 6);
            }

            // Traffic light pole
            g2d.setColor(new Color(70, 70, 70));
            g2d.fillRect(trafficLightX + 13, trafficLightY + 60, 4, 55);

            // Traffic light body
            g2d.setColor(new Color(30, 30, 30));
            g2d.fillRoundRect(trafficLightX, trafficLightY, 30, 65, 10, 10);

            // Red light
            g2d.setColor(!isGreen ? Color.RED : new Color(70, 0, 0));
            g2d.fillOval(trafficLightX + 7, trafficLightY + 10, 16, 16);

            // Green light
            g2d.setColor(isGreen ? Color.GREEN : new Color(0, 70, 0));
            g2d.fillOval(trafficLightX + 7, trafficLightY + 39, 16, 16);

            // Car body
            g2d.setColor(new Color(45, 120, 255));
            g2d.fillRoundRect(carX, carY, 90, 28, 15, 15);
            // Car windows
            g2d.setColor(new Color(200, 230, 255));
            g2d.fillRoundRect(carX + 15, carY + 4, 40, 14, 7, 7);
            // Car roof
            g2d.setColor(new Color(30, 100, 220));
            g2d.fillRoundRect(carX + 10, carY - 4, 60, 12, 10, 10);
            // Car wheels
            g2d.setColor(Color.BLACK);
            g2d.fillOval(carX + 12, carY + 18, 20, 20);
            g2d.fillOval(carX + 58, carY + 18, 20, 20);

            // Headlights (when green, as if car is starting)
            if (isGreen) {
                g2d.setColor(new Color(255, 255, 120, 140));
                g2d.fillArc(carX + 86, carY + 7, 40, 16, -10, 30);
            }

            // Waiting message
            if (!isGreen && carX == 50) {
                g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
                g2d.setColor(new Color(50, 50, 50));
                g2d.drawString("Waiting for green light...", 220, 60);
            }
        }
        private void drawCloud(Graphics2D g2d, int x, int y) {
            g2d.setColor(new Color(255, 255, 255, 220));
            g2d.fillOval(x, y, 50, 30);
            g2d.fillOval(x + 20, y - 10, 50, 40);
            g2d.fillOval(x + 40, y, 50, 30);
        }
    }

   

   
}