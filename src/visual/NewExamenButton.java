package visual;

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

/**
 * Button to register a new exam, following the pattern of NewDriverButton and NewViolationButton.
 */
public class NewExamenButton extends AbstractAddButton {
    private static final long serialVersionUID = 1L;

    private JTextField txtExamCode;
    private JComboBox<String> cmbExamType;
    private JXDatePicker datePicker;
    private JComboBox<String> cmbResult;
    private JTextField txtExaminerName;
    private JTextField txtEntityCode;
    private JTextField txtDriverId;

    public NewExamenButton(JFrame parent, Runnable refreshCallback) {
        super("New Exam", parent, refreshCallback);
    }

    @Override
    protected void showFormDialog() {
        JDialog dialog = new JDialog(parentFrame, "Register Exam", true);
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

        addFormField(panel, "Exam Code*:", txtExamCode);
        addFormField(panel, "Type*:", cmbExamType);
        addFormField(panel, "Date*:", datePicker);
        addFormField(panel, "Result*:", cmbResult);
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

        // Examiner Name
        errors.addAll(Validation.validateRequired(txtExaminerName.getText(), "Examiner Name"));
        errors.addAll(Validation.validateLength(txtExaminerName.getText(), 2, 50, "Examiner Name"));

        // Entity Code
        errors.addAll(Validation.validateRequired(txtEntityCode.getText(), "Entity Code"));

        // Driver ID
        errors.addAll(Validation.validateRequired(txtDriverId.getText(), "Driver ID"));

        return Validation.showErrors(parentFrame, errors);
    }

    @Override
    protected void saveToDatabase() {
        Exam exam = new Exam();
        exam.setExamCode(txtExamCode.getText().trim());
        exam.setExamType((String) cmbExamType.getSelectedItem());
        exam.setExamDate(new Date(datePicker.getDate().getTime()));
        exam.setResult((String) cmbResult.getSelectedItem());
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