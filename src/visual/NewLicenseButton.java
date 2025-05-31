package visual;

import model.License;
import services.LicenseService;
import utils.Validation;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;

public class NewLicenseButton extends AbstractAddButton {
    private static final long serialVersionUID = 1L;

    private JTextField txtCode;
    private JComboBox<String> cmbType;
    private JXDatePicker datePickerIssue;
    private JXDatePicker datePickerExpiration;
    private JComboBox<String> cmbVehicle;
    private JRadioButton[] restrictionButtons;
    private JComboBox<String> cmbRenewed;
    private JTextField txtDriverId;

    private static final String[] RESTRICTION_OPTIONS = {
        "use of glasses", "daytime driving", "automatic vehicle", "hearing aids"
    };

    public NewLicenseButton(JFrame parent, Runnable refreshCallback) {
        super("New License", parent, refreshCallback);
    }

    @Override
    protected void showFormDialog() {
        JDialog dialog = new JDialog(parentFrame, "Register License", true);
        dialog.setLayout(new BorderLayout());
        dialog.setPreferredSize(new Dimension(420, 400));

        JPanel formPanel = createFormPanel();
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(createButtonPanel(dialog), BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        initializeFormComponents();

        int y = 0;
        addFormField(panel, "Code*:", txtCode, gbc, y++);
        addFormField(panel, "Type*:", cmbType, gbc, y++);
        addFormField(panel, "Issue Date*:", datePickerIssue, gbc, y++);
        addFormField(panel, "Expiration Date*:", datePickerExpiration, gbc, y++);
        addFormField(panel, "Vehicle Category*:", cmbVehicle, gbc, y++);

        // Restrictions: label and radio buttons (vertical layout)
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 0;
        panel.add(new JLabel("Restrictions:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        JPanel restrPanel = new JPanel();
        restrPanel.setLayout(new BoxLayout(restrPanel, BoxLayout.Y_AXIS));
        for (JRadioButton btn : restrictionButtons) {
            restrPanel.add(btn);
        }
        panel.add(restrPanel, gbc);
        y++;

        gbc.anchor = GridBagConstraints.WEST;
        addFormField(panel, "Renewed*:", cmbRenewed, gbc, y++);
        addFormField(panel, "Driver ID*:", txtDriverId, gbc, y++);

        return panel;
    }

    private void initializeFormComponents() {
        txtCode = new JTextField(12);
        cmbType = new JComboBox<>(new String[]{"A", "B", "C", "D", "E"});
        datePickerIssue = new JXDatePicker();
        datePickerExpiration = new JXDatePicker();
        cmbVehicle = new JComboBox<>(new String[]{"Motorcycle", "Car", "Truck", "Bus"});

        // Use JRadioButton for each restriction (independent, not ButtonGroup)
        restrictionButtons = new JRadioButton[RESTRICTION_OPTIONS.length];
        for (int i = 0; i < RESTRICTION_OPTIONS.length; i++) {
            restrictionButtons[i] = new JRadioButton(RESTRICTION_OPTIONS[i]);
        }

        cmbRenewed = new JComboBox<>(new String[]{"Yes", "No"});
        txtDriverId = new JTextField(12);

        datePickerIssue.setFormats("yyyy-MM-dd");
        datePickerExpiration.setFormats("yyyy-MM-dd");
        txtCode.setToolTipText("License code format, e.g. LIC-0001");
    }

    private void addFormField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private JPanel createButtonPanel(JDialog dialog) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> {
            if (validateForm()) {
                try {
                    saveToDatabase();
                    refreshCallback.run();
                    dialog.dispose();
                } catch (RuntimeException ex) {
                    String msg = ex.getMessage();
                    Throwable cause = ex.getCause();
                    if (cause instanceof java.sql.SQLException) {
                        java.sql.SQLException sqlEx = (java.sql.SQLException) cause;
                        if ("23503".equals(sqlEx.getSQLState())) {
                            JOptionPane.showMessageDialog(dialog,
                                    "The driver ID does not exist. Please verify.",
                                    "Driver not found",
                                    JOptionPane.ERROR_MESSAGE
                            );
                            return;
                        }
                    }
                    if (msg == null || msg.isEmpty()) msg = "Unexpected error while saving the license.";
                    JOptionPane.showMessageDialog(dialog, msg, "Error saving", JOptionPane.ERROR_MESSAGE);
                }
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

        errors.addAll(Validation.validateRequired(txtCode.getText(), "License Code"));
        errors.addAll(Validation.validateLength(txtCode.getText(), 1, 10, "License Code"));

        if (cmbType.getSelectedIndex() < 0) {
            errors.add("You must select a license type.");
        }

        if (datePickerIssue.getDate() == null) {
            errors.add("Issue date is required.");
        }
        if (datePickerExpiration.getDate() == null) {
            errors.add("Expiration date is required.");
        }

        if (datePickerIssue.getDate() != null && datePickerExpiration.getDate() != null
                && datePickerExpiration.getDate().before(datePickerIssue.getDate())) {
            errors.add("Expiration date must be after issue date.");
        }

        if (cmbVehicle.getSelectedIndex() < 0) {
            errors.add("You must select a vehicle category.");
        }

        // It's valid to have no restrictions selected

        if (cmbRenewed.getSelectedIndex() < 0) {
            errors.add("You must select if renewed.");
        }

        errors.addAll(Validation.validateRequired(txtDriverId.getText(), "Driver ID"));
        errors.addAll(Validation.validateID(txtDriverId.getText()));

        return Validation.showErrors(parentFrame, errors);
    }

    @Override
    protected void saveToDatabase() {
        License license = new License();
        license.setLicenseCode(txtCode.getText().trim());
        license.setLicenseType((String) cmbType.getSelectedItem());
        license.setIssueDate(new Date(datePickerIssue.getDate().getTime()));
        license.setExpirationDate(new Date(datePickerExpiration.getDate().getTime()));
        license.setVehicleCategory((String) cmbVehicle.getSelectedItem());

        // Collect selected restrictions
        ArrayList<String> selectedRestrictions = new ArrayList<>();
        for (JRadioButton btn : restrictionButtons) {
            if (btn.isSelected()) {
                selectedRestrictions.add(btn.getText());
            }
        }
        String restrictions = String.join(", ", selectedRestrictions);
        license.setRestrictions(restrictions);

        license.setRenewed("Yes".equals(cmbRenewed.getSelectedItem()));
        license.setDriverId(txtDriverId.getText().trim());

        boolean ok = new LicenseService().create(license);

        if (ok) {
            JOptionPane.showMessageDialog(parentFrame, "License successfully registered.");
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Error registering license.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}