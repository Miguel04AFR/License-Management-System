package model;

import java.util.List;

import javax.swing.SwingUtilities;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import services.UserService;
import visual.AddRol;
import visual.TrafficLightCarAnimation;

public class Main {
	public static void main(String[] args) {

		 try {
	            UIManager.setLookAndFeel(new FlatLightLaf());
	            SwingUtilities.invokeLater(TrafficLightCarAnimation::new);
	        } catch (Exception ex) {
	            System.err.println("Failed to initialize FlatLaf");
	        }
		/*  UserService usuarios = new UserService();
			List<User> lista = usuarios.getAll(); 
			boolean x = usuarios.autenticar("Jorge", "12345");*/
	        
	}
	
}
