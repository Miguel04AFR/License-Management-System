package visual;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import model.Center;
import services.CenterService;
import services.UserService;
import visual.Buttons.ModernButton;

public class Login extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
                Login frame = new Login();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Login() {
        FlatLightLaf.setup();
      

        setTitle("License Management System");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 950, 650);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBounds(0, 0, 934, 22);
        contentPane.add(menuBar);

        JMenu mnNewMenu = new JMenu("Settings");
        menuBar.add(mnNewMenu);

        JMenu mnNewMenu_1 = new JMenu("Theme");
        mnNewMenu.add(mnNewMenu_1);

        JMenuItem mntmNewMenuItem = new JMenuItem("Light");
        mntmNewMenuItem.addActionListener(e -> switchTheme(new FlatLightLaf()));
        mnNewMenu_1.add(mntmNewMenuItem);

        JMenuItem mntmNewMenuItem_1 = new JMenuItem("Dark");
        mntmNewMenuItem_1.addActionListener(e -> switchTheme(new FlatDarkLaf()));
        mnNewMenu_1.add(mntmNewMenuItem_1);

        JPanel panel = new JPanel();
        panel.setBounds(0, 22, 934, 588);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblUsuario = new JLabel("Username:");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblUsuario.setBounds(326, 301, 234, 30);
        panel.add(lblUsuario);

        JTextField txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBounds(326, 330, 258, 35);
        panel.add(txtUsuario);

        JLabel lblContrasena = new JLabel("Password:");
        lblContrasena.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblContrasena.setBounds(326, 402, 234, 30);
        panel.add(lblContrasena);

        JPasswordField pwdContrasena = new JPasswordField();
        pwdContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pwdContrasena.setBounds(326, 433, 271, 35);
        panel.add(pwdContrasena);

        JLabel lblTogglePass = new JLabel(new ImageIcon(Login.class.getResource("/visual/llave (1).png")));
        lblTogglePass.setBounds(594, 433, 44, 35);
        panel.add(lblTogglePass);

        ModernButton mdrnbtnAceptar = new ModernButton("Login");
        mdrnbtnAceptar.setBounds(326, 513, 271, 39);
        panel.add(mdrnbtnAceptar);

        lblTogglePass.addMouseListener(new MouseAdapter() {
            private boolean mostrar = false;
            public void mouseClicked(MouseEvent e) {
                mostrar = !mostrar;
                pwdContrasena.setEchoChar(mostrar ? (char) 0 : '•');
            }
        });

        mdrnbtnAceptar.addActionListener(e -> {
           
                UserService usuarios = new UserService();
                if(usuarios.autenticar(txtUsuario.getText(),(pwdContrasena.getText()))) {
                  String rol =usuarios.getRolPorUsuario(txtUsuario.getText());
                  LicenseManagementUI l = new LicenseManagementUI(rol);
                  l.setVisible(true);
                  dispose();
                  //enc.encripta
                }else {
                 javax.swing.JOptionPane.showMessageDialog(
                            null,
                            "Credenciales incorrectas.",
                            "Error de Autenticación",
                            javax.swing.JOptionPane.ERROR_MESSAGE
                        );
                    }
                
                
              
        });

        // Load logo from DB
        // Load logo from DB, centered
int logoWidth = 364;
int logoHeight = 277;
int panelWidth = 934;

int centeredX = (panelWidth - logoWidth) / 2;

JLabel logoLabel = new JLabel();
logoLabel.setBounds(centeredX, 11, logoWidth, logoHeight);
panel.add(logoLabel);
loadLogoFromDatabase(logoLabel);

    }

    private void loadLogoFromDatabase(JLabel logoLabel) {
        CenterService centerService = new CenterService();
        Center center = centerService.getAll().stream().findFirst().orElse(null);

        if (center != null && center.getLogo() != null) {
            try {
                ImageIcon icon = new ImageIcon(center.getLogo());
                Image scaled = icon.getImage().getScaledInstance(logoLabel.getWidth(), logoLabel.getHeight(), Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(scaled));
            } catch (Exception e) {
                System.err.println("Error loading logo: " + e.getMessage());
            }
        } else {
            System.err.println("No logo found in the database.");
        }
    }

    private void switchTheme(LookAndFeel laf) {
        try {
            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
} 