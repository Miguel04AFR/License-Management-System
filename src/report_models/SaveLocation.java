package report_models;

import javax.swing.*;
import java.awt.*;

public class SaveLocation {
    public static String askSaveLocation(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save report as...");
        int userSelection = fileChooser.showSaveDialog(parent);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf"; // adds the file extension if not present
            }
            return filePath;
        }
        return null; // User cancelled operation
    }
}
