package visual;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXDatePicker;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import model.Driver;
import model.Violation;
import services.DriverService;
import services.ViolationService;

public class LicenseManagementUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private boolean darkMode = true;
	private JTable driversTable;
	private JTable violationsTable;

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
				UIManager.setLookAndFeel(new FlatDarkLaf());
			} else {
				UIManager.setLookAndFeel(new FlatLightLaf());
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
	private void refreshViolationsTable() {
	    DefaultTableModel model = (DefaultTableModel) violationsTable.getModel();
	    model.setRowCount(0);

	    // SUPONIENDO QUE TIENES UN SERVICIO SIMILAR A DriverService
	    List<Violation> violations = new ViolationService().getAllViolations();
	    for (Violation violation : violations) {
	        model.addRow(new Object[]{
	            violation.getViolationCode(),
	            violation.getLicenseCode(), // o como corresponda a tu modelo
	            violation.getViolationType(),
	            new SimpleDateFormat("yyyy-MM-dd").format(violation.getDate()),
	            violation.getDeductedPoints(),
	            violation.isPaid() ? "Paid" : "Pending"
	        });
	    }
	}
	private JPanel createDashboardPanel() {
		JPanel dashboardPanel = new JPanel(new BorderLayout(10, 10));

		// Top panel with quick statistics
		JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
		statsPanel.add(createStatCard("Active Licenses", "1,234"));
		statsPanel.add(createStatCard("Pending Renewals", "89"));
		statsPanel.add(createStatCard("Recent Violations", "45"));
		statsPanel.add(createStatCard("Scheduled Exams", "12"));

		// Central panel with charts (placeholder)
		JPanel chartPanel = new JPanel();
		chartPanel.setBackground(new Color(30, 30, 30));
		chartPanel.setPreferredSize(new Dimension(300, 400));

		dashboardPanel.add(statsPanel, BorderLayout.NORTH);
		dashboardPanel.add(chartPanel, BorderLayout.CENTER);

		return dashboardPanel;
	}

	private JPanel createDriversPanel() {
		JPanel driversPanel = new JPanel(new BorderLayout());

		// Toolbar
		JToolBar toolbar = new JToolBar();
		  AbstractAddButton newDriverBtn = new NewDriverButton(this, this::refreshDriversTable);
		    toolbar.add(newDriverBtn);
		    
		    // Botón Edit
		    JButton editBtn = new JButton("Edit", UIManager.getIcon("FileView.computerIcon"));
		    editBtn.setFocusable(false);
		    toolbar.add(editBtn);
		
	    JButton deleteBtn = new JButton("Delete", UIManager.getIcon("FileView.floppyDriveIcon"));
	    editBtn.setFocusable(false);
	    toolbar.add(deleteBtn);

		// Drivers table
	    // Inicializar la tabla usando la variable de clase
	    String[] columns = {"ID", "First Name", "Last Name", "Document", "License", "Status"};
	    DefaultTableModel model = new DefaultTableModel(columns, 0);
	    driversTable = new JTable(model);  // Usa la variable de clase

		driversPanel.add(toolbar, BorderLayout.NORTH);
		driversPanel.add(new JScrollPane(driversTable), BorderLayout.CENTER);

		return driversPanel;
	}
	private void refreshDriversTable() {
	    // Lógica para actualizar la tabla de conductores
	    DefaultTableModel model = (DefaultTableModel) driversTable.getModel();
	    model.setRowCount(0);
	    
	    List<Driver> drivers = new DriverService().getAllDrivers();
	    for(Driver driver : drivers) {
	        model.addRow(new Object[]{
	            driver.getDriverId(),
	            driver.getFirstName(),
	            driver.getLastName(),
	            new SimpleDateFormat("yyyy-MM-dd").format(driver.getBirthDate()),
	            driver.getLicenseStatus()
	        });
	    }
	}

	private JPanel createLicensesPanel() {
		JPanel licensesPanel = new JPanel(new BorderLayout());

		// Filters
		JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterPanel.add(new JLabel("Filter by type:"));
		filterPanel.add(new JComboBox<>(new String[]{"All", "A", "B", "C", "D", "E"}));
		filterPanel.add(new JLabel("Status:"));
		filterPanel.add(new JComboBox<>(new String[]{"All", "Active", "Expired", "Suspended"}));

		// Licenses table
		String[] licenseColumns = {"Code", "Driver", "Type", "Issue Date", "Expiration", "Status"};
		JTable licensesTable = new JTable(new Object[0][6], licenseColumns);

		licensesPanel.add(filterPanel, BorderLayout.NORTH);
		licensesPanel.add(new JScrollPane(licensesTable), BorderLayout.CENTER);

		return licensesPanel;
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

	private JButton createButton(String text, Icon icon) {
		JButton button = new JButton(text, icon);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button.setFocusable(false);
		return button;
	}

	private JPanel createExamsPanel() {
		JPanel examsPanel = new JPanel(new BorderLayout(10, 10));

		// Toolbar
		JToolBar examsToolbar = new JToolBar();
	    JButton newExamBtn = new JButton("New Exam", UIManager.getIcon("FileChooser.newFolderIcon"));
	    newExamBtn.setFocusable(false);
	    examsToolbar.add(newExamBtn);
		
	    JButton CertificateExamBtn = new JButton("Generate Certificate", UIManager.getIcon("FileView.computerIcon"));
	    CertificateExamBtn.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	}
	    });
	    newExamBtn.setFocusable(false);
	    examsToolbar.add(CertificateExamBtn);

		// Filters
		JPanel examFilters = new JPanel(new FlowLayout(FlowLayout.LEFT));
		examFilters.add(new JLabel("Type:"));
		examFilters.add(new JComboBox<>(new String[]{"All", "Theory", "Practice", "Medical"}));
		examFilters.add(new JLabel("Date:"));
		examFilters.add(new JXDatePicker());
		examFilters.add(new JLabel("to"));
		examFilters.add(new JXDatePicker());

		// Exams table
		String[] examColumns = {"Code", "Driver", "Type", "Date", "Result", "Institution"};
		Object[][] examData = new Object[0][6];
		JTable examsTable = new JTable(examData, examColumns);

		examsPanel.add(examsToolbar, BorderLayout.NORTH);
		examsPanel.add(examFilters, BorderLayout.CENTER);
		examsPanel.add(new JScrollPane(examsTable), BorderLayout.SOUTH);

		return examsPanel;
	}

	private JPanel createViolationsPanel() {
		JPanel violationsPanel = new JPanel(new BorderLayout(10, 10));

		// Toolbar with actions
		JToolBar violationsToolbar = new JToolBar();
		violationsToolbar.add(new NewViolationButton(this, this::refreshViolationsTable));
		violationsToolbar.add(createButton("Mark as Paid", UIManager.getIcon("FileView.floppyDriveIcon")));

		// Filters
		JPanel violationFilters = new JPanel(new FlowLayout(FlowLayout.LEFT));
		violationFilters.add(new JLabel("Type:"));
		violationFilters.add(new JComboBox<>(new String[]{"All", "Minor", "Major", "Severe"}));
		violationFilters.add(new JLabel("Status:"));
		violationFilters.add(new JComboBox<>(new String[]{"All", "Paid", "Pending"}));

		// Violations table
		String[] violationColumns = {"Code", "Driver", "Type", "Date", "Points", "Status"};
		JTable violationsTable = new JTable(new Object[0][6], violationColumns);

		violationsPanel.add(violationsToolbar, BorderLayout.NORTH);
		violationsPanel.add(violationFilters, BorderLayout.CENTER);
		violationsPanel.add(new JScrollPane(violationsTable), BorderLayout.SOUTH);

		return violationsPanel;
	}

	private JPanel createReportsPanel() {
		JPanel reportsPanel = new JPanel(new BorderLayout(10, 10));

		// Report selector
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		headerPanel.add(new JLabel("Select Report:"));
		JComboBox<String> reportSelector = new JComboBox<>(new String[]{
				"Issued Licenses",
				"Completed Exams",
				"Registered Violations",
				"Violation Summary",
				"Expired Licenses"
		});
		headerPanel.add(reportSelector);

		// Report parameters
		JPanel paramsPanel = new JPanel(new CardLayout());

		// Date range panel
		JPanel dateRangePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		dateRangePanel.add(new JLabel("From:"));
		dateRangePanel.add(new JXDatePicker());
		dateRangePanel.add(new JLabel("To:"));
		dateRangePanel.add(new JXDatePicker());

		// Year panel
		JPanel yearPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		yearPanel.add(new JLabel("Year:"));
		yearPanel.add(new JSpinner(new SpinnerNumberModel(2024, 2000, 2100, 1)));

		paramsPanel.add(dateRangePanel, "DATE_RANGE");
		paramsPanel.add(yearPanel, "YEAR");

		// Generate button
		JButton generateBtn = new JButton("Generate Report");

		// Results area
		JTable resultsTable = new JTable();

		// Layout
		reportsPanel.add(headerPanel, BorderLayout.NORTH);
		reportsPanel.add(paramsPanel, BorderLayout.CENTER);
		reportsPanel.add(generateBtn, BorderLayout.SOUTH);
		reportsPanel.add(new JScrollPane(resultsTable), BorderLayout.SOUTH);

		// Parameter switching logic
		reportSelector.addActionListener(e -> {
			String selection = (String) reportSelector.getSelectedItem();
			CardLayout cl = (CardLayout) paramsPanel.getLayout();
			if(selection.contains("Summary")) {
				cl.show(paramsPanel, "YEAR");
			} else {
				cl.show(paramsPanel, "DATE_RANGE");
			}
		});

		return reportsPanel;
	}



	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			new LicenseManagementUI().setVisible(true);
		});
	}
}