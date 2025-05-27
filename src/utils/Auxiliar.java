package utils;

import java.util.Calendar;
import java.util.Date;

public final class Auxiliares {
    
    private Auxiliares() {}

    // Convierte la fecha de nacimiento contenida en el CI a un objeto Date
    public static Date convertirFechaNacimientoCiDate(String ci) {
        if (ci == null || ci.length() < 6) {
            throw new IllegalArgumentException("CI inválido.");
        }

        int siglo = 0;
        int digitoMilenio = Character.getNumericValue(ci.charAt(6));

        if (digitoMilenio <= 5)
            siglo = 20;
        else if (digitoMilenio <= 8)
            siglo = 21;
        else
            siglo = 19;

        int anio = Integer.parseInt((siglo - 1) + ci.substring(0, 2));
        int mes = Integer.parseInt(ci.substring(2, 4)) - 1; // En Calendar, enero es 0
        int dia = Integer.parseInt(ci.substring(4, 6));

        Calendar calendario = Calendar.getInstance();
        calendario.set(Calendar.YEAR, anio);
        calendario.set(Calendar.MONTH, mes);
        calendario.set(Calendar.DAY_OF_MONTH, dia);

        return calendario.getTime();
    }

    // Suma o resta años a una fecha
    public static Date sumarAnyosFecha(Date fecha, int cantidadAnyos) {
        if (fecha == null) {
            throw new IllegalArgumentException("Fecha no puede ser nula.");
        }

        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        calendario.add(Calendar.YEAR, cantidadAnyos);
        
        return calendario.getTime();
    }
}
