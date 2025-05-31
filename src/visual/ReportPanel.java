package visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class ReportPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public ReportPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(UIManager.getColor("Panel.background"));

        // Title
        JLabel titleLabel = new JLabel("Reports");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(UIManager.getColor("Label.foreground"));

        // Report cards panel
        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new GridLayout(0, 2, 20, 20));
        cardsPanel.setBackground(getBackground());

        // Center Information report card with action
        cardsPanel.add(createCenterInfoCard());

        // Driver Information report card with action
        cardsPanel.add(createDriverInfoCard());

        // Other report cards
        cardsPanel.add(createReportCard(
                "Entity Information",
                "Shows details about a driving school or clinic.",
                "entity_icon.png"
        ));
        cardsPanel.add(createReportCard(
                "Licenses Issued in Period",
                "List of licenses issued in a selected period.",
                "license_icon.png"
        ));
        cardsPanel.add(createReportCard(
                "Exams in Period",
                "List of exams taken in a selected period.",
                "exam_icon.png"
        ));
        cardsPanel.add(createReportCard(
                "Infractions in Period",
                "Registered infractions in a selected period.",
                "violation_icon.png"
        ));
        cardsPanel.add(createReportCard(
                "Consolidated Infractions by Type/Year",
                "Summary of infractions by type for a given year.",
                "stats_icon.png"
        ));
        cardsPanel.add(createReportCard(
                "Drivers with Expired Licenses",
                "Drivers whose licenses have expired in a period.",
                "alert_icon.png"
        ));

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(getBackground());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createCenterInfoCard() {
        // Colors according to look and feel
        Color bg = UIManager.getColor("Panel.background");
        Color fg = UIManager.getColor("Label.foreground");
        Color border = UIManager.getColor("Separator.foreground");
        if (border == null) border = fg;

        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(border, 1, true),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        card.setBackground(bg);

        // Icon
        JLabel iconLabel;
        try {
            iconLabel = new JLabel(new ImageIcon(getClass().getResource("/icons/center_icon.png")));
        } catch (Exception e) {
            iconLabel = new JLabel(UIManager.getIcon("OptionPane.informationIcon"));
        }
        iconLabel.setPreferredSize(new Dimension(48, 48));

        // Title
        JLabel titleLabel = new JLabel("Center Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(fg);

        // Description
        JLabel descLabel = new JLabel("<html><div style='width:200px;'>Shows the center's details, contact info, and staff.</div></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(fg);

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);
        topPanel.add(iconLabel, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(descLabel);

        topPanel.add(textPanel, BorderLayout.CENTER);

        card.add(topPanel, BorderLayout.CENTER);

        // Show report button
        JButton showButton = new JButton("Show report");
        showButton.addActionListener(e -> showCenterInfoDialog());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(showButton);

        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private void showCenterInfoDialog() {
        // Example data, replace with real data as needed
        String[][] info = {
                {"Center name:", "Example Center"},
                {"Postal address:", "123 Main St, City, Country"},
                {"Phone:", "+1 555-123456"},
                {"Email:", "info@example.com"},
                {"General director:", "John Doe"},
                {"HR chief:", "Jane Smith"},
                {"Accounting chief:", "Mary Johnson"},
                {"Union secretary:", "Robert Brown"}
        };

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Center Information", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout(16, 16));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        panel.setBackground(UIManager.getColor("Panel.background"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // Center name, address, and contact info
        for (String[] kv : info) {
            addRow(panel, kv[0], kv[1], gbc, y++);
        }

        // Logo (if available)
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        panel.add(new JLabel("Logo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        JLabel logo = new JLabel();
        logo.setPreferredSize(new Dimension(100, 100));
        logo.setBorder(BorderFactory.createEtchedBorder());
        try {
            logo.setIcon(new ImageIcon(getClass().getResource("/icons/center_logo.png")));
        } catch (Exception e) {
            // No icon
        }
        panel.add(logo, gbc);

        dialog.add(panel, BorderLayout.CENTER);

        // Export button with TXT/CSV export (no dependencies)
        JButton exportButton = new JButton("Export...");
        exportButton.addActionListener((ActionEvent e) -> exportCenterInfoSimple(info));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(exportButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Simple export: CSV or TXT (no dependencies)
    private void exportCenterInfoSimple(String[][] info) {
        String[] options = {"CSV (.csv)", "Text (.txt)"};
        int choice = JOptionPane.showOptionDialog(this,
                "Select the format to export:",
                "Export",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if (choice == -1) return;

        JFileChooser chooser = new JFileChooser();
        if (choice == 0)
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));
        else
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text files", "txt"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();
        String ext = choice == 0 ? ".csv" : ".txt";
        if (!file.getName().toLowerCase().endsWith(ext)) {
            file = new File(file.getAbsolutePath() + ext);
        }

        try (PrintWriter pw = new PrintWriter(file, "UTF-8")) {
            if (choice == 0) { // CSV
                for (String[] kv : info) {
                    pw.println("\"" + kv[0].replace("\"", "\"\"") + "\"," +
                               "\"" + kv[1].replace("\"", "\"\"") + "\"");
                }
            } else { // TXT
                for (String[] kv : info) {
                    pw.println(kv[0] + " " + kv[1]);
                }
            }
            JOptionPane.showMessageDialog(this, "Report exported successfully:\n" + file.getAbsolutePath());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addRow(JPanel panel, String label, String value, GridBagConstraints gbc, int y) {
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(new JLabel(value), gbc);
    }

    private JPanel createReportCard(String title, String description, String iconPath) {
        Color bg = UIManager.getColor("Panel.background");
        Color fg = UIManager.getColor("Label.foreground");
        Color border = UIManager.getColor("Separator.foreground");
        if (border == null) border = fg;

        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(border, 1, true),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        card.setBackground(bg);

        JLabel iconLabel;
        try {
            iconLabel = new JLabel(new ImageIcon(getClass().getResource("/icons/" + iconPath)));
        } catch (Exception e) {
            iconLabel = new JLabel(UIManager.getIcon("OptionPane.informationIcon"));
        }
        iconLabel.setPreferredSize(new Dimension(48, 48));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(fg);

        JLabel descLabel = new JLabel("<html><div style='width:200px;'>" + description + "</div></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(fg);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);
        topPanel.add(iconLabel, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(descLabel);

        topPanel.add(textPanel, BorderLayout.CENTER);

        card.add(topPanel, BorderLayout.CENTER);

        return card;
    }

    // NUEVO: Driver Information card con botón "Show report"
    private JPanel createDriverInfoCard() {
        Color bg = UIManager.getColor("Panel.background");
        Color fg = UIManager.getColor("Label.foreground");
        Color border = UIManager.getColor("Separator.foreground");
        if (border == null) border = fg;

        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(border, 1, true),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        card.setBackground(bg);

        JLabel iconLabel;
        try {
            iconLabel = new JLabel(new ImageIcon(getClass().getResource("/icons/driver_icon.png")));
        } catch (Exception e) {
            iconLabel = new JLabel(UIManager.getIcon("OptionPane.informationIcon"));
        }
        iconLabel.setPreferredSize(new Dimension(48, 48));

        JLabel titleLabel = new JLabel("Driver Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(fg);

        JLabel descLabel = new JLabel("<html><div style='width:200px;'>Shows all data and infractions for a specific driver.</div></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(fg);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);
        topPanel.add(iconLabel, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(descLabel);

        topPanel.add(textPanel, BorderLayout.CENTER);

        card.add(topPanel, BorderLayout.CENTER);

        // Show report button
        JButton showButton = new JButton("Show report");
        showButton.addActionListener(e -> showDriverInfoDialog());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(showButton);

        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    // Aquí se invoca el panel/modal de DriverInfoPanel
    private void showDriverInfoDialog() {
        // Instancia el panel y muestra en un diálogo modal
        DriverInfoPanel driverPanel = new DriverInfoPanel();
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Driver Information", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(driverPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}