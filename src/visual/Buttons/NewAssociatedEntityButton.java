package visual.Buttons;

import model.AssociatedEntity;
import services.AssociatedEntityService;
import utils.Validation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class NewAssociatedEntityButton extends AbstractAddButton {
    private static final long serialVersionUID = 1L;
    private JTextField txtEntityCode;
    private JTextField txtEntityName;
    private JComboBox<String> cmbEntityType;
    private JTextField txtAddress;
    private JTextField txtPhoneNumber;
    private JTextField txtContactEmail;
    private JTextField txtDirectorName;
   

    public NewAssociatedEntityButton(JFrame parent, Runnable refreshCallback) {
        super("New Associated Entity", parent, refreshCallback);
    }

    @Override
    protected void showFormDialog() {
        JDialog dialog = new JDialog(parentFrame, "New Associated Entity", true);
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

        addFormField(panel, "Entity Code*:", txtEntityCode);
        addFormField(panel, "Entity Name*:", txtEntityName);
        addFormField(panel, "Entity Type*:", cmbEntityType);
        addFormField(panel, "Address:", txtAddress);
        addFormField(panel, "Phone Number:", txtPhoneNumber);
        addFormField(panel, "Contact Email:", txtContactEmail);
        addFormField(panel, "Director Name:", txtDirectorName);
        

        return panel;
    }

    private void initializeFormComponents() {
        txtEntityCode = new JTextField();
        txtEntityName = new JTextField();
        cmbEntityType = new JComboBox<>(new String[]{"Clinic", "DrivingSchool"});
        txtAddress = new JTextField();
        txtPhoneNumber = new JTextField();
        txtContactEmail = new JTextField();
        txtDirectorName = new JTextField();
       
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

        errors.addAll(Validation.validateRequired(txtEntityCode.getText(), "Entity Code"));
        errors.addAll(Validation.validateRequired(txtEntityName.getText(), "Entity Name"));
      

        errors.addAll(Validation.validateLength(txtEntityCode.getText(), 2, 20, "Entity Code"));
        errors.addAll(Validation.validateLength(txtEntityName.getText(), 2, 100, "Entity Name"));

        errors.addAll(Validation.validateFormat(txtPhoneNumber.getText().trim(),
                "^\\+?[0-9\\s-]{7,15}$",
                "Invalid phone number format"));
        errors.addAll(Validation.validateEmail(txtContactEmail.getText().trim()));

        return Validation.showErrors(this, errors);
    }

    @Override
    protected void saveToDatabase() {
        AssociatedEntity entity = new AssociatedEntity();
        entity.setEntityCode(txtEntityCode.getText().trim());
        entity.setEntityName(txtEntityName.getText().trim());
        entity.setEntityType((String) cmbEntityType.getSelectedItem());
        entity.setAddress(txtAddress.getText().trim());
        entity.setPhoneNumber(txtPhoneNumber.getText().trim());
        entity.setContactEmail(txtContactEmail.getText().trim());
        entity.setDirectorName(txtDirectorName.getText().trim());
       ;

        AssociatedEntityService service = new AssociatedEntityService();
        if (!service.create(entity)) {
            JOptionPane.showMessageDialog(parentFrame,
                    "Error saving associated entity to database",
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