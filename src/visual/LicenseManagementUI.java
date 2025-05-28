package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class LicenseManagementUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private boolean darkMode = true;

	public LicenseManagementUI() {
		initializeUI();
	}

	private void initializeUI() {
		setupTheme();
		createMenu();
		createMainUI();
	}
	private void setupTheme() {
		try {
			if (darkMode) {
				UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
			} else {
				UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
			}
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	private void createMainUI() {
		setTitle("Driver License Management System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1280, 720);
		setLocationRelativeTo(null);

		JTabbedPane mainTabbedPane = new JTabbedPane();
		mainTabbedPane.addTab("Dashboard", createDashboardPanel());
		mainTabbedPane.addTab("Drivers", createDriversPanel());
		mainTabbedPane.addTab("Licenses", createLicensesPanel());
		mainTabbedPane.addTab("Exams", createExamsPanel());
		mainTabbedPane.addTab("Violations", createViolationsPanel());
		mainTabbedPane.addTab("Reports", createReportsPanel());
		mainTabbedPane.addTab("Alerts", createAlertsPanel());

		getContentPane().add(mainTabbedPane);
	}
	private void toggleTheme(ActionEvent e) {
		darkMode = e.getActionCommand().equals("Dark");
		setupTheme();
		updateUI();
	}
	private void updateUI() {
		SwingUtilities.updateComponentTreeUI(this);
		validate();
		repaint();
	}
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu settingsMenu = new JMenu("Settings");
		JMenu themeMenu = new JMenu("Theme");

		JMenuItem lightTheme = new JMenuItem("Light");
		JMenuItem darkTheme = new JMenuItem("Dark");

		lightTheme.addActionListener(this::toggleTheme);
		darkTheme.addActionListener(this::toggleTheme);

		themeMenu.add(lightTheme);
		themeMenu.add(darkTheme);
		settingsMenu.add(themeMenu);
		menuBar.add(settingsMenu);

		setJMenuBar(menuBar);
	}

	private JPanel createDashboardPanel() {
		JPanel dashboardPanel = new JPanel(new BorderLayout(10, 10));
		JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
		statsPanel.add(createStatCard("Active Licenses", "1,234"));
		statsPanel.add(createStatCard("Pending Renewals", "89"));
		statsPanel.add(createStatCard("Recent Violations", "45"));
		statsPanel.add(createStatCard("Scheduled Exams", "12"));
		JPanel chartPanel = new JPanel();
		chartPanel.setBackground(new Color(30, 30, 30));
		chartPanel.setPreferredSize(new Dimension(300, 400));
		dashboardPanel.add(statsPanel, BorderLayout.NORTH);
		dashboardPanel.add(chartPanel, BorderLayout.CENTER);
		return dashboardPanel;
	}

	private JPanel createDriversPanel() {
		return new DriverPanel();
	}





	private JPanel createLicensesPanel() {
		return new LicensePanel();
	}

	private JPanel createAlertsPanel() {
		JPanel alertsPanel = new JPanel(new BorderLayout());

		// Alerts list
		DefaultListModel<String> alertsModel = new DefaultListModel<>();
		alertsModel.addElement("[Urgent] License #1234 expires in 3 days");
		alertsModel.addElement("[Important] Pending medical exam for J. Smith");
		alertsModel.addElement("[Notification] 2 unpaid violations");

		JList<String> alertsList = new JList<>(alertsModel);
		alertsList.setCellRenderer(new AlertListRenderer());

		alertsPanel.add(new JScrollPane(alertsList), BorderLayout.CENTER);

		return alertsPanel;
	}

	private JComponent createStatCard(String title, String value) {
		JPanel statCard = new JPanel(new BorderLayout());
		statCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		statCard.setBackground(UIManager.getColor("Panel.background"));

		JLabel titleLabel = new JLabel(title);
		titleLabel.setForeground(UIManager.getColor("Label.foreground"));

		JLabel valueLabel = new JLabel(value);
		valueLabel.setFont(valueLabel.getFont().deriveFont(24.0f));
		valueLabel.setForeground(UIManager.getColor("Label.foreground"));

		statCard.add(titleLabel, BorderLayout.NORTH);
		statCard.add(valueLabel, BorderLayout.CENTER);

		return statCard;
	}



	private JPanel createExamsPanel() {
		return new ExamPanel();
	}

	private JPanel createViolationsPanel() {
		return new ViolationPanel();
	}

	private JPanel createReportsPanel() {

		return new ReportPanel();
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			new LicenseManagementUI().setVisible(true);
		});
	}
}