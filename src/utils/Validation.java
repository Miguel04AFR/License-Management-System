package utils;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class Validation {
    
    // Validación de campo requerido
    public static ArrayList<String> validateRequired(String value, String fieldName) {
        ArrayList<String> errors = new ArrayList<>();
        if (value == null || value.trim().isEmpty()) {
            errors.add(fieldName + " es requerido");
        }
        return errors;
    }

    // Validación de formato específico
    public static ArrayList<String> validateFormat(String value, String pattern, String errorMessage) {
        ArrayList<String> errors = new ArrayList<>();
        if (value != null && !value.matches(pattern)) {
            errors.add(errorMessage);
        }
        return errors;
    }

    // Validación de fecha mínima
    public static ArrayList<String> validateMinimumAge(Date date, int minAge, String fieldName) {
        ArrayList<String> errors = new ArrayList<>();
        if (date == null) {
            errors.add(fieldName + " es requerido");
            return errors;
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -minAge);
        Date minDate = cal.getTime();

        if (date.after(minDate)) {
            errors.add("Debe tener al menos " + minAge + " años");
        }
        return errors;
    }

    // Validación de selección en JComboBox
    public static ArrayList<String> validateSelection(JComboBox<?> comboBox, String fieldName) {
        ArrayList<String> errors = new ArrayList<>();
        if (comboBox.getSelectedIndex() <= 0) { // Asume que el índice 0 es "Seleccionar..."
            errors.add(fieldName + " debe ser seleccionado");
        }
        return errors;
    }

    // Validación de longitud
    public static ArrayList<String> validateLength(String value, int min, int max, String fieldName) {
        ArrayList<String> errors = new ArrayList<>();
        if (value != null && (value.length() < min || value.length() > max)) {
            errors.add(fieldName + " debe tener entre " + min + " y " + max + " caracteres");
        }
        return errors;
    }

    // Mostrar errores de validación
    public static boolean showErrors(Component parent, ArrayList<String> errors) {
        if (!errors.isEmpty()) {
            StringBuilder message = new StringBuilder("<html><b>Errores de validación:</b><ul>");
            for (String error : errors) {
                message.append("<li>").append(error).append("</li>");
            }
            message.append("</ul></html>");
            
            JOptionPane.showMessageDialog(null,
                message.toString(),
                "Validación Fallida",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    public static ArrayList<String> validateEmail(String email) {
        ArrayList<String> errors = new ArrayList<>();
        
        if (email == null || email.trim().isEmpty()) {           
                errors.add("El email es requerido");
            return errors;
        }
        
        // Expresión regular para validar email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        
        if (!email.matches(emailRegex)) {
            errors.add("Formato de email inválido");
        }
        
        return errors;
    }

    // Validación de número dentro de rango
    public static ArrayList<String> validateNumberRange(Number value, Number min, Number max, String fieldName) {
        ArrayList<String> errors = new ArrayList<>();
        if (value != null && (value.doubleValue() < min.doubleValue() || 
                              value.doubleValue() > max.doubleValue())) {
            errors.add(fieldName + " debe estar entre " + min + " y " + max);
        }
        return errors;
    }

    // Validación combinada
    @SafeVarargs
    public static boolean validateAll(JComponent parent, ArrayList<String>... validationResults) {
        ArrayList<String> allErrors = new ArrayList<>();
        for (ArrayList<String> result : validationResults) {
            allErrors.addAll(result);
        }
        return showErrors(parent, allErrors);
    }

    //Validar id del conductor como el carnet cubano
    public static ArrayList<String> validateID(String id) {
        ArrayList<String> errors = new ArrayList<>();

        if (id == null || id.length() != 11) {
            errors.add("The ID must have exactly 11 digits.");
        }
        if (id != null && !id.matches("\\d{11}")) {
            errors.add("The ID must contain only numbers.");
        }
        // Prevent substring errors if previous checks failed
        if (!errors.isEmpty()) {
            return errors;
        }

        int centuryDigit = Integer.parseInt(id.substring(7, 8));
        int year = Integer.parseInt(id.substring(0, 2));
        int month = Integer.parseInt(id.substring(2, 4));
        int day = Integer.parseInt(id.substring(4, 6));
        int fullYear = 0;

        if (centuryDigit == 9) {
            errors.add("IDs from the 19th century are not valid.");
        }
        if (centuryDigit >= 0 && centuryDigit <= 5) {
            fullYear = 1900 + year;
            if (year < 25) {
                errors.add("Invalid year for the 20th century.");
            }
        } else if (centuryDigit >= 6 && centuryDigit <= 8) {
            fullYear = 2000 + year;
            if (year > 25) {
                errors.add("Invalid year for the 21st century.");
            }
        } else {
            errors.add("Invalid century digit.");
        }
        if (month < 1 || month > 12) {
            errors.add("Month must be between 1 and 12.");
        }
        if (day < 1 || day > 31) {
            errors.add("Day must be between 1 and 31.");
        }
        // Months with 30 days
        if (month != 2) {
            if ((month < 8 && month % 2 == 0 && day > 30) ||
                (month > 7 && month % 2 != 0 && day > 30)) {
                errors.add("This month only has 30 days.");
            }
        }
        // February and leap year
        if (month == 2) {
            boolean leap = (fullYear % 4 == 0 && (fullYear % 100 != 0 || fullYear % 400 == 0));
            if ((leap && day > 29) || (!leap && day > 28)) {
                errors.add("Invalid day for February in the given year.");
            }
        }
        return errors;
    }
        
}