package visual.Buttons;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton {
    private static final long serialVersionUID = 1L;
    
    private Color normalBackgroundColor;
    private Color hoverBackgroundColor;
    private Color pressedBackgroundColor;

    public ModernButton(String text) {
        super(text);
        // Define colores (puedes personalizarlos)
        normalBackgroundColor = new Color(66, 133, 244); // Azul
        hoverBackgroundColor = normalBackgroundColor.brighter();
        pressedBackgroundColor = normalBackgroundColor.darker();
        
        // Configuración inicial
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorder(new EmptyBorder(10, 20, 10, 20));
        setForeground(Color.WHITE);
        setFont(getFont().deriveFont(Font.BOLD));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBackground(normalBackgroundColor);
        setOpaque(false);  // Para permitir la transparencia en el dibujo
        
        // Agrega listeners para efectos al pasar el ratón y presionar
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverBackgroundColor);
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalBackgroundColor);
                repaint();
            }
            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedBackgroundColor);
                repaint();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverBackgroundColor);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Creamos un Graphics2D para un renderizado más suave
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dibuja el fondo con esquinas redondeadas
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        
        g2.dispose();
        // Finalmente pinta el contenido (texto, iconos, etc.)
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Dibuja un borde sutil redondeado
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground().darker());
        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
        g2.dispose();
    }
}
