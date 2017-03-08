package dk.itu.n.danmarkskort;

import javax.swing.*;
import java.awt.*;

public class Main {

	public final static String APP_NAME = "FrankMaps";
	public final static String APP_VERSION = "0.1";
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            startup();
            main();
            makeFrame();
        });
	}

    public static void makeFrame() {
        JFrame window = new JFrame("sick map");
        window.add(new MainPanel());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.pack();
        window.setVisible(true);
    }
	
	public static void log(String text) {
		System.out.println("[" + APP_NAME + " " + APP_VERSION + "] " + text);
	}
	
	public static void startup() {
		log("Application started.");
	}
	
	public static void main() {
		
	}
	
	public static void shutdown() {
		log("Application ended.");
	}

	
}
