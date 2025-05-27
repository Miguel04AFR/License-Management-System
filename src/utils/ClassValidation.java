package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public final class ClassValidation {
		private ClassValidation() {}

		public static boolean validarNumeroRango(double valor, double minV, double maxV) {
			boolean esValido = minV<=maxV;

			if(esValido)
				esValido = valor>=minV && valor<=maxV;

				return esValido;
		}

		public static boolean validarTamString(String s, int min, int max) {
			boolean esValido = s!=null && min<=max;

			if(esValido)
				esValido = s.length()>=min && s.length()<=max;

				return esValido;
		}

		public static boolean validarStringTodoLetra(String s) {
			boolean esValido = s!=null;

			for(int i=0;i<s.length() && esValido;i++)
				esValido = Character.isLetter(s.charAt(i)) || Character.isWhitespace(s.charAt(i));

			return esValido;
		}

		public static boolean validarStringTodoDigito(String s) {
			boolean esValido = s!=null;

			for(int i=0;i<s.length() && esValido;i++)
				esValido = Character.isDigit(s.charAt(i));

			return esValido;
		}

		public static boolean validarStringTodoDigitoLetraSinEspacio(String s) {
			boolean esValido = s!=null;

			for(int i=0;i<s.length() && esValido;i++)
				esValido = Character.isDigit(s.charAt(i)) || Character.isUpperCase(s.charAt(i)) || Character.isLowerCase(s.charAt(i));

			return esValido;
		}

		public static boolean validarStringNoTodoEspacio(String s) {
			boolean esValido = false; 
			
			if(s!=null){
				for(int i=0;i<s.length() && !esValido;i++)
					if(!Character.isWhitespace(s.charAt(i)))
						esValido = true;
			}
			return esValido;
		}

		public static boolean validarStringNoVacio(String s) {
			return (s!=null && !s.isEmpty());
		}

		public static boolean validarStringNoEspacio(String s) {
			boolean esValido = (s!=null);

			for(int i=0;i<s.length() && esValido;i++)
				esValido = !Character.isWhitespace(s.charAt(i));

			return esValido;
		}

		public static boolean validarFechaNacimientoCi(String ci, Date fechaMin, Date fechaMax) {
			boolean esValido = ci!=null;

			if(esValido) {
				int siglo = 0;
				int digitoMilenio = Byte.valueOf(""+ci.charAt(6));

				if(digitoMilenio<=5)
					siglo=20;
				else if(digitoMilenio<=8)
					siglo=21;
				else
					siglo=19;

				String anyo = (siglo-1)+ci.substring(0, 2);
				String mes = ci.substring(2,4);
				String dia = ci.substring(4,6);
				SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
				formato.setLenient(false);
				try {
					formato.parse(dia+"/"+mes+"/"+anyo);
				}catch(Exception e) {
					esValido=false;
				}
			}


			if(esValido){
				Date fecha = Auxiliares.convertirFechaNacimientoCiDate(ci);
				if(fechaMin!=null)
					esValido = fecha.compareTo(fechaMin)>=0;
				if(esValido && fechaMax!=null)
					esValido = fecha.compareTo(fechaMax)<=0;
			}

			return esValido;
		}

		public static boolean validarCI(String ci, Date fechaMin, Date fechaMax) {
			return validarStringNoVacio(ci) && validarTamString(ci,11,11) && validarStringTodoDigito(ci) && validarFechaNacimientoCi(ci,fechaMin,fechaMax);
		}

		public static boolean validarRangoFecha(Date fecha, Date fechaMin, Date fechaMax) {
			boolean esValida = (fecha!=null && fechaMin!=null && fechaMax!=null);

			if(esValida)
				esValida = (fecha.after(Auxiliares.sumarAnyosFecha(fechaMin, -1)) && fecha.before(Auxiliares.sumarAnyosFecha(fechaMax, 1)));

			return esValida;
		}

		public static <T extends Comparable<? super T>> boolean validarNoRepeticionLista(List<T> lista) {
			boolean esValida = lista!=null;

			Collections.sort(lista);

			for(int i=0;i<lista.size()-1 && esValida;i++)
				esValida = !(lista.get(i).equals(lista.get(i+1)));

			return esValida;
		}

		public static <T> boolean validarNoRepeticionLista(List<T> lista, Comparator<? super T> c){
			boolean esValida = lista!=null && c!=null;
			Collections.sort(lista, c);

			for(int i=0;i<lista.size()-1 && esValida;i++)
				esValida = !(lista.get(i).equals(lista.get(i+1)));

			return esValida;
		}

		public static <T extends Comparable<? super T>> boolean validarNoRepeticionElementoLista(List<T> lista, T elemento) {
			boolean esValida = lista!=null && elemento!=null;

			if(esValida) {
				Collections.sort(lista);
				esValida = Collections.binarySearch(lista,elemento)<0;
			}

			return esValida;
		}

		public static <T> boolean validarNoRepeticionElementoLista(List<T> lista, T elemento, Comparator<? super T> c) {
			boolean esValida = lista!=null && elemento!=null && c!=null;

			if(esValida) {
				Collections.sort(lista,c);
				esValida = Collections.binarySearch(lista,elemento,c)<0;
			}

			return esValida;
		}

		@SafeVarargs
		public static <T> boolean validarNoRepeticionListas(List<T>... listas) {
			boolean esValida = (listas!=null);

			for(int i=0;i<listas.length && esValida;i++)
				esValida = listas[i]!=null;

			if(esValida && listas.length>1){
				for(int i=0;i<listas.length && esValida;i++){
					for(int j=i+1;j<listas.length && esValida;j++){
						esValida = Collections.disjoint(listas[i], listas[j]);
					}
				}
			}
			return esValida;
		}

		public static boolean validarCalendarsMismoDia(Calendar c1, Calendar c2) {
			boolean esValido = c1!=null && c2!=null;
			
			if(esValido)
				esValido = c1.get(Calendar.YEAR)==c2.get(Calendar.YEAR);
			if(esValido)
				esValido = c1.get(Calendar.MONTH)==c2.get(Calendar.MONTH);
			if(esValido)
				esValido = c1.get(Calendar.DAY_OF_MONTH)==c2.get(Calendar.DAY_OF_MONTH);
			
			return esValido;
		}

		public static boolean validarDatesMismoDia(Date d1, Date d2) {
			boolean esValido = d1!=null && d2!=null;
			
			if(esValido){
				Calendar c1 = Calendar.getInstance();
				Calendar c2 = Calendar.getInstance();
				c1.setTime(d1);
				c2.setTime(d2);
				esValido = validarCalendarsMismoDia(c1,c2);
			}
			
			return esValido;
			
		}
	}

}
