package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import services.AssociatedEntityService;
import services.DriverService;
import services.ExamService;
import services.LicenseService;
import services.ViolationService;

public class DashboardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final LicenseService licenseService = new LicenseService();
    private final ViolationService violationService = new ViolationService();
    private final ExamService examService = new ExamService();
    private final DriverService driverService = new DriverService();
    private final AssociatedEntityService entityService = new AssociatedEntityService();

    private final JLabel lblTotalDrivers = new JLabel("0");
    private final JLabel lblTotalLicenses = new JLabel("0");
    private final JLabel lblActiveLicenses = new JLabel("0");
    private final JLabel lblInactiveLicenses = new JLabel("0");
    private final JLabel lblSuspendedLicenses = new JLabel("0");
    private final JLabel lblRenewedLicenses = new JLabel("0");
    private final JLabel lblTotalViolations = new JLabel("0");
    private final JLabel lblPaidViolations = new JLabel("0");
    private final JLabel lblUnpaidViolations = new JLabel("0");
    private final JLabel lblAssociatedEntities = new JLabel("0");
    private final JLabel lblTotalExams = new JLabel("0");
    private final JLabel lblMedicalExams = new JLabel("0");
    private final JLabel lblTheoryExams = new JLabel("0");
    private final JLabel lblPracticalExams = new JLabel("0");
    // NUEVOS LABELS PARA ALERTAS PERSONALIZADAS
    private final JLabel lblSoonToExpireLicenses = new JLabel("0");
    private final JLabel lblPendingMedicalExams = new JLabel("0");

    public DashboardPanel() {
        setLayout(new BorderLayout(16, 16));
        setOpaque(true);

        JLabel title = new JLabel("System Dashboard", SwingConstants.LEFT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        JPanel mainGrid = new JPanel(new GridLayout(2, 3, 16, 16));
        mainGrid.setOpaque(false);

        mainGrid.add(makeCard("Drivers", lblTotalDrivers));
        mainGrid.add(makeCard("Licenses", lblTotalLicenses));
        mainGrid.add(makeCard("Violations", lblTotalViolations));
        mainGrid.add(makeCard("Entities", lblAssociatedEntities));
        mainGrid.add(makeCard("Exams", lblTotalExams));
        mainGrid.add(makeAlertsPanel());

        add(mainGrid, BorderLayout.CENTER);

        JPanel detailsPanel = new JPanel(new GridLayout(2, 4, 12, 3));
        detailsPanel.setOpaque(false);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        detailsPanel.add(makeSmallCard("Active Licenses", lblActiveLicenses));
        detailsPanel.add(makeSmallCard("Inactive Licenses", lblInactiveLicenses));
        detailsPanel.add(makeSmallCard("Suspended Licenses", lblSuspendedLicenses));
        detailsPanel.add(makeSmallCard("Renewed Licenses", lblRenewedLicenses));
        detailsPanel.add(makeSmallCard("Paid Violations", lblPaidViolations));
        detailsPanel.add(makeSmallCard("Medical Exams", lblMedicalExams));
        detailsPanel.add(makeSmallCard("Theory Exams", lblTheoryExams));
        detailsPanel.add(makeSmallCard("Practical Exams", lblPracticalExams));

        add(detailsPanel, BorderLayout.SOUTH);

        refreshData();
    }

    public void refreshData() {
        setTotalDrivers(driverService.getAll().size());
        setTotalLicenses(licenseService.getAll().size());
        setActiveLicenses(licenseService.countActiveLicenses());
        setInactiveLicenses(licenseService.countInactiveLicenses());
        setSuspendedLicenses(licenseService.countSuspendedLicenses());
        setRenewedLicenses(licenseService.countRenewedLicenses());
        setTotalViolations(violationService.getAll().size());
        setPaidViolations(violationService.countPaidViolations());
        setUnpaidViolations(violationService.countUnpaidViolations());
        setAssociatedEntities(entityService.getAll().size());
        setTotalExams(examService.getAll().size());
        setMedicalExams(examService.countMedicalExams());
        setTheoryExams(examService.countTheoryExams());
        setPracticalExams(examService.countPracticalExams());
        
        setPendingMedicalExams(examService.countDriversWithoutMedicalExam());
        setSoonToExpireLicenses(licenseService.countSoonToExpireLicenses());
    }

    private JPanel makeCard(String title, JLabel valueLabel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 28f));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel makeSmallCard(String title, JLabel valueLabel) {
        JPanel panel = new JPanel(new BorderLayout());
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.PLAIN, 18f));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(valueLabel, BorderLayout.CENTER);
        JLabel lbl = new JLabel(title, SwingConstants.CENTER);
        lbl.setFont(lbl.getFont().deriveFont(Font.PLAIN, 12f));
        panel.add(lbl, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel makeAlertsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 0, 7));
        panel.setBorder(BorderFactory.createTitledBorder("Alerts"));
        panel.setOpaque(false);

        panel.add(makeAlertRow("Unpaid Violations", lblUnpaidViolations, new Color(211, 47, 47)));
        panel.add(makeAlertRow("Soon-to-Expire Licenses", lblSoonToExpireLicenses, new Color(255, 160, 0)));
        panel.add(makeAlertRow("Pending Medical Exams", lblPendingMedicalExams, new Color(46, 125, 50)));
        return panel;
    }

    private JPanel makeAlertRow(String labelText, JLabel valueLabel, Color textColor) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        row.setOpaque(false);

        JLabel label = new JLabel(labelText + ": ");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
        if (textColor != null) label.setForeground(textColor);

        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 18f));
        if (textColor != null) valueLabel.setForeground(textColor);
        else valueLabel.setForeground(UIManager.getColor("Label.foreground"));

        row.add(label);
        row.add(valueLabel);
        return row;
    }

    // Setters públicos igual que antes
    public void setTotalDrivers(int value) { lblTotalDrivers.setText(String.valueOf(value)); }
    public void setTotalLicenses(int value) { lblTotalLicenses.setText(String.valueOf(value)); }
    public void setActiveLicenses(int value) { lblActiveLicenses.setText(String.valueOf(value)); }
    public void setInactiveLicenses(int value) { lblInactiveLicenses.setText(String.valueOf(value)); }
    public void setSuspendedLicenses(int value) { lblSuspendedLicenses.setText(String.valueOf(value)); }
    public void setRenewedLicenses(int value) { lblRenewedLicenses.setText(String.valueOf(value)); }
    public void setTotalViolations(int value) { lblTotalViolations.setText(String.valueOf(value)); }
    public void setPaidViolations(int value) { lblPaidViolations.setText(String.valueOf(value)); }
    public void setUnpaidViolations(int value) { lblUnpaidViolations.setText(String.valueOf(value)); }
    public void setAssociatedEntities(int value) { lblAssociatedEntities.setText(String.valueOf(value)); }
    public void setTotalExams(int value) { lblTotalExams.setText(String.valueOf(value)); }
    public void setMedicalExams(int value) { lblMedicalExams.setText(String.valueOf(value)); }
    public void setTheoryExams(int value) { lblTheoryExams.setText(String.valueOf(value)); }
    public void setPracticalExams(int value) { lblPracticalExams.setText(String.valueOf(value)); }
    // NUEVOS SETTERS
    public void setSoonToExpireLicenses(int value) { lblSoonToExpireLicenses.setText(String.valueOf(value)); }
    public void setPendingMedicalExams(int value) { lblPendingMedicalExams.setText(String.valueOf(value)); }
}