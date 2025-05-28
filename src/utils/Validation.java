package utils;

import java.awt.Component;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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

        LocalDate birthDate = date.toInstant()
                               .atZone(ZoneId.systemDefault())
                               .toLocalDate();
        LocalDate minDate = LocalDate.now().minusYears(minAge);

        if (birthDate.isAfter(minDate)) {
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
    public static boolean validateAll(JComponent parent, ArrayList<String>... validationResults) {
        ArrayList<String> allErrors = new ArrayList<>();
        for (ArrayList<String> result : validationResults) {
            allErrors.addAll(result);
        }
        return showErrors(parent, allErrors);
    }

    //Validar id del conductor como el carnet cubano
    public static ArrayList<String>  validateID(String cadena) {
    	ArrayList<String> errors = new ArrayList<>();
		int lengthText = cadena.length();
		boolean validate = true;

		if (lengthText != 11) {
			validate = false;
		}

		int i = 0;
		while (i < lengthText && validate) {
			if (!Character.isDigit(cadena.charAt(i))) {
				validate = false;
			}
			i++;
		}

		String century = cadena.substring(7, 8);
		int compCentury = Integer.parseInt(century);

		if (compCentury == 9 && validate) {
			validate = false; // Siglo XIX
		}

		String year = cadena.substring(0, 2);
		int compYear = Integer.parseInt(year);
        int yearComplete=0;
		if (validate && ((compCentury >= 6 && compCentury < 9) && compYear > 25)) {
			validate = false; // Siglo XXI y año mayor que el actual (inválido).
		}

		if (validate && ((compCentury >= 0 && compCentury < 6) && compYear < 25)) {
			validate = false; // Siglo XX y mayor que 100 años.
		}

	    if(validate){
        if (compCentury >= 0 && compCentury <= 5) {
        	yearComplete = 1900 + compYear;
    } else if (compCentury >= 6 && compCentury <= 8) {
    	yearComplete = 2000 + compYear;
    }
	    }

		String month = cadena.substring(2, 4);
		int compMonth = Integer.parseInt(month);

		if (compMonth < 1 || compMonth > 12) {
			validate = false; // Solo 12 meses.
		}

		String day = cadena.substring(4, 6);
		int compDay = Integer.parseInt(day);

		if (validate && compDay < 1) {
			validate = false;
		}
		if (validate && compDay > 31) {
			validate = false;
		}
		if (validate && ((compMonth < 8 && compMonth != 2 && compMonth % 2 == 0) && compDay > 30)) {
			validate = false;
		}
		if (validate && ((compMonth > 7 && compMonth != 2 && compMonth % 2 != 0) && compDay > 30)) {
			validate = false;
		}

		// Año bisiesto y validación de febrero
    if (compMonth == 2 && validate) {
        boolean bisiesto = (yearComplete % 4 == 0 && (yearComplete % 100 != 0 || yearComplete % 400 == 0));
        if (bisiesto && compDay > 29){
        	validate = false;
		}
        if (!bisiesto && compDay > 28){
        	validate = false;
    	}
    }
    if(!validate)
    	errors.add("ID  Invalid");
		return errors;
}
        
}