package model;

import javax.swing.SwingUtilities;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import visual.Login;
import visual.TrafficLightCarAnimation;

public class Main {
	public static void main(String[] args) {

		 try {
	            UIManager.setLookAndFeel(new FlatLightLaf());
	            SwingUtilities.invokeLater(TrafficLightCarAnimation::new);
	        } catch (Exception ex) {
	            System.err.println("Failed to initialize FlatLaf");
	        }
	        
	}
	
}
