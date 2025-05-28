package visual;

import javax.swing.*;
import java.awt.*;

public class ReportPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public ReportPanel() {
		setLayout(new BorderLayout(20, 20));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		setBackground(UIManager.getColor("Panel.background"));

		// Título
		JLabel titleLabel = new JLabel("Reports");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setForeground(UIManager.getColor("Label.foreground"));

		// Panel de tarjetas de reporte
		JPanel cardsPanel = new JPanel();
		cardsPanel.setLayout(new GridLayout(0, 2, 20, 20));
		cardsPanel.setBackground(getBackground());

		cardsPanel.add(createReportCard(
				"Center Information",
				"Shows the center's details, contact info and staff.",
				"center_icon.png"
				));
		cardsPanel.add(createReportCard(
				"Driver Information",
				"Shows all data and infractions for a specific driver.",
				"driver_icon.png"
				));
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

	private JPanel createReportCard(String title, String description, String iconPath) {
		// Colores según el look and feel
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

		// Icono
		JLabel iconLabel;
		try {
			iconLabel = new JLabel(new ImageIcon(getClass().getResource("/icons/" + iconPath)));
		} catch (Exception e) {
			iconLabel = new JLabel(UIManager.getIcon("OptionPane.informationIcon"));
		}
		iconLabel.setPreferredSize(new Dimension(48, 48));

		// Título
		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		titleLabel.setForeground(fg);

		// Descripción
		JLabel descLabel = new JLabel("<html><div style='width:200px;'>" + description + "</div></html>");
		descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		descLabel.setForeground(fg);

		// Exportar
		JButton exportButton = new JButton("Export...");
		exportButton.setIcon(UIManager.getIcon("FileView.floppyDriveIcon"));
		exportButton.setFocusable(false);

		// LAYOUT
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

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		buttonPanel.setOpaque(false);
		buttonPanel.add(exportButton);

		card.add(buttonPanel, BorderLayout.SOUTH);

		return card;
	}
}