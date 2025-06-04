package model;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import visual.TrafficLightCarAnimation;

public class Main {
	public static void main(String[] args) {

		 try {
	            UIManager.setLookAndFeel(new FlatLightLaf());
	        } catch (Exception ex) {
	            System.err.println("Failed to initialize FlatLaf");
	        }
	        SwingUtilities.invokeLater(TrafficLightCarAnimation::new);
	}
	
		/*  Driver driver = new Driver(
		            "DR123",
		            "Juan",
		            "Pérez",
		            new Date(),
		            "Av. Siempre Viva 123",
		            "555-1234",
		            "juan.perez@email.com",
		            "Vigente"
		        );

		        DriverPDFGenerator.createDriverPDF(driver);}
		        */
}
