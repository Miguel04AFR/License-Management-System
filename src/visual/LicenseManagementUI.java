package visual;


import java.awt.BorderLayout;
import java.awt.EventQueue;

import java.awt.event.ActionEvent;

import javax.swing.JDialog;
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
import visual.EntityPanels.UserPanel;
import visual.EntityPanels.ViolationPanel;
import visual.Reports.ReportPanel;


public class LicenseManagementUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private boolean darkMode = false;
	public static String rOl;

	public LicenseManagementUI(String rol) {
		setupTheme();
		initializeUI(rol);
	}

	private void initializeUI(String rol) {
		setupTheme();
		createMenu(rol);
		createMainUI(rol);
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
	private void createMainUI(String rol) {
		rOl=rol;
		setTitle("Driver License Management System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1280, 720);
		setLocationRelativeTo(null);

		JTabbedPane mainTabbedPane = new JTabbedPane();
		mainTabbedPane.addTab("Sumary", new DashboardPanel());
		mainTabbedPane.addTab("Drivers", new DriverPanel(rol));
		mainTabbedPane.addTab("Licenses",  new LicensePanel(rol));
		mainTabbedPane.addTab("Exams", new ExamPanel(rol));
		mainTabbedPane.addTab("Violations", new ViolationPanel(rol));
		mainTabbedPane.addTab("AssociatedEntity", new AssociatedEntityPanel(rol));
		mainTabbedPane.addTab("Reports", new ReportPanel());	


		getContentPane().add(mainTabbedPane);
		setupTheme();
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
	private void createMenu(String rol) {
		JMenuBar menuBar = new JMenuBar();

		JMenu settingsMenu = new JMenu("Settings");
		JMenu themeAdmin = new JMenu("Admin");
		JMenu themeMenu = new JMenu("Theme");

		JMenuItem lightTheme = new JMenuItem("Light");
		JMenuItem darkTheme = new JMenuItem("Dark");
		if(rol.equalsIgnoreCase("admin")) {
		JMenuItem centerConfig = new JMenuItem("Center configuration");
		JMenuItem addRol = new JMenuItem("Add Rol");
		JMenuItem users = new JMenuItem("Users");
		centerConfig.addActionListener(e -> CenterInfoPanel.showEditableDialog(this));
		addRol.addActionListener(e -> {
		    AddRol ventanaRol = new AddRol(); 
		    ventanaRol.setLocationRelativeTo(null);
		    ventanaRol.setVisible(true);    
		});
		users.addActionListener(e -> {
			JDialog userDialog = new JDialog(this, "User Management", true);
			userDialog.setSize(800, 600);
			userDialog.setLayout(new BorderLayout());
			userDialog.add(BorderLayout.CENTER, new UserPanel());
			userDialog.setLocationRelativeTo(this);
			userDialog.setVisible(true);
		});
		settingsMenu.add(themeAdmin);
		themeAdmin.add(centerConfig);
		themeAdmin.add(addRol);
		themeAdmin.add(users);
		}
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
			new LicenseManagementUI("admin").setVisible(true);
		});
	}
}