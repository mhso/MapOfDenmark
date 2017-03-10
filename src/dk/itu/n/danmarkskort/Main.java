package dk.itu.n.danmarkskort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;

public class Main {

	public final static String APP_NAME = "This is not the map you're looking for";
	public final static String APP_VERSION = "0.1";
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            startup();
            main();
            makeFrame();
        });
	}

    public static void makeFrame() {
        JFrame window = new JFrame("This is not the map you're looking for");
        window.add(new MainCanvas());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
		});
        window.pack();
		window.setLocationRelativeTo(null);
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
