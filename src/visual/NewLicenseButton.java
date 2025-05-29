package visual;

import model.License;
import services.LicenseService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Date;

public class NewLicenseButton extends JButton {
	 private static final long serialVersionUID = 1L;
    private final Runnable onSuccess;

    public NewLicenseButton(String text, Runnable onSuccess) {
        super(text != null ? text : "New");
        this.onSuccess = onSuccess;
        setIcon(UIManager.getIcon("OptionPane.warningIcon"));
        addActionListener(this::showDialog);
    }

    private void showDialog(ActionEvent e) {
        JTextField txtCode = new JTextField();
        JComboBox<String> cmbType = new JComboBox<>(new String[]{"A", "B", "C", "D", "E"});
        JTextField txtIssueDate = new JTextField();
        JTextField txtExpirationDate = new JTextField();
        JComboBox<String> cmbVehicle = new JComboBox<>(new String[]{"Motorcycle", "Car", "Truck", "Bus"});
        JTextField txtRestrictions = new JTextField();
        JComboBox<String> cmbRenewed = new JComboBox<>(new String[]{"Yes", "No"});
        JTextField txtDriverId = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Code:"));
        panel.add(txtCode);
        panel.add(new JLabel("Type:"));
        panel.add(cmbType);
        panel.add(new JLabel("Issue Date (YYYY-MM-DD):"));
        panel.add(txtIssueDate);
        panel.add(new JLabel("Expiration Date (YYYY-MM-DD):"));
        panel.add(txtExpirationDate);
        panel.add(new JLabel("Vehicle Category:"));
        panel.add(cmbVehicle);
        panel.add(new JLabel("Restrictions:"));
        panel.add(txtRestrictions);
        panel.add(new JLabel("Renewed:"));
        panel.add(cmbRenewed);
        panel.add(new JLabel("Driver ID:"));
        panel.add(txtDriverId);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "New License",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            if (txtCode.getText().trim().isEmpty() ||
                txtIssueDate.getText().trim().isEmpty() ||
                txtExpirationDate.getText().trim().isEmpty() ||
                txtRestrictions.getText().trim().isEmpty() ||
                txtDriverId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields must be completed.");
                return;
            }

            License license = new License();
            license.setLicenseCode(txtCode.getText().trim());
            license.setLicenseType((String) cmbType.getSelectedItem());
            license.setIssueDate(Date.valueOf(txtIssueDate.getText().trim()));
            license.setExpirationDate(Date.valueOf(txtExpirationDate.getText().trim()));
            license.setVehicleCategory((String) cmbVehicle.getSelectedItem());
            license.setRestrictions(txtRestrictions.getText().trim());
            license.setRenewed("Yes".equals(cmbRenewed.getSelectedItem()));
            license.setDriverId(txtDriverId.getText().trim());

            boolean ok = new LicenseService().create(license);
            if (ok) {
                JOptionPane.showMessageDialog(null, "License added successfully.");
                if (onSuccess != null) onSuccess.run();
            } else {
                JOptionPane.showMessageDialog(null, "Error adding license.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}