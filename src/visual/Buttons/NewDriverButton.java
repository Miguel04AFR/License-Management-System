package visual.Buttons;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXDatePicker;

import model.Driver;
import services.DriverService;
import utils.Validation;


public class NewDriverButton extends AbstractAddButton {
	private static final long serialVersionUID = 1L;
    private JTextField txtDriverId;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JXDatePicker datePicker;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtAddress;
    

    public NewDriverButton(JFrame parent, Runnable refreshCallback) {
        super("New Driver", parent, refreshCallback);
    }
    
    @Override
    protected void showFormDialog() {
        JDialog dialog = new JDialog(parentFrame, "New Driver", true);
        dialog.setLayout(new BorderLayout());
        dialog.setPreferredSize(new Dimension(500, 450));

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
        
        addFormField(panel, "Driver ID*:", txtDriverId);
        addFormField(panel, "First Name*:", txtFirstName);
        addFormField(panel, "Last Name*:", txtLastName);
        addFormField(panel, "Birth Date*:", datePicker);
        addFormField(panel, "Phone*:", txtPhone);
        addFormField(panel, "Email:", txtEmail);
        addFormField(panel, "Address:", txtAddress);
     

        return panel;
    }

    private void initializeFormComponents() {
        txtDriverId = new JTextField();
        txtFirstName = new JTextField();
        txtLastName = new JTextField();
        datePicker = new JXDatePicker();
        txtPhone = new JTextField();
        txtEmail = new JTextField();
        txtAddress = new JTextField();
        datePicker.setFormats("yyyy-MM-dd");
        txtDriverId.setToolTipText("");
    }

    private JPanel createButtonPanel(JDialog dialog) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> {
            if(validateForm()) {
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
       
        // Validar ID del conductor
       errors.addAll(Validation.validateID(txtDriverId.getText()));        
        // Validar nombres
        errors.addAll(Validation.validateRequired(txtFirstName.getText(), "Name"));
        errors.addAll(Validation.validateRequired(txtLastName.getText(), "Last Name"));
        errors.addAll(Validation.validateLength(txtFirstName.getText(), 2, 50, "Name"));
        errors.addAll(Validation.validateLength(txtLastName.getText(), 2, 50, "Last Name"));
        
        // Validar fecha
        errors.addAll(Validation.validateMinimumAge(datePicker.getDate(), 18, "Birth Date"));
        
        // Validar teléfono
        errors.addAll(Validation.validateFormat(txtPhone.getText().trim(), 
                       "^\\+?[0-9\\s-]{7,15}$", 
                       "Invalid phone format"));
        errors.addAll(Validation.validateEmail(txtEmail.getText().trim()));
        
        // Validar combo box
       
        
        return Validation.showErrors(this, errors);
    }

    @Override
    protected void saveToDatabase() {
        Driver driver = new Driver();
        driver.setDriverId(txtDriverId.getText().trim());
        driver.setFirstName(txtFirstName.getText().trim());
        driver.setLastName(txtLastName.getText().trim());
        driver.setBirthDate(datePicker.getDate());
        driver.setAddress(txtAddress.getText().trim());
        driver.setPhoneNumber(txtPhone.getText().trim());
        driver.setEmail(txtEmail.getText().trim());     
        driver.setLicenseStatus("In Process");

        DriverService service = new DriverService();
        if(!service.create(driver)) {
            JOptionPane.showMessageDialog(parentFrame,
                "Error saving driver to database",
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