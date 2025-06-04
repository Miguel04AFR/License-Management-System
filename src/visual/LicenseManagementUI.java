package visual;


import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import visual.EntityPanels.AssociatedEntityPanel;
import visual.EntityPanels.DriverPanel;
import visual.EntityPanels.ExamPanel;
import visual.EntityPanels.LicensePanel;
import visual.EntityPanels.ViolationPanel;
import visual.Reports.ReportPanel;


public class LicenseManagementUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private boolean darkMode = false;

	public LicenseManagementUI() {
		initializeUI();
	}

	private void initializeUI() {
		setupTheme();
		createMenu();
		createMainUI();
		setupTheme();
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
		mainTabbedPane.addTab("Sumary", new DashboardPanel());
		mainTabbedPane.addTab("Drivers", new DriverPanel());
		mainTabbedPane.addTab("Licenses",  new LicensePanel());
		mainTabbedPane.addTab("Exams", new ExamPanel());
		mainTabbedPane.addTab("Violations", new ViolationPanel());
		mainTabbedPane.addTab("AssociatedEntity", new AssociatedEntityPanel());
		mainTabbedPane.addTab("Reports", new ReportPanel());	


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















	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			new LicenseManagementUI().setVisible(true);
		});
	}
}