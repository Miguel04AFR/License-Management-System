package visual.Reports;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
<<<<<<< Updated upstream
import java.util.HashMap;
import model.Violation;
import services.ViolationService;

=======

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import model.Violation;
import services.ViolationService;

>>>>>>> Stashed changes
/**
 * Report: Consolidated Infractions by Type in a Year.
 * Shows, for a selected year, the total number of infractions, total points deducted,
 * and count of paid/pending fines, grouped by infraction type.
 */
public class ConsolidatedInfractionsReport extends JPanel {
	  private static final long serialVersionUID = 1L;
    private static final ViolationService violationService = new ViolationService();

    /**
     * Opens the Consolidated Infractions report dialog.
     * @param parent the parent component for dialog centering
     */
    public static void showDialog(Component parent) {
        String yearInput = JOptionPane.showInputDialog(parent, "Enter year (e.g., 2024):", "Year", JOptionPane.QUESTION_MESSAGE);
        if (yearInput == null || yearInput.trim().isEmpty()) return;
        int year;
        try {
            year = Integer.parseInt(yearInput.trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "Invalid year format.", "Input error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Gather all violations for the given year
        List<Violation> allViolations = violationService.getAll();
        // Map: infractionType -> [count, totalPoints, paidCount, pendingCount]
        Map<String, ConsolidatedRow> data = new HashMap<>();

        for (Violation v : allViolations) {
            if (v.getDate() == null) continue;
            Date d = v.getDate();
            @SuppressWarnings("deprecation")
            int violationYear = d.getYear() + 1900;
            if (violationYear != year) continue;
            String type = v.getViolationType();
            if (type == null) type = "(unknown)";
            ConsolidatedRow row = data.getOrDefault(type, new ConsolidatedRow(year, type));
            row.count++;
            row.totalPoints += v.getDeductedPoints();
            if (v.isPaid()) row.paidCount++;
            else row.pendingCount++;
            data.put(type, row);
        }

        // Sort by infraction type (leve, grave, muy grave, others lex)
        String[] order = {"leve", "grave", "muy grave"};
        java.util.List<ConsolidatedRow> rowsList = new java.util.ArrayList<>(data.values());
        rowsList.sort((a, b) -> {
            int ai = indexOf(order, a.infractionType);
            int bi = indexOf(order, b.infractionType);
            if (ai != bi) return Integer.compare(ai, bi);
            return a.infractionType.compareToIgnoreCase(b.infractionType);
        });

        // Prepare table data
        Object[][] rows = new Object[rowsList.size()][6];
        for (int i = 0; i < rowsList.size(); i++) {
            ConsolidatedRow row = rowsList.get(i);
            rows[i][0] = row.year;
            rows[i][1] = capitalize(row.infractionType);
            rows[i][2] = row.count;
            rows[i][3] = row.totalPoints;
            rows[i][4] = row.paidCount;
            rows[i][5] = row.pendingCount;
        }

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), "Consolidated Infractions by Type in Year", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout(18, 18));

        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] columns = {"Year", "Infraction Type", "Infractions Count", "Total Points Deducted", "Total Paid Fines", "Total Pending Fines"};
        JTable table = new JTable(rows, columns);
        table.setEnabled(false);
        table.setRowHeight(22);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(800, Math.min(rowsList.size() * 28 + 24, 350)));
        tablePanel.add(scroll, BorderLayout.CENTER);

        dialog.add(tablePanel, BorderLayout.CENTER);

        // Export button (disabled for now)
        JButton exportButton = new JButton("Export...");
        exportButton.setEnabled(false); // no action yet
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(exportButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private static int indexOf(String[] arr, String val) {
        for (int i = 0; i < arr.length; i++) if (arr[i].equalsIgnoreCase(val)) return i;
        return arr.length + 1;
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private static class ConsolidatedRow {
        int year;
        String infractionType;
        int count = 0;
        int totalPoints = 0;
        int paidCount = 0;
        int pendingCount = 0;

        ConsolidatedRow(int year, String infractionType) {
            this.year = year;
            this.infractionType = infractionType;
        }
    }
}