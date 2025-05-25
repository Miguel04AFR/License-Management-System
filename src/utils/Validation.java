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
    public static boolean validarCarnet(String cadena) {
		int longitud = cadena.length();
		boolean valido = true;

		if (longitud != 11) {
			valido = false;
		}

		int i = 0;
		while (i < longitud && valido) {
			if (!Character.isDigit(cadena.charAt(i))) {
				valido = false;
			}
			i++;
		}

		String siglo = cadena.substring(7, 8);
		int compSiglo = Integer.parseInt(siglo);

		if (compSiglo == 9 && valido) {
			valido = false; // Siglo XIX
		}

		String anno = cadena.substring(0, 2);
		int compAnno = Integer.parseInt(anno);
        int annoCompleto;
		if (valido && ((compSiglo >= 6 && compSiglo < 9) && compAnno > 25)) {
			valido = false; // Siglo XXI y año mayor que el actual (inválido).
		}

		if (valido && ((compSiglo >= 0 && compSiglo < 6) && compAnno < 25)) {
			valido = false; // Siglo XX y mayor que 100 años.
		}

        if (siglo >= 0 && siglo <= 5) {
        annoCompleto = 1900 + anno;
    } else if (siglo >= 6 && siglo <= 8) {
        annoCompleto = 2000 + anno;
    }

		String mes = cadena.substring(2, 4);
		int compMes = Integer.parseInt(mes);

		if (compMes < 1 || compMes > 12) {
			valido = false; // Solo 12 meses.
		}

		String dia = cadena.substring(4, 6);
		int compDia = Integer.parseInt(dia);

		if (valido && compDia < 1) {
			valido = false;
		}
		if (valido && compDia > 31) {
			valido = false;
		}
		if (valido && ((compMes < 8 && compMes != 2 && compMes % 2 == 0) && compDia > 30)) {
			valido = false;
		}
		if (valido && ((compMes > 7 && compMes != 2 && compMes % 2 != 0) && compDia > 30)) {
			valido = false;
		}

		// Año bisiesto y validación de febrero
    if (mes == 2) {
        boolean bisiesto = (annoCompleto % 4 == 0 && (annoCompleto % 100 != 0 || annoCompleto % 400 == 0));
        if (bisiesto && dia > 29){
 			valido = false;
		}
        if (!bisiesto && dia > 28){
			valido = false;
    	}
    }
		return valido;
}
        
}
