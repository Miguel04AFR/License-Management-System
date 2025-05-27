package visual;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.jdesktop.swingx.JXDatePicker;

import model.Violation;
import services.ViolationService;
import utils.Validation;

public class NewViolationButton extends AbstractAddButton {
    private static final long serialVersionUID = 1L;

    private JTextField txtViolationCode;
    private JTextField txtViolationType;
    private JXDatePicker datePicker;
    private JTextField txtLocation;
    private JTextField txtDescription;
    private JSpinner spnDeductedPoints;
    private JCheckBox chkPaid;
    private JTextField txtLicenseCode;

    public NewViolationButton(JFrame parent, Runnable refreshCallback) {
        super("Register Violation", parent, refreshCallback);
    }

    @Override
    protected void showFormDialog() {
        JDialog dialog = new JDialog(parentFrame, "Register Violation", true);
        dialog.setLayout(new BorderLayout());
        dialog.setPreferredSize(new Dimension(500, 400));

        JPanel formPanel = createFormPanel();
        dialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        dialog.add(createButtonPanel(dialog), BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initializeFormComponents();

        addFormField(panel, "Violation Code*:", txtViolationCode);
        addFormField(panel, "Type*:", txtViolationType);
        addFormField(panel, "Date*:", datePicker);
        addFormField(panel, "Location*:", txtLocation);
        addFormField(panel, "Description:", txtDescription);
        addFormField(panel, "Deducted Points*:", spnDeductedPoints);
        addFormField(panel, "Paid:", chkPaid);
        addFormField(panel, "License Code*:", txtLicenseCode);

        return panel;
    }

    private void initializeFormComponents() {
        txtViolationCode = new JTextField();
        txtViolationType = new JTextField();
        datePicker = new JXDatePicker();
        txtLocation = new JTextField();
        txtDescription = new JTextField();
        spnDeductedPoints = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
        chkPaid = new JCheckBox();
        txtLicenseCode = new JTextField();

        datePicker.setFormats("yyyy-MM-dd");
        txtViolationCode.setToolTipText("Format: VLT-0000");
    }

    private JPanel createButtonPanel(JDialog dialog) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> {
            if (validateForm()) {
                saveToDatabase();
                refreshCallback.run();
                dialog.dispose();
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        panel.add(btnSave);
        panel.add(btnCancel);
        return panel;
    }

    @Override
    protected boolean validateForm() {
        ArrayList<String> errors = new ArrayList<>();

        // Violation Code
        errors.addAll(Validation.validateRequired(txtViolationCode.getText(), "Violation Code"));
        errors.addAll(Validation.validateFormat(txtViolationCode.getText().trim(),
                "^VLT-\\d{4}$",
                "Invalid code format (VLT-0000)"));

        // Type
        errors.addAll(Validation.validateRequired(txtViolationType.getText(), "Type"));
        errors.addAll(Validation.validateLength(txtViolationType.getText(), 2, 40, "Type"));

        // Date
        errors.addAll(Validation.validateRequired(txtLocation.getText(), "Location"));
        errors.addAll(Validation.validateMinimumAge(datePicker.getDate(), 0, "Violation Date")); // Just checks it's set

        // License Code
        errors.addAll(Validation.validateRequired(txtLicenseCode.getText(), "License Code"));
        errors.addAll(Validation.validateFormat(txtLicenseCode.getText().trim(),
                "^LIC-\\d{4}$",
                "Invalid license code format (LIC-0000)"));

        // Deducted Points
        int points = (Integer) spnDeductedPoints.getValue();
        if (points < 0) {
            errors.add("Deducted Points must be 0 or greater");
        }

        return Validation.showErrors(this, errors);
    }

    @Override
    protected void saveToDatabase() {
        Violation violation = new Violation();
        violation.setViolationCode(txtViolationCode.getText().trim());
        violation.setViolationType(txtViolationType.getText().trim());
        java.util.Date utilDate = datePicker.getDate();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        violation.setDate(sqlDate);
        violation.setLocation(txtLocation.getText().trim());
        violation.setDescription(txtDescription.getText().trim());
        violation.setDeductedPoints((Integer) spnDeductedPoints.getValue());
        violation.setPaid(chkPaid.isSelected());
        violation.setLicenseCode(txtLicenseCode.getText().trim());

        ViolationService service = new ViolationService();
        if (!service.createViolation(violation)) {
            JOptionPane.showMessageDialog(parentFrame,
                    "Error saving violation to database",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addFormField(JPanel panel, String label, JComponent component) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        panel.add(lbl);
        panel.add(component);
    }
}