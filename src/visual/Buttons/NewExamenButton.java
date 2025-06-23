package visual.Buttons;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Date;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXDatePicker;

import model.Exam;
import services.ExamService;
import utils.Validation;

public class NewExamenButton extends AbstractAddButton {
    private static final long serialVersionUID = 1L;

    private JTextField txtExamCode;
    private JComboBox<String> cmbExamType;
    private JXDatePicker datePicker;
    private JComboBox<String> cmbResult;
    private JTextField txtExaminerName;
    private JTextField txtEntityCode;
    private JTextField txtDriverId;
    private JComboBox<String> cmbVehicleCategory; // NEW FIELD

    public NewExamenButton(JFrame parent, Runnable refreshCallback) {
        super("New Exam", parent, refreshCallback);
    }

    @Override
    protected void showFormDialog() {
        JDialog dialog = new JDialog(parentFrame, "Register Exam", true);
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

        addFormField(panel, "Exam Code*:", txtExamCode);
        addFormField(panel, "Type*:", cmbExamType);
        addFormField(panel, "Date*:", datePicker);
        addFormField(panel, "Result*:", cmbResult);
        addFormField(panel, "Vehicle Category*:", cmbVehicleCategory); // NEW FIELD
        addFormField(panel, "Examiner Name*:", txtExaminerName);
        addFormField(panel, "Entity Code*:", txtEntityCode);
        addFormField(panel, "Driver ID*:", txtDriverId);

        return panel;
    }

    private void initializeFormComponents() {
        txtExamCode = new JTextField();
        cmbExamType = new JComboBox<>(new String[]{"Theory", "Practical", "Medical"});
        datePicker = new JXDatePicker();
        cmbResult = new JComboBox<>(new String[]{"Approved", "Under Review", "Disapproved"});
        cmbVehicleCategory = new JComboBox<>(new String[]{"Motorcycle", "Car", "Truck", "Bus"}); // NEW FIELD
        txtExaminerName = new JTextField();
        txtEntityCode = new JTextField();
        txtDriverId = new JTextField();

        datePicker.setFormats("yyyy-MM-dd");
        txtExamCode.setToolTipText("Format: EXM-0001");
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
                    // Verifica si la causa es SQLException
                    if (cause instanceof java.sql.SQLException) {
                        java.sql.SQLException sqlEx = (java.sql.SQLException) cause;
                        // 23503 = foreign key violation (PostgreSQL)
                        if ("23503".equals(sqlEx.getSQLState())) {
                            JOptionPane.showMessageDialog(dialog,
                            		"The associated entity code does not exist. Please verify.",
                            		"Entity not found",
                                JOptionPane.ERROR_MESSAGE
                            );
                            return;
                        }
                    }
                    // Otros errores
                    if (msg == null || msg.isEmpty()) msg = "Error .";
                    JOptionPane.showMessageDialog(dialog, msg, "Error", JOptionPane.ERROR_MESSAGE);
                    // NO cerrar el dialog, así el usuario puede corregir!
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

        // Exam Code
        errors.addAll(Validation.validateRequired(txtExamCode.getText(), "Exam Code"));
        errors.addAll(Validation.validateFormat(txtExamCode.getText().trim(), "^EXM-\\d{4}$", "Invalid exam code format (EXM-0000)"));

        // Exam Type
        if (cmbExamType.getSelectedIndex() < 0) {
            errors.add("You must select an exam type.");
        }

        // Date
        if (datePicker.getDate() == null) {
            errors.add("Date is required.");
        }

        // Result
        if (cmbResult.getSelectedIndex() < 0) {
            errors.add("You must select a result.");
        }

        // Vehicle Category
        if (cmbVehicleCategory.getSelectedIndex() < 0) {
            errors.add("You must select a vehicle category.");
        }

        // Examiner Name
        errors.addAll(Validation.validateRequired(txtExaminerName.getText(), "Examiner Name"));
        errors.addAll(Validation.validateLength(txtExaminerName.getText(), 2, 50, "Examiner Name"));
//041228168
        // Entity Code
        errors.addAll(Validation.validateRequired(txtEntityCode.getText(), "Entity Code"));

        // Driver ID
        errors.addAll(Validation.validateRequired(txtDriverId.getText(), "Driver ID"));
        ExamService examService = new ExamService();
        String examType = (String) cmbExamType.getSelectedItem();
        String driverId = txtDriverId.getText();
        String vehicleCategory = (String) cmbVehicleCategory.getSelectedItem();
        
        if (examType.equals("Theory")) {
            if (!examService.hasApprovedMedicalExamForCategory(driverId, vehicleCategory)) {
                errors.add("The driver must have an approved medical exam before the theoretical exam in this category.");
            }
        }
        
        if (examType.equals("Practical")) {
            if (!examService.hasApprovedTheoryExamForCategory(driverId, vehicleCategory)) {
                errors.add("The driver must have an approved theoretical exam before the practical exam in this category.");
            }
            if (!examService.hasApprovedMedicalExamForCategory(driverId, vehicleCategory)) {
                errors.add("The driver must have an approved medical exam before the practical exam in this category.");
            }
        }
        
        if (examService.isDuplicateExamTypeAndCategory(driverId, examType, vehicleCategory)) {
            errors.add("The driver already has a registered exam of this type and category.");
        }
        if (!examService.existsEntityCode(txtEntityCode.getText())) {
            errors.add("The entity code does not exist. Please verify.");
        }
        if (!examService.existsDriverId(driverId)) {
            errors.add("The driver ID does not exist. Please verify.");
        }
        if (!examService.isEntityEligibleForExamType(txtEntityCode.getText(), examType)) {
            errors.add("The entity is not eligible to administer this type of exam.");
        }
        return Validation.showErrors(parentFrame, errors);
    }

    @Override
    protected void saveToDatabase() {
        Exam exam = new Exam();
        exam.setExamCode(txtExamCode.getText().trim());
        exam.setExamType((String) cmbExamType.getSelectedItem());
        exam.setExamDate(new Date(datePicker.getDate().getTime()));
        exam.setResult((String) cmbResult.getSelectedItem());
        exam.setVehicleCategory((String) cmbVehicleCategory.getSelectedItem()); // NEW FIELD
        exam.setExaminerName(txtExaminerName.getText().trim());
        exam.setEntityCode(txtEntityCode.getText().trim());
        exam.setDriverId(txtDriverId.getText().trim());

        boolean ok = new ExamService().create(exam);

        if (ok) {
            JOptionPane.showMessageDialog(parentFrame, "Exam successfully registered.");
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Error registering exam.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addFormField(JPanel panel, String label, JComponent field) {
        panel.add(new JLabel(label));
        panel.add(field);
    }
}