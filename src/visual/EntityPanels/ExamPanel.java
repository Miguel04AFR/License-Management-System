package visual.EntityPanels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.jdesktop.swingx.JXDatePicker;

import model.Exam;
import services.ExamService;
import visual.Buttons.NewExamenButton;

public class ExamPanel extends AbstractEntityPanel<Exam> {
    private static final long serialVersionUID = 1L;
    private JComboBox<String> statusFilterCombo;
    private JXDatePicker fromDatePicker;
    private JXDatePicker toDatePicker;
    private JComboBox<String> vehicleCategoryFilterCombo; // NEW FIELD
    private JButton filterButton;
    private JButton clearFilterButton;

    public ExamPanel() {
        super(new ExamService(), new String[]{
                "Code", "Type", "Date", "Result", "Vehicle Category", "Examiner", "Entity", "Driver ID"
        });

        // Initialize filters
        statusFilterCombo = new JComboBox<>(new String[]{"All", "Approved", "Under Review", "Disapproved"});
        vehicleCategoryFilterCombo = new JComboBox<>(new String[]{"All", "Motorcycle", "Car", "Truck", "Bus"}); // NEW
        fromDatePicker = new JXDatePicker();
        fromDatePicker.setFormats("yyyy-MM-dd");
        toDatePicker = new JXDatePicker();
        toDatePicker.setFormats("yyyy-MM-dd");
        filterButton = new JButton("Filter");
        clearFilterButton = new JButton("Clear Filter");

        filterButton.addActionListener(e -> refreshTable());
        clearFilterButton.addActionListener(e -> {
            statusFilterCombo.setSelectedIndex(0);
            vehicleCategoryFilterCombo.setSelectedIndex(0);
            fromDatePicker.setDate(null);
            toDatePicker.setDate(null);
            refreshTable();
        });

        // Find and remove the toolbar added by the parent (from BorderLayout.NORTH)
        JToolBar toolbar = null;
        for (Component comp : getComponents()) {
            if (comp instanceof JToolBar) {
                toolbar = (JToolBar) comp;
                break;
            }
        }
        if (toolbar != null) {
            remove(toolbar);
        }

        // Filter panel (no border, to match the toolbar)
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusFilterCombo);
        filterPanel.add(new JLabel("Vehicle Category:"));
        filterPanel.add(vehicleCategoryFilterCombo); // NEW FIELD
        filterPanel.add(new JLabel("From:"));
        filterPanel.add(fromDatePicker);
        filterPanel.add(new JLabel("To:"));
        filterPanel.add(toDatePicker);
        filterPanel.add(filterButton);
        filterPanel.add(clearFilterButton);

        // Unified top panel: toolbar + filters, all in FlowLayout for perfect alignment
        JPanel unifiedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        unifiedPanel.setOpaque(false);
        if (toolbar != null) unifiedPanel.add(toolbar);
        unifiedPanel.add(filterPanel);

        // Put the unified panel at the top
        add(unifiedPanel, BorderLayout.NORTH);

        refreshTable();
    }

    private List<Exam> applyFilters(List<Exam> exams) {
        if (statusFilterCombo == null) return exams;
        String selectedStatus = (String) statusFilterCombo.getSelectedItem();
        if (selectedStatus != null && !"All".equals(selectedStatus)) {
            exams = exams.stream()
                    .filter(exam -> selectedStatus.equals(exam.getResult()))
                    .collect(Collectors.toList());
        }
        String selectedCategory = (String) vehicleCategoryFilterCombo.getSelectedItem();
        if (selectedCategory != null && !"All".equals(selectedCategory)) {
            exams = exams.stream()
                    .filter(exam -> selectedCategory.equals(exam.getVehicleCategory()))
                    .collect(Collectors.toList());
        }
        java.util.Date from = fromDatePicker.getDate();
        java.util.Date to = toDatePicker.getDate();
        if (from != null) {
            exams = exams.stream()
                    .filter(exam -> !exam.getExamDate().before(new Date(from.getTime())))
                    .collect(Collectors.toList());
        }
        if (to != null) {
            exams = exams.stream()
                    .filter(exam -> !exam.getExamDate().after(new Date(to.getTime())))
                    .collect(Collectors.toList());
        }
        return exams;
    }

    @Override
    public void refreshTable() {
        List<Exam> exams = service.getAll();
        exams = applyFilters(exams);
        model.setRowCount(0);
        for (Exam exam : exams) {
            model.addRow(getRowData(exam));
        }
    }

    @Override
    protected Object[] getRowData(Exam exam) {
        return new Object[]{
            exam.getExamCode(),
            exam.getExamType(),
            exam.getExamDate(),
            exam.getResult(),
            exam.getVehicleCategory(), // NEW FIELD
            exam.getExaminerName(),
            exam.getEntityCode(),
            exam.getDriverId()
        };
    }

    @Override
    protected Exam getEntityFromRow(int row) {
        String examCode = (String) table.getValueAt(row, 0);
        return service.getById(examCode);
    }

    @Override
    protected String getEntityIdFromRow(int row) {
        return (String) table.getValueAt(row, 0);
    }

    @Override
    protected void showEditDialog(Exam exam) {
        JTextField txtExamCode = new JTextField(exam.getExamCode());
        txtExamCode.setEditable(false);

        JComboBox<String> cmbExamType = new JComboBox<>(new String[]{"Theory", "Practical", "Medical"});
        cmbExamType.setSelectedItem(exam.getExamType());

        JXDatePicker datePicker = new JXDatePicker();
        datePicker.setDate(exam.getExamDate());

        JComboBox<String> cmbResult = new JComboBox<>(new String[]{"Approved", "Under Review", "Disapproved"});
        cmbResult.setSelectedItem(exam.getResult());

        JComboBox<String> cmbVehicleCategory = new JComboBox<>(new String[]{"Motorcycle", "Car", "Truck", "Bus"});
        cmbVehicleCategory.setSelectedItem(exam.getVehicleCategory());

        JTextField txtExaminerName = new JTextField(exam.getExaminerName());
        JTextField txtEntityCode = new JTextField(exam.getEntityCode());
        JTextField txtDriverId = new JTextField(exam.getDriverId());

        JPanel panel = new JPanel(new java.awt.GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Exam Code:"));
        panel.add(txtExamCode);
        panel.add(new JLabel("Type:"));
        panel.add(cmbExamType);
        panel.add(new JLabel("Date:"));
        panel.add(datePicker);
        panel.add(new JLabel("Result:"));
        panel.add(cmbResult);
        panel.add(new JLabel("Vehicle Category:"));
        panel.add(cmbVehicleCategory);
        panel.add(new JLabel("Examiner Name:"));
        panel.add(txtExaminerName);
        panel.add(new JLabel("Entity Code:"));
        panel.add(txtEntityCode);
        panel.add(new JLabel("Driver ID:"));
        panel.add(txtDriverId);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Edit Exam",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            if (txtExaminerName.getText().trim().isEmpty() ||
                txtEntityCode.getText().trim().isEmpty() ||
                txtDriverId.getText().trim().isEmpty() ||
                datePicker.getDate() == null ||
                cmbVehicleCategory.getSelectedIndex() < 0) {
                JOptionPane.showMessageDialog(this, "All fields must be filled out.");
                return;
            }

            exam.setExamType((String) cmbExamType.getSelectedItem());
            exam.setExamDate(new Date(datePicker.getDate().getTime()));
            exam.setResult((String) cmbResult.getSelectedItem());
            exam.setVehicleCategory((String) cmbVehicleCategory.getSelectedItem());
            exam.setExaminerName(txtExaminerName.getText().trim());
            exam.setEntityCode(txtEntityCode.getText().trim());
            exam.setDriverId(txtDriverId.getText().trim());

            boolean ok = service.update(exam);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Exam successfully updated.");
            } else {
                JOptionPane.showMessageDialog(this, "Error updating exam.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            refreshTable();
        }
    }

    @Override
    protected JButton createAddButton() {
        return new NewExamenButton(null, this::refreshTable);
    }
}